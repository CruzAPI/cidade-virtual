package com.eul4.model.inventory.craft;

import com.eul4.common.model.player.CommonPlayer;
import com.eul4.i18n.PluginMessage;
import com.eul4.model.town.structure.Structure;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.plugin.Plugin;

@Getter
public class CraftTownHallGui extends CraftStructureGui
{
	public CraftTownHallGui(CommonPlayer commonPlayer, Structure structure)
	{
		super(commonPlayer, structure, commonPlayer.createInventory(InventoryType.HOPPER,
				PluginMessage.STRUCTURE_TITLE,
				PluginMessage.STRUCTURE_TOWN_HALL_NAME,
				structure.getLevel()));
		
		inventory.setItem(1, move);
	}
	
	@Override
	public Component getUpdatedTitleComponent()
	{
		return PluginMessage.STRUCTURE_TITLE.translate(commonPlayer.getLocale(),
				PluginMessage.STRUCTURE_TOWN_HALL_NAME,
				structure.getLevel());
	}
}
