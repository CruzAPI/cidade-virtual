package com.eul4.listener;

import com.eul4.Main;
import com.eul4.common.util.ItemStackUtil;
import com.eul4.model.player.PluginPlayer;
import lombok.RequiredArgsConstructor;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Optional;

@RequiredArgsConstructor
public class PlayerAttackSpeedListener implements Listener
{
	public static final double MAX_ATTACK_SPEED = 16.0D;
	
	private final Main plugin;
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void on(PlayerInteractEvent event)
	{
		if(event.getAction().isLeftClick())
		{
			resetPlayerAttackSpeed((PluginPlayer) plugin.getPlayerManager().get(event.getPlayer()));
		}
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void on(PlayerItemHeldEvent event)
	{
		Player player = event.getPlayer();
		PluginPlayer pluginPlayer = (PluginPlayer) plugin.getPlayerManager().get(player);
		
		ItemStack newItem = player.getInventory().getItem(event.getNewSlot());
		resetPlayerAttackSpeed(pluginPlayer, newItem);
	}
	
	private void resetPlayerAttackSpeed(PluginPlayer pluginPlayer)
	{
		resetPlayerAttackSpeed(pluginPlayer, pluginPlayer.getPlayer().getInventory().getItemInMainHand());
	}
	
	private void resetPlayerAttackSpeed(PluginPlayer pluginPlayer, ItemStack itemInMainHand)
	{
		Player player = pluginPlayer.getPlayer();
		boolean itemInMainHandHasAttackSpeed = ItemStackUtil.hasAttackSpeed(itemInMainHand);
		
		Optional.ofNullable(player.getAttribute(Attribute.GENERIC_ATTACK_SPEED))
				.ifPresent(attribute -> attribute.setBaseValue
				(
					pluginPlayer.hasAttackSpeed() || itemInMainHandHasAttackSpeed
					? attribute.getDefaultValue()
					: MAX_ATTACK_SPEED
				));
	}
}
