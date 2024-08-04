package com.eul4.common.listener.container;

import com.eul4.common.Common;
import com.eul4.common.event.CommonPlayerRegisterEvent;
import com.eul4.common.util.ContainerUtil;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import static com.eul4.common.constant.CommonNamespacedKey.REMOVE_ITEM_ON_COMMON_PLAYER_REGISTER;

@RequiredArgsConstructor
public class RemoveItemOnCommonPlayerRegisterListener implements Listener
{
	private final Common plugin;
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void removeItemOnCommonPlayerRegister(CommonPlayerRegisterEvent event)
	{
		Player player = event.getCommonPlayer().getPlayer();
		ItemStack[] contents = player.getInventory().getContents();
		
		for(int i = 0; i < contents.length; i++)
		{
			ItemStack content = contents[i];
			
			if(ContainerUtil.hasFlag(content, REMOVE_ITEM_ON_COMMON_PLAYER_REGISTER))
			{
				player.getInventory().setItem(i, null);
			}
		}
	}
}
