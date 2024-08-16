package com.eul4.model.inventory.craft;

import com.eul4.common.model.inventory.craft.CraftGui;
import com.eul4.model.inventory.ArmorySelectOrStorageItemsGui;
import com.eul4.model.player.PluginPlayer;
import com.eul4.model.town.structure.Armory;
import lombok.Getter;

import static com.eul4.i18n.PluginMessage.INVENTORY_ARMORY_STORAGE_TITLE;

public class CraftArmorySelectOrStorageItemsGui extends CraftGui implements ArmorySelectOrStorageItemsGui
{
	@Getter
	private final Armory armory;
	
	public CraftArmorySelectOrStorageItemsGui(PluginPlayer pluginPlayer, Armory armory)
	{
		super(pluginPlayer, armory.getStorageInventoryClone(INVENTORY_ARMORY_STORAGE_TITLE.translate(pluginPlayer)));
		
		this.armory = armory;
	}
	
	@Override
	public void updateTitle()
	{
	
	}
}
