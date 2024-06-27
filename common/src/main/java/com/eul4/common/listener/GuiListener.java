package com.eul4.common.listener;

import com.eul4.common.Common;
import com.eul4.common.event.GuiCloseEvent;
import com.eul4.common.model.player.CommonPlayer;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
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
		//TODO calling twice on ArmorySelectOrStorageItems inventory gui
		if(!(event.getPlayer() instanceof Player player)
				|| !(plugin.getPlayerManager().get(player) instanceof CommonPlayer commonPlayer))
		{
			return;
		}
		
		plugin.getServer().getPluginManager().callEvent(new GuiCloseEvent(commonPlayer.getGui(), event));
		commonPlayer.nullifyGui();
	}
}
