package com.eul4.model.inventory.craft;

import com.eul4.common.model.inventory.craft.CraftGui;
import com.eul4.common.model.player.CommonPlayer;
import com.eul4.i18n.PluginMessage;
import com.eul4.model.inventory.StructureGui;
import com.eul4.model.town.structure.Generator;
import com.eul4.model.town.structure.Structure;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

@Getter
public abstract class CraftStructureGui extends CraftGui implements StructureGui
{
	protected final Structure structure;
	
	protected final ItemStack info;
	protected final ItemStack move;
	
	public CraftStructureGui(CommonPlayer commonPlayer, Structure structure, Inventory inventory)
	{
		super(commonPlayer, inventory);
		
		this.structure = structure;
		
		this.info = new ItemStack(Material.PAPER);
		ItemMeta meta = info.getItemMeta();
		meta.setDisplayName("structure: " + structure.getStructureType().getName()
				+ " level: " + structure.getLevel());
		info.setItemMeta(meta);
		
		this.move = new ItemStack(Material.TARGET);
		ItemMeta moveMeta = move.getItemMeta();
		moveMeta.setDisplayName("MOVE STRUCTURE");
		move.setItemMeta(moveMeta);
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
