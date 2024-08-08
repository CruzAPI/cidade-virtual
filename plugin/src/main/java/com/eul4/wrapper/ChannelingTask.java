package com.eul4.wrapper;

import com.eul4.common.i18n.Message;
import com.eul4.i18n.PluginMessage;
import com.eul4.model.player.Channeler;
import com.eul4.util.MessageUtil;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.scheduler.BukkitRunnable;

public class ChannelingTask extends BukkitRunnable
{
	@RequiredArgsConstructor
	public enum CancelReason
	{
		SNEAK(PluginMessage.CHANNELER_CANCEL_REASON_SNEAK),
		MOVE(PluginMessage.CHANNELER_CANCEL_REASON_MOVE),
		TAKE_DAMAGE(PluginMessage.CHANNELER_CANCEL_REASON_TAKE_DAMAGE),
		DEAL_DAMAGE(PluginMessage.CHANNELER_CANCEL_REASON_DEAL_DAMAGE),
		DEAD(PluginMessage.CHANNELER_CANCEL_REASON_DEAD),
		CHANGE_MODE(PluginMessage.CHANNELER_CANCEL_REASON_CHANGE_MODE),
		INTERACT(PluginMessage.CHANNELER_CANCEL_REASON_INTERACT),
		DROP_ITEM(PluginMessage.CHANNELER_CANCEL_REASON_DROP_ITEM),
		PICKUP_ITEM(PluginMessage.CHANNELER_CANCEL_REASON_PICKUP_ITEM),
		OPEN_INVENTORY(PluginMessage.CHANNELER_CANCEL_REASON_OPEN_INVENTORY),
		CHANNELING(PluginMessage.CHANNELER_CANCEL_REASON_CHANNELING),
		VEHICLE(PluginMessage.CHANNELER_CANCEL_REASON_VEHICLE),
		PASSENGERS(PluginMessage.CHANNELER_CANCEL_REASON_PASSENGERS),
		;
		
		private final Message message;
	}
	
	private final Channeler channeler;
	private final long maxTicks;
	private final Runnable runnable;
	
	private final Location channelingLocation;
	
	private long ticks;
	
	public ChannelingTask(Channeler channeler, long maxTicks, Runnable runnable)
	{
		this.channeler = channeler;
		this.maxTicks = maxTicks;
		this.runnable = runnable;
		
		channelingLocation = channeler.getPlayer().getLocation();
		
		ticks = maxTicks;
	}
	
	@Override
	public void run()
	{
		if(cancelInAnyInvalidPrecondition())
		{
			return;
		}
		
		if(ticks < 0L)
		{
			complete();
			return;
		}
		
		updateActionBar();
		
		ticks--;
	}
	
	private boolean cancelInAnyInvalidPrecondition()
	{
		if(!channeler.isValid())
		{
			cancel(CancelReason.CHANGE_MODE);
			return true;
		}
		
		Player player = channeler.getPlayer();
		
		if(player.isInsideVehicle())
		{
			cancel(CancelReason.VEHICLE);
			return true;
		}
		
		if(!player.getPassengers().isEmpty())
		{
			cancel(CancelReason.PASSENGERS);
			return true;
		}
		
		if(player.isSneaking())
		{
			cancel(CancelReason.SNEAK);
			return true;
		}
		
		if(hasMoved())
		{
			cancel(CancelReason.MOVE);
			return true;
		}
		
		if(player.isDead())
		{
			cancel(CancelReason.DEAD);
			return true;
		}
		
		if(player.getOpenInventory().getType() != InventoryType.CRAFTING)
		{
			cancel(CancelReason.OPEN_INVENTORY);
			return true;
		}
		
		return false;
	}
	
	@Override
	public synchronized void cancel() throws IllegalStateException
	{
		super.cancel();
		channeler.getPlayer().sendActionBar(Component.empty());
	}
	
	public void cancel(CancelReason reason)
	{
		if(isCancelled())
		{
			return;
		}
		
		cancel();
		channeler.sendMessage(PluginMessage.CHANNELER_CHANNELING_CANCELLED);
		channeler.sendMessage(reason.message);
	}
	
	private void updateActionBar()
	{
		final int percentage = (int) Math.ceil((float) ticks / maxTicks * 100);
		channeler.getPlayer().sendActionBar(MessageUtil.getDecrescentPercentageProgressBar(percentage));
	}
	
	private void complete()
	{
		if(isCancelled())
		{
			return;
		}
		
		cancel();
		runnable.run();
	}
	
	private boolean hasMoved()
	{
		return !channelingLocation.equals(channeler.getPlayer().getLocation());
	}
}
