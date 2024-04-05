package com.eul4.listener;

import com.eul4.Main;
import com.eul4.common.event.GuiCloseEvent;
import com.eul4.common.event.GuiOpenEvent;
import com.eul4.common.model.inventory.UpdatableGui;
import lombok.RequiredArgsConstructor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

@RequiredArgsConstructor
public class InventoryUpdateListener implements Listener
{
	private final Main plugin;
	
	@EventHandler
	public void onGuiOpen(GuiOpenEvent event)
	{
		if(event.getGui() instanceof UpdatableGui updatableGui)
		{
			updatableGui.scheduleUpdate();
		}
	}
	
	@EventHandler
	public void onGuiClose(GuiCloseEvent event)
	{
		if(event.getGui() instanceof UpdatableGui updatableGui)
		{
			updatableGui.cancelScheduleUpdate();
		}
	}
}
