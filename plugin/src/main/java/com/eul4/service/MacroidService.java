package com.eul4.service;

import com.eul4.Main;
import com.eul4.wrapper.Macroid;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.BuiltInClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardWriter;
import com.sk89q.worldedit.function.operation.ForwardExtentCopy;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.regions.Region;
import lombok.RequiredArgsConstructor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.io.File;
import java.io.FileOutputStream;
import java.text.MessageFormat;
import java.util.*;
import java.util.logging.Level;

import static com.eul4.enums.PluginNamespacedKey.MACROID_WAND_UUID;
import static org.bukkit.persistence.PersistentDataType.LONG_ARRAY;

@RequiredArgsConstructor
public class MacroidService
{
	private final Main plugin;
	
	private final Map<UUID, Macroid> macroids = new HashMap<>();
	
	public boolean cancel(UUID macroidUUID)
	{
		return macroids.remove(macroidUUID) != null;
	}
	
	public void finish(UUID uuid, Macroid macroid)
	{
		if(!macroids.containsKey(uuid))
		{
			macroid.getPlayer().sendMessage("Macroid not found.");
			return;
		}
		
		saveStructureSchematics(macroid);
	}
	
	public void newMacroidWand(Macroid macroid)
	{
		UUID uuid = UUID.randomUUID();
		
		macroids.put(uuid, macroid);
		
		ItemStack item = new ItemStack(Material.STONE_AXE);
		ItemMeta meta = item.getItemMeta();
		var container = meta.getPersistentDataContainer();
		container.set(MACROID_WAND_UUID, LONG_ARRAY, uuidToLongArray(uuid));
		item.setItemMeta(meta);
		
		macroid.getPlayer().getInventory().addItem(item);
	}
	
	private long[] uuidToLongArray(UUID uuid)
	{
		return new long[] { uuid.getMostSignificantBits(), uuid.getLeastSignificantBits() };
	}
	
	public void saveStructureSchematics(Macroid macroid)
	{
		List<Clipboard> clipboards = macroid.getClipboards();
		
		for(int i = 0; i < clipboards.size(); i++)
		{
			Clipboard clipboard = clipboards.get(i);
			
			int level = i + 1;
			saveSchematic(macroid.getPlayer(), macroid.getFileName(level), clipboard);
		}
	}
	
	private void saveSchematic(Player player, String fileName, Clipboard clipboard)
	{
		try
		{
			Region region = clipboard.getRegion();
			var weWorld = region.getWorld();
			
			try(EditSession editSession = WorldEdit.getInstance().newEditSession(weWorld))
			{
				ForwardExtentCopy operation = new ForwardExtentCopy(editSession, region, clipboard, region.getMinimumPoint());
				operation.setCopyingEntities(false);
				Operations.complete(operation);
			}
			
			File file = new File(plugin.getSchematicsFolder(), fileName);
			
			file.createNewFile();
			
			try(FileOutputStream fileOutputStream = new FileOutputStream(file);
					ClipboardWriter writer = BuiltInClipboardFormat.FAST.getWriter(fileOutputStream))
			{
				writer.write(clipboard);
				player.sendMessage(MessageFormat.format("Schematic {0} saved!", file.getName()));
			}
		}
		catch(Exception e)
		{
			plugin.getLogger().log(Level.WARNING, MessageFormat.format(
					"class=MacroidService, Failed to save {0} schematic!",
					fileName), e);
			player.sendMessage(MessageFormat.format("Failed to save {0} schematic!", fileName));
		}
	}
	
	public Macroid getMacroid(UUID macroidUUID)
	{
		return macroids.get(macroidUUID);
	}
	
	public UUID getMacroidWandUUID(ItemStack item)
	{
		return Optional
				.ofNullable(item.getItemMeta())
				.map(ItemMeta::getPersistentDataContainer)
				.map(container -> container.get(MACROID_WAND_UUID, PersistentDataType.LONG_ARRAY))
				.map(bits -> new UUID(bits[0], bits[1]))
				.orElse(null);
	}
	
	public boolean isMacroidWand(ItemStack item)
	{
		return getMacroidWandUUID(item) != null;
	}
}
