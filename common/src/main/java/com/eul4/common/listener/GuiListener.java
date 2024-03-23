package com.eul4.common.listener;

import com.eul4.common.Common;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;

@RequiredArgsConstructor
public class GuiListener implements Listener
{
	private final Common plugin;
	
	@EventHandler
	public void onInventoryClose(InventoryCloseEvent event)
	{
		if(event.getPlayer() instanceof Player player)
		{
			plugin.getPlayerManager().get(player).nullifyGui();
		}
	}
}
