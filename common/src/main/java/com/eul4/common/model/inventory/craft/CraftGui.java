package com.eul4.common.model.inventory.craft;

import com.eul4.common.Common;
import com.eul4.common.model.inventory.Gui;
import com.eul4.common.model.player.CommonPlayer;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.inventory.Inventory;

import java.util.function.Supplier;

@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public abstract class CraftGui implements Gui
{
	protected final CommonPlayer commonPlayer;
	protected final Inventory inventory;
	
	protected CraftGui(CommonPlayer commonPlayer, Supplier<Inventory> inventorySupplier)
	{
		this(commonPlayer, inventorySupplier.get());
	}
	
	@Override
	public Common getPlugin()
	{
		return commonPlayer.getPlugin();
	}
	
	@Override
	public void close()
	{
		if(commonPlayer.getGui() == this
				&& commonPlayer.getPlayer().getOpenInventory().getTopInventory() == inventory)
		{
			commonPlayer.getPlayer().closeInventory();
		}
	}
}
