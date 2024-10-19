package com.eul4.model.player.performer;

import com.eul4.common.model.player.CommonPlayer;
import com.eul4.i18n.PluginMessage;
import com.eul4.model.player.Channeler;
import com.eul4.model.player.PluginPlayer;
import com.eul4.model.player.physical.SpawnPlayer;
import com.eul4.type.PluginWorldType;
import com.eul4.type.player.PhysicalPlayerType;
import com.eul4.world.NewbieLevel;

public interface NewbiePerformer extends PluginPlayer
{
	boolean performNewbie();
	
	default boolean tryTeleportNewbie()
	{
		if(canTeleportNewbie())
		{
			return teleportNewbie();
		}
		
		return false;
	}
	
	default boolean teleportNewbie()
	{
		return getPlayer().teleport(PluginWorldType.NEWBIE_WORLD.getInstance().getSpawnLocation());
	}
	
	default boolean canTeleportNewbie()
	{
		if(getCommonWorld() instanceof NewbieLevel)
		{
			sendMessage(PluginMessage.COMMAND_NEWBIE_ALREADY_IN_WORLD);
			return false;
		}
		
		return true;
	}
	
	interface Teleport extends NewbiePerformer
	{
		@Override
		default boolean performNewbie()
		{
			return tryTeleportNewbie();
		}
	}
	
	interface Reincarnate extends NewbiePerformer.Teleport
	{
		@Override
		default boolean performNewbie()
		{
			if(canTeleportNewbie())
			{
				CommonPlayer commonPlayer = getPlugin().getPlayerManager().register(this, PhysicalPlayerType.SPAWN_PLAYER);
				
				if(commonPlayer instanceof SpawnPlayer)
				{
					return teleportNewbie();
				}
			}
			
			return false;
		}
	}
	
	interface ChannelReincarnation extends Reincarnate, Channeler
	{
		@Override
		default boolean performNewbie()
		{
			if(canTeleportNewbie())
			{
				channel(8L * 20L, Reincarnate.super::performNewbie);
				return true;
			}
			
			return false;
		}
	}
}
