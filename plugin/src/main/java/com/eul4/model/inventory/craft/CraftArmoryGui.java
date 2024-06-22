package com.eul4.model.inventory.craft;

import com.eul4.common.model.player.CommonPlayer;
import com.eul4.i18n.PluginMessage;
import com.eul4.model.inventory.ArmoryGui;
import com.eul4.model.town.structure.Structure;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

@Getter
public class CraftArmoryGui extends CraftStructureGui implements ArmoryGui
{
	private final ItemStack menu;
	
	public CraftArmoryGui(CommonPlayer commonPlayer, Structure structure)
	{
		super(commonPlayer, structure, commonPlayer.createInventory(InventoryType.HOPPER,
				PluginMessage.STRUCTURE_TITLE,
				PluginMessage.STRUCTURE_ARMORY_NAME,
				structure.getLevel()));
		
		ItemMeta meta;
		
		menu = new ItemStack(Material.SPRUCE_HANGING_SIGN);
		meta = menu.getItemMeta();
		meta.displayName(Component.empty());
		menu.setItemMeta(meta);
		
		inventory.setItem(4, menu);
	}
	
	@Override
	public Component getUpdatedTitleComponent()
	{
		return PluginMessage.STRUCTURE_TITLE.translate(commonPlayer.getLocale(),
				PluginMessage.STRUCTURE_ARMORY_NAME,
				structure.getLevel());
	}
	
	@Override
	public ItemStack getMenuIcon()
	{
		return menu;
	}
}
