package com.eul4.common.listener.container;

import com.eul4.common.Common;
import com.eul4.common.constant.CommonNamespacedKey;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import static com.eul4.common.constant.CommonNamespacedKey.REMOVE_ITEM_ON_PLAYER_JOIN;
import static com.eul4.common.constant.CommonNamespacedKey.REMOVE_ON_CHUNK_LOAD;
import static org.bukkit.persistence.PersistentDataType.BOOLEAN;

@RequiredArgsConstructor
public class RemoveItemOnPlayerJoinListener implements Listener
{
	private final Common plugin;
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void removeItemOnPlayerJoin(PlayerJoinEvent event)
	{
		Player player = event.getPlayer();
		ItemStack[] contents = player.getInventory().getContents();
		
		for(int i = 0; i < contents.length; i++)
		{
			ItemStack content = contents[i];
			
			if(hasRemoveOnPlayerJoinFlag(content))
			{
				player.getInventory().setItem(i, null);
			}
		}
	}
	
	private boolean hasRemoveOnPlayerJoinFlag(ItemStack itemStack)
	{
		if(itemStack == null)
		{
			return false;
		}
		
		ItemMeta meta = itemStack.getItemMeta();
		
		if(meta == null)
		{
			return false;
		}
		
		return meta.getPersistentDataContainer().getOrDefault(REMOVE_ITEM_ON_PLAYER_JOIN, BOOLEAN, false);
	}
}
