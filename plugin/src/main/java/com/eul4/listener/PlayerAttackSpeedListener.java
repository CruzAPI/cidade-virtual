package com.eul4.listener;

import com.eul4.Main;
import com.eul4.common.util.ItemStackUtil;
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
			resetPlayerAttackSpeed(event.getPlayer());
		}
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void on(PlayerItemHeldEvent event)
	{
		Player player = event.getPlayer();
		ItemStack newItem = player.getInventory().getItem(event.getNewSlot());
		resetPlayerAttackSpeed(event.getPlayer(), newItem);
	}
	
	private void resetPlayerAttackSpeed(Player player)
	{
		resetPlayerAttackSpeed(player, player.getInventory().getItemInMainHand());
	}
	
	private void resetPlayerAttackSpeed(Player player, ItemStack itemInMainHand)
	{
		boolean itemInMainHandHasAttackSpeed = ItemStackUtil.hasAttackSpeed(itemInMainHand);
		
		Optional.ofNullable(player.getAttribute(Attribute.GENERIC_ATTACK_SPEED))
				.ifPresent(attribute -> attribute.setBaseValue(itemInMainHandHasAttackSpeed
						? attribute.getDefaultValue()
						: MAX_ATTACK_SPEED));
	}
}
