package com.eul4.listener.player;

import com.destroystokyo.paper.event.player.PlayerPostRespawnEvent;
import com.eul4.Main;
import com.eul4.common.world.CommonWorld;
import com.eul4.model.player.physical.VanillaPlayer;
import com.eul4.type.PluginWorldType;
import com.eul4.world.RaidLevel;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

@RequiredArgsConstructor
public class VanillaListener implements Listener
{
	private final Main plugin;
	
	@EventHandler
	public void on(PlayerRespawnEvent event)
	{
		Player player = event.getPlayer();
		
		if(player.getRespawnLocation() != null
				|| !(plugin.getPlayerManager().get(player) instanceof VanillaPlayer vanillaPlayer))
		{
			return;
		}
		
		CommonWorld commonWorld = vanillaPlayer.getCommonWorld();
		
		if(commonWorld instanceof RaidLevel)
		{
			event.setRespawnLocation(PluginWorldType.RAID_WORLD.getInstance().getSpawnLocation());
		}
		else
		{
			event.setRespawnLocation(PluginWorldType.NEWBIE_WORLD.getInstance().getSpawnLocation());
		}
	}
	
	@EventHandler
	public void on(PlayerPostRespawnEvent event)
	{
		Player player = event.getPlayer();
		
		if(!(plugin.getPlayerManager().get(player) instanceof VanillaPlayer vanillaPlayer))
		{
			return;
		}
		
		if(player.getRespawnLocation() == null)
		{
			vanillaPlayer.performSpawnSkipChanneling();
		}
	}
}
