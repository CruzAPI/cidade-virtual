package com.eul4.listener;

import com.eul4.Main;
import com.eul4.event.AssistantInteractEvent;
import com.eul4.model.inventory.craft.CraftAssistantGui;
import com.eul4.model.player.TownPlayer;
import com.eul4.model.town.Town;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;

@RequiredArgsConstructor
public class AssistantInteractListener implements Listener
{
	private final Main plugin;
	
	@EventHandler
	public void onAssistantInteraction(PlayerInteractEntityEvent event)
	{
		Player player = event.getPlayer();
		
		if(!(plugin.getPlayerManager().get(player) instanceof TownPlayer townPlayer))
		{
			return;
		}
		
		Town town = townPlayer.getTown();
		Villager assistant = town == null ? null : town.getAssistant();
		
		if(event.getRightClicked() != assistant)
		{
			return;
		}
		
		AssistantInteractEvent assistantInteractEvent = new AssistantInteractEvent(townPlayer, town);
		plugin.getPluginManager().callEvent(assistantInteractEvent);
		
		if(assistantInteractEvent.isCancelled())
		{
			return;
		}
		
		townPlayer.openGui(new CraftAssistantGui(townPlayer));
	}
}
