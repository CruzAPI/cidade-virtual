package com.eul4.model.inventory.craft;

import com.eul4.common.model.inventory.craft.CraftGui;
import com.eul4.common.model.player.CommonPlayer;
import com.eul4.model.inventory.StructureGui;
import com.eul4.model.town.structure.Structure;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

@Getter
public class CraftStructureGui extends CraftGui implements StructureGui
{
	protected final Structure structure;
	
	public CraftStructureGui(CommonPlayer commonPlayer, Structure structure)
	{
		super(commonPlayer, () ->
		{
			Inventory inventory = commonPlayer.getPlugin()
					.getServer()
					.createInventory(commonPlayer.getPlayer(), InventoryType.HOPPER);
			
			ItemStack info = new ItemStack(Material.PAPER);
			ItemMeta meta = info.getItemMeta();
			meta.setDisplayName("structure: " + structure.getStructureType().getName()
					+ " level: " + structure.getLevel());
			info.setItemMeta(meta);
			
			ItemStack move = new ItemStack(Material.TARGET);
			ItemMeta moveMeta = move.getItemMeta();
			moveMeta.setDisplayName("MOVE STRUCTURE");
			move.setItemMeta(moveMeta);
			
			inventory.setItem(0, info);
			inventory.setItem(1, move);
			
			return inventory;
		});
		
		this.structure = structure;
	}
}
