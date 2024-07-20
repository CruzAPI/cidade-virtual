package com.eul4.model.player;

import com.eul4.model.inventory.craft.CraftStructureShopGui;

public interface BuyStructurePerformer extends PluginPlayer
{
	default boolean performBuyStructure()
	{
		openGui(new CraftStructureShopGui(this));
		return true;
	}
}
