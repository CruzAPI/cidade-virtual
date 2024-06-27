package com.eul4.common.listener;

import com.eul4.common.Common;
import com.eul4.common.event.GuiClickEvent;
import com.eul4.common.event.WorldSaveOrStopEvent;
import com.eul4.common.model.inventory.Gui;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.world.WorldSaveEvent;

@RequiredArgsConstructor
public class GuiClickEventCallListener implements Listener
{
	private final Common plugin;
	
	@EventHandler
	public void on(InventoryClickEvent event)
	{
		if(!(event.getWhoClicked() instanceof Player player))
		{
			return;
		}
		
		//TODO replace all InventoryClickEvent to GuiClickEvent or add it in Paper patch
		
		Gui gui = plugin.getPlayerManager().get(player).getGui();
		new GuiClickEvent(event, gui).callEvent();
	}
}
