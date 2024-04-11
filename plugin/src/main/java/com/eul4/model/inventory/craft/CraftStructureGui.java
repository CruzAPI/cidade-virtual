package com.eul4.model.inventory.craft;

import com.eul4.common.model.inventory.UpdatableGui;
import com.eul4.common.model.inventory.craft.CraftGui;
import com.eul4.common.model.player.CommonPlayer;
import com.eul4.common.wrapper.TimerTranslater;
import com.eul4.enums.StructureStatus;
import com.eul4.i18n.PluginMessage;
import com.eul4.model.inventory.StructureGui;
import com.eul4.model.town.structure.Generator;
import com.eul4.model.town.structure.Structure;
import com.eul4.rule.GenericAttribute;
import joptsimple.internal.Strings;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.apache.commons.lang.WordUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import javax.naming.Name;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.eul4.i18n.PluginMessage.CLICK_TO_FINISH_BUILD;
import static net.kyori.adventure.text.format.NamedTextColor.GREEN;
import static net.kyori.adventure.text.format.TextDecoration.BOLD;

@Getter
public abstract class CraftStructureGui extends CraftGui implements StructureGui, UpdatableGui
{
	protected final Structure structure;
	
	protected final ItemStack upgrade;
	protected final ItemStack move;
	protected final ItemStack finishBuild;
	
	private BukkitRunnable updateTask;
	
	public CraftStructureGui(CommonPlayer commonPlayer, Structure structure, Inventory inventory)
	{
		super(commonPlayer, inventory);
		
		this.structure = structure;
		
		ItemMeta meta;
		
		upgrade = new ItemStack(Material.EXPERIENCE_BOTTLE);
		
		
		move = new ItemStack(Material.TARGET);
		meta = move.getItemMeta();
		meta.setDisplayName("Move Structure");
		move.setItemMeta(meta);
		
		finishBuild = new ItemStack(Material.LIME_CONCRETE);
		meta = finishBuild.getItemMeta();
		meta.setDisplayName("Finish Build");
		finishBuild.setItemMeta(meta);
	}
	
	@Override
	public void updateInventory()
	{
		ItemMeta meta;
		
		inventory.setItem(0, upgrade);
		
		GenericAttribute currentAttribute = structure.getRule().getAttribute(structure.getLevel());
		GenericAttribute nextAttribute = structure.getRule().getAttribute(structure.getLevel() + 1);
		
		meta = upgrade.getItemMeta();
		
		meta.displayName(PluginMessage.UPGRADE.translateWord(commonPlayer, WordUtils::capitalize)
				.color(GREEN)
				.decorate(BOLD));
		
		if(currentAttribute == null || nextAttribute == null)
		{
			meta.lore(PluginMessage.STRUCTURE_MAX_UPGRADE_REACHED.translateLore(commonPlayer));
		}
		else
		{
			meta.lore(structure.getStructureType().getUpgradePreviewLoreMessage()
					.translateLore(commonPlayer, currentAttribute, nextAttribute));
		}
		
		upgrade.setItemMeta(meta);
		
		inventory.setItem(1, move);
		
		if(structure.getStatus() != StructureStatus.BUILT)
		{
			close();
		}
	}
	
	@Override
	public void scheduleUpdate()
	{
		if(getPlugin().isQueued(updateTask))
		{
			return;
		}
		
		updateTask = new BukkitRunnable()
		{
			@Override
			public void run()
			{
				updateInventory();
			}
		};
		
		updateTask.runTaskTimer(getPlugin(), 0L, 1L);
	}
	
	@Override
	public void cancelScheduleUpdate()
	{
		Optional.ofNullable(updateTask).ifPresent(BukkitRunnable::cancel);
	}
	
	@Override
	public void updateTitle()
	{
		if(commonPlayer.getGui() != this
				|| commonPlayer.getPlayer().getOpenInventory().getTopInventory() != inventory)
		{
			return;
		}
		
		Component updatedTitleComponent = getUpdatedTitleComponent();
		
		String legacyText = LegacyComponentSerializer.legacySection().serialize(updatedTitleComponent);
		
		commonPlayer.getPlayer().getOpenInventory().setTitle(legacyText);
	}
}
