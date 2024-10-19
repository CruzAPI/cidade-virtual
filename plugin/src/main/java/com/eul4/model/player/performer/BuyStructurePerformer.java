package com.eul4.model.player.performer;

import com.eul4.model.inventory.craft.CraftStructureShopGui;
import com.eul4.model.player.PluginPlayer;

public interface BuyStructurePerformer extends PluginPlayer
{
	default boolean performBuyStructure()
	{
		openGui(new CraftStructureShopGui(this));
		return true;
	}
}
