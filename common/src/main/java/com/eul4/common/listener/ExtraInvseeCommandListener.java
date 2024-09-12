package com.eul4.common.listener;

import com.eul4.common.Common;
import com.eul4.common.event.GuiClickEvent;
import com.eul4.common.event.GuiOpenEvent;
import com.eul4.common.model.inventory.ExtraInventory;
import com.eul4.common.model.player.CommonPlayer;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.scheduler.BukkitRunnable;

@RequiredArgsConstructor
public class ExtraInvseeCommandListener implements Listener
{
	private final Common plugin;
	
	@EventHandler
	public void onExtraInventoryOpem(GuiOpenEvent event)
	{
		if(event.getGui() instanceof ExtraInventory extraInventory)
		{
			scheduleTask(extraInventory);
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onDrag(InventoryDragEvent event)
	{
		Player player = (Player) event.getWhoClicked();
		CommonPlayer commonPlayer = plugin.getPlayerManager().get(player);
		
		if(commonPlayer.getGui() instanceof ExtraInventory extraInventory)
		{
			event.setCancelled(true);
		}
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onGuiClick(GuiClickEvent event)
	{
		if(event.getGui() instanceof ExtraInventory extraInventory)
		{
			plugin.getServer().getScheduler().runTask(plugin, extraInventory::applyToPlayer);
		}
	}
	
	private void scheduleTask(ExtraInventory extraInventory)
	{
		new Task(extraInventory.getCommonPlayer(), extraInventory).runTaskTimer(plugin, 0L, 1L);
	}
	
	@AllArgsConstructor
	private class Task extends BukkitRunnable
	{
		private CommonPlayer viewer;
		private ExtraInventory extraInventory;
		
		@Override
		public void run()
		{
			if(!viewer.isValid())
			{
				if((viewer = plugin.getPlayerManager().get(viewer.getUniqueId())) == null)
				{
					cancel();
					return;
				}
			}
			
			if(viewer.getGui() != extraInventory)
			{
				cancel();
				return;
			}
			
			if(!extraInventory.getTarget().isOnline())
			{
				cancel();
				return;
			}
			
			extraInventory.updateInventory();
		}
		
		@Override
		public synchronized void cancel() throws IllegalStateException
		{
			super.cancel();
			
			if(viewer != null && viewer.getGui() == extraInventory)
			{
				viewer.getPlayer().closeInventory();
			}
		}
	}
}
