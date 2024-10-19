package com.eul4.model.player.performer;

import com.eul4.common.model.player.CommonPlayer;
import com.eul4.i18n.PluginMessage;
import com.eul4.model.player.Channeler;
import com.eul4.model.player.PluginPlayer;
import com.eul4.model.player.physical.SpawnPlayer;
import com.eul4.type.PluginWorldType;
import com.eul4.type.player.PhysicalPlayerType;
import com.eul4.world.RaidLevel;

public interface RaidPerformer extends PluginPlayer
{
	boolean performRaid();
	
	default boolean teleportRaid()
	{
		return getPlayer().teleport(PluginWorldType.RAID_WORLD.getInstance().getSpawnLocation());
	}
	
	default boolean tryTeleportRaid()
	{
		if(canTeleportRaid())
		{
			return teleportRaid();
		}
		
		return false;
	}
	
	default boolean canTeleportRaid()
	{
		if(getCommonWorld() instanceof RaidLevel)
		{
			sendMessage(PluginMessage.COMMAND_RAID_ALREADY_IN_WORLD);
			return false;
		}
		
		return true;
	}
	
	interface Teleport extends RaidPerformer
	{
		@Override
		default boolean performRaid()
		{
			return tryTeleportRaid();
		}
	}
	
	interface Reincarnate extends RaidPerformer.Teleport
	{
		@Override
		default boolean performRaid()
		{
			if(canTeleportRaid())
			{
				CommonPlayer commonPlayer = getPlugin().getPlayerManager().register(this, PhysicalPlayerType.SPAWN_PLAYER);
				
				if(commonPlayer instanceof SpawnPlayer)
				{
					return teleportRaid();
				}
			}
			
			return false;
		}
	}
	
	interface ChannelReincarnation extends Reincarnate, Channeler
	{
		@Override
		default boolean performRaid()
		{
			if(canTeleportRaid())
			{
				channel(8L * 20L, Reincarnate.super::performRaid);
				return true;
			}
			
			return false;
		}
	}
}
