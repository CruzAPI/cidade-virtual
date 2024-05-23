package com.eul4.model.player;

import com.eul4.common.model.player.CommonPlayer;
import com.eul4.exception.CannotConstructException;
import com.eul4.type.player.PhysicalPlayerType;

import java.io.IOException;

public sealed interface TownPerformer extends PluginPlayer
		permits TownPerformer.TeleportInside, TownPerformer.TeleportOutside
{
	default void performTown()
	{
		try
		{
			getPlugin().getTownManager().getOrCreateNewTown(getUniqueId());
			execute();
		}
		catch(CannotConstructException | IOException e)
		{
			throw new RuntimeException(e); //TODO message
		}
	}
	
	void execute();
	
	non-sealed interface TeleportInside extends TownPerformer
	{
		@Override
		default void execute()
		{
			teleportToTownHall();
		}
	}
	
	non-sealed interface TeleportOutside extends TownPerformer
	{
		@Override
		default void execute()
		{
			teleportToHighestTownHall();
		}
	}
	
	interface Reincarnate extends TeleportInside
	{
		@Override
		default void execute()
		{
			CommonPlayer commonPlayer = getPlugin().getPlayerManager().register(this, PhysicalPlayerType.TOWN_PLAYER);
			
			if(commonPlayer instanceof TownPlayer)
			{
				TeleportInside.super.execute();
			}
		}
	}
}
