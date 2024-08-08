package com.eul4.listener;

import com.eul4.Main;
import com.eul4.common.event.CommonPlayerRegisterEvent;
import com.eul4.common.model.player.CommonPlayer;
import com.eul4.model.player.Channeler;
import com.eul4.wrapper.ChannelingTask;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.player.*;

import java.util.Optional;

import static com.eul4.wrapper.ChannelingTask.CancelReason.*;

@RequiredArgsConstructor
public class ChannelingTaskListener implements Listener
{
	private final Main plugin;
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onToggleSneak(PlayerToggleSneakEvent event)
	{
		cancelChannelingTask(event, SNEAK);
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onTakeDamage(EntityDamageEvent event)
	{
		cancelChannelingTask(event, TAKE_DAMAGE);
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onDealDamage(EntityDamageByEntityEvent event)
	{
		if(event.getDamager() instanceof Player player)
		{
			cancelChannelingTask(player, DEAL_DAMAGE);
		}
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onDealDamageRanged(EntityDamageByEntityEvent event)
	{
		if(event.getDamager() instanceof Projectile projectile
				&& projectile.getShooter() instanceof Player player)
		{
			cancelChannelingTask(player, DEAL_DAMAGE);
		}
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onChangeMode(PlayerGameModeChangeEvent event)
	{
		cancelChannelingTask(event, CHANGE_MODE);
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onChangeMode(CommonPlayerRegisterEvent event)
	{
		cancelChannelingTask(event.getCommonPlayer(), CHANGE_MODE);
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onInteract(PlayerInteractEvent event)
	{
		cancelChannelingTask(event, INTERACT);
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onInteract(PlayerInteractEntityEvent event)
	{
		cancelChannelingTask(event, INTERACT);
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onDropItem(PlayerDropItemEvent event)
	{
		cancelChannelingTask(event, DROP_ITEM);
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onPickupItem(EntityPickupItemEvent event)
	{
		cancelChannelingTask(event, PICKUP_ITEM);
	}
	
	private void cancelChannelingTask(PlayerEvent event, ChannelingTask.CancelReason reason)
	{
		cancelChannelingTask(event.getPlayer(), reason);
	}
	
	private void cancelChannelingTask(EntityEvent event, ChannelingTask.CancelReason reason)
	{
		if(event.getEntity() instanceof Player player)
		{
			cancelChannelingTask(player, reason);
		}
	}
	
	private void cancelChannelingTask(Player player, ChannelingTask.CancelReason reason)
	{
		cancelChannelingTask(plugin.getPlayerManager().get(player), reason);
	}
	
	private void cancelChannelingTask(CommonPlayer commonPlayer, ChannelingTask.CancelReason reason)
	{
		Optional.of(commonPlayer)
				.filter(Channeler.class::isInstance)
				.map(Channeler.class::cast)
				.map(Channeler::getChannelingTask)
				.ifPresent(channelingTask -> channelingTask.cancel(reason));
	}
}
