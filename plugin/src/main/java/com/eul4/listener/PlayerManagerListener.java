package com.eul4.listener;

import com.eul4.Main;
import com.eul4.common.model.player.CommonPlayer;
import com.eul4.common.world.CommonWorld;
import com.eul4.externalizer.filer.PlayerDataFiler;
import com.eul4.model.player.PluginPlayer;
import com.eul4.type.PluginWorldType;
import com.eul4.type.player.PhysicalPlayerType;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.text.MessageFormat;

@RequiredArgsConstructor
public class PlayerManagerListener implements Listener
{
	private final Main plugin;
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event)
	{
		Player player = event.getPlayer();
		
		PlayerDataFiler playerDataFiler = plugin.getPlayerDataFiler();
		
		PluginPlayer pluginPlayer = playerDataFiler.loadFromMemoryOrDiskLogging(player);
		
		if(pluginPlayer != null)
		{
			pluginPlayer = (PluginPlayer) plugin.getPlayerManager().register(player, pluginPlayer);
			return;
		}
		
		pluginPlayer = (PluginPlayer) plugin.getPlayerManager().register(player, plugin, PhysicalPlayerType.SPAWN_PLAYER);
		pluginPlayer.setTag(pluginPlayer.getBestTag());
		player.teleport(PluginWorldType.RAID_WORLD.getInstance().getSpawnLocation());
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerQuit(PlayerQuitEvent event)
	{
		plugin.getServer().getScheduler().runTask(plugin,
				() -> plugin.getPlayerManager().unregister(event.getPlayer()));
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerChangedWorld(PlayerChangedWorldEvent event)
	{
		CommonPlayer commonPlayer = plugin.getPlayerManager().get(event.getPlayer());
		CommonWorld commonWorld = commonPlayer.getCommonWorld();
		
		if(!commonWorld.getAcceptablePlayerTypes().contains(commonPlayer.getPlayerType()))
		{
			plugin.getLogger().warning(MessageFormat.format("type={0} from={1} to={2} registering={3}",
					commonPlayer.getPlayerType(),
					event.getFrom().getName(),
					commonWorld.getWorld().getName(),
					commonWorld.getDefaultPlayerType()));//TODO remove
			plugin.getPlayerManager().register(commonPlayer, commonWorld.getDefaultPlayerType());
		}
	}
}
