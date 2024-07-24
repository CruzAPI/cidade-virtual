package com.eul4.model.player;

import com.eul4.common.model.player.CommonPlayer;
import com.eul4.type.PluginWorldType;
import com.eul4.type.player.PhysicalPlayerType;

public interface SpawnPerformer extends PluginPlayer
{
	void performSpawn();
	
	interface Teleport extends SpawnPerformer
	{
		@Override
		default void performSpawn()
		{
			getPlayer().teleport(PluginWorldType.OVER_WORLD.getInstance().getSpawnLocation());
		}
	}
	
	interface Reincarnation extends SpawnPerformer.Teleport
	{
		@Override
		default void performSpawn()
		{
			CommonPlayer commonPlayer = getPlugin().getPlayerManager().register(this, PhysicalPlayerType.SPAWN_PLAYER);
			
			if(commonPlayer instanceof SpawnPlayer)
			{
				Teleport.super.performSpawn();
			}
		}
	}
}
