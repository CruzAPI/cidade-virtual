package com.eul4.listener.player;

import com.eul4.Main;
import com.eul4.model.player.PluginPlayer;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

@RequiredArgsConstructor
public class PluginPlayerListener implements Listener
{
	private final Main plugin;
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void on(PlayerJoinEvent event)
	{
		Player player = event.getPlayer();
		
		if(!(plugin.getPlayerManager().get(player) instanceof PluginPlayer pluginPlayer))
		{
			return;
		}
		
		pluginPlayer.refreshTag();
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void on(PlayerItemHeldEvent event)
	{
		Player player = event.getPlayer();
		
		if(!(plugin.getPlayerManager().get(player) instanceof PluginPlayer pluginPlayer))
		{
			return;
		}
		
		ItemStack previousItem = player.getInventory().getItem(event.getPreviousSlot());
		ItemStack newItem = player.getInventory().getItem(event.getNewSlot());
		
		previousItem = previousItem == null ? ItemStack.empty() : previousItem;
		newItem = newItem == null ? ItemStack.empty() : newItem;
		
		if(previousItem.getType() != newItem.getType())
		{
			pluginPlayer.resetAttackCooldown();
		}
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void on(PlayerInteractEvent event)
	{
		Action action = event.getAction();
		Player player = event.getPlayer();
		EquipmentSlot hand = event.getHand();
		
		if((action != Action.LEFT_CLICK_AIR && action != Action.LEFT_CLICK_BLOCK)
				|| !(plugin.getPlayerManager().get(player) instanceof PluginPlayer pluginPlayer)
				|| hand != EquipmentSlot.HAND)
		{
			return;
		}
		
		pluginPlayer.resetAttackCooldown();
	}
}
