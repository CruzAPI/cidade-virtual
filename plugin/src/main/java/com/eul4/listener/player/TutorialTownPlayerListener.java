package com.eul4.listener.player;

import com.eul4.Main;
import com.eul4.event.StructureInteractEvent;
import com.eul4.event.TileInteractEvent;
import com.eul4.model.player.TutorialTownPlayer;
import io.papermc.paper.event.player.AsyncChatEvent;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

@RequiredArgsConstructor
public class TutorialTownPlayerListener implements Listener
{
	private final Main plugin;
	
	@EventHandler
	public void cancelChat(AsyncChatEvent event)
	{
		event.viewers().removeIf(viewer -> viewer instanceof Player player
				&& plugin.getPlayerManager().get(player) instanceof TutorialTownPlayer);
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void cancelStructureInteraction(StructureInteractEvent event)
	{
		if(event.getStructure()
				.getTown()
				.findPluginPlayer()
				.filter(TutorialTownPlayer.class::isInstance)
				.isPresent())
		{
			event.setCancelled(true);
		}
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void cancelTileInteraction(TileInteractEvent event)
	{
		if(event.getTile()
				.getTown()
				.findPluginPlayer()
				.filter(TutorialTownPlayer.class::isInstance)
				.isPresent())
		{
			event.setCancelled(true);
		}
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void cancelCommands(PlayerCommandPreprocessEvent event)
	{
		if(plugin.getPlayerManager().get(event.getPlayer()) instanceof TutorialTownPlayer)
		{
			event.setCancelled(true);
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void cancelTeleports(PlayerTeleportEvent event)
	{
		if(plugin.getPlayerManager().get(event.getPlayer()) instanceof TutorialTownPlayer
				&& event.getCause() != PlayerTeleportEvent.TeleportCause.PLUGIN)
		{
			event.setCancelled(true);
		}
	}
}
