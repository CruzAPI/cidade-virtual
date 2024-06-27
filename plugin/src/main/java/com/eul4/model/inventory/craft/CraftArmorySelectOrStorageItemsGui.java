package com.eul4.model.inventory.craft;

import com.eul4.common.model.inventory.craft.CraftGui;
import com.eul4.model.inventory.ArmorySelectOrStorageItemsGui;
import com.eul4.model.player.PluginPlayer;
import com.eul4.model.town.structure.Armory;
import lombok.Getter;

public class CraftArmorySelectOrStorageItemsGui extends CraftGui implements ArmorySelectOrStorageItemsGui
{
	@Getter
	private final Armory armory;
	
	public CraftArmorySelectOrStorageItemsGui(PluginPlayer pluginPlayer, Armory armory)
	{
		//TODO title
		super(pluginPlayer, armory.getStorageInventoryClone());
		
		this.armory = armory;
	}
	
	@Override
	public void updateTitle()
	{
	
	}
}
