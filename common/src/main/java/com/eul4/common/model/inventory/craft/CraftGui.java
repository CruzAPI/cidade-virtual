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
	private final CommonPlayer commonPlayer;
	private final Inventory inventory;
	
	protected CraftGui(CommonPlayer commonPlayer, Supplier<Inventory> inventorySupplier)
	{
		this(commonPlayer, inventorySupplier.get());
	}
	
	@Override
	public Common getPlugin()
	{
		return commonPlayer.getPlugin();
	}
}
