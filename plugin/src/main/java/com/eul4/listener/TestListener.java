package com.eul4.listener;

import com.eul4.Main;
import com.eul4.type.PluginWorldType;
import lombok.RequiredArgsConstructor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBurnEvent;

@RequiredArgsConstructor
public class TestListener implements Listener
{
	private final Main plugin;
	
	@EventHandler
	public void cancelBlockBurn(BlockBurnEvent event)
	{
		if(event.getBlock().getWorld() == PluginWorldType.CIDADE_VIRTUAL.getWorld())
		{
			event.setCancelled(true);
		}
	}
}
