package com.eul4.task;

import com.eul4.Main;
import com.eul4.common.model.player.CommonPlayer;
import com.eul4.model.player.physical.SpawnPlayer;
import com.eul4.world.SpawnProtectedLevel;
import lombok.RequiredArgsConstructor;
import org.bukkit.scheduler.BukkitRunnable;

@RequiredArgsConstructor
public class SpawnProtectionTask extends BukkitRunnable
{
	private final Main plugin;
	
	@Override
	public void run()
	{
		for(CommonPlayer commonPlayer : plugin.getPlayerManager().getAll())
		{
			if(!(commonPlayer instanceof SpawnPlayer spawnPlayer))
			{
				continue;
			}
			
			if(!(spawnPlayer.getCommonWorld() instanceof SpawnProtectedLevel spawnProtectedLevel)
					|| !spawnProtectedLevel.isSafeZone(spawnPlayer.getPlayer().getLocation()))
			{
				spawnPlayer.removeProtection();
			}
		}
	}
}
