package com.eul4.common.listener;

import com.eul4.common.Common;
import com.eul4.common.constant.CommonNamespacedKey;
import com.eul4.common.event.WorldSaveOrStopEvent;
import lombok.RequiredArgsConstructor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.world.WorldSaveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

@RequiredArgsConstructor
public class WorldSaveRecallListener implements Listener
{
	private final Common plugin;
	
	@EventHandler
	public void onWorldSave(WorldSaveEvent event)
	{
		plugin.getServer().getPluginManager().callEvent(new WorldSaveOrStopEvent(event.getWorld()));
	}
}
