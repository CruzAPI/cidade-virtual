package com.eul4.model.inventory.craft;

import com.eul4.common.model.player.CommonPlayer;
import com.eul4.i18n.PluginMessage;
import com.eul4.model.inventory.TownHallGui;
import com.eul4.model.town.structure.Structure;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

@Getter
public class CraftTownHallGui extends CraftStructureGui implements TownHallGui
{
	private final ItemStack assistantMenu;
	
	public CraftTownHallGui(CommonPlayer commonPlayer, Structure structure)
	{
		super(commonPlayer, structure, commonPlayer.createInventory(InventoryType.HOPPER,
				PluginMessage.STRUCTURE_TITLE,
				PluginMessage.STRUCTURE_TOWN_HALL_NAME,
				structure.getLevel()));
		
		ItemMeta meta;
		
		assistantMenu = ItemStack.of(Material.VILLAGER_SPAWN_EGG);
		meta = assistantMenu.getItemMeta();
		meta.displayName(PluginMessage.INVENTORY_TOWN_HALL_ASSISTANT_MENU.translateOne(commonPlayer));
		assistantMenu.setItemMeta(meta);
		
		inventory.setItem(1, move);
		inventory.setItem(4, assistantMenu);
	}
	
	@Override
	public Component getUpdatedTitleComponent()
	{
		return PluginMessage.STRUCTURE_TITLE.translateOne(commonPlayer.getLocale(),
				PluginMessage.STRUCTURE_TOWN_HALL_NAME,
				structure.getLevel());
	}
	
	@Override
	public ItemStack getAssistantMenuIcon()
	{
		return assistantMenu.clone();
	}
}
