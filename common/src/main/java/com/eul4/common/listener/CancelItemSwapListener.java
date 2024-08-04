package com.eul4.common.listener;

import com.eul4.common.Common;
import lombok.RequiredArgsConstructor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;

import static com.eul4.common.constant.CommonNamespacedKey.CANCEL_SWAP;
import static com.eul4.common.util.ContainerUtil.hasFlag;

@RequiredArgsConstructor
public class CancelItemSwapListener implements Listener
{
	private final Common plugin;
	
	@EventHandler(ignoreCancelled = true)
	public void onItemSwap(PlayerSwapHandItemsEvent event)
	{
		if(hasFlag(event.getMainHandItem(), CANCEL_SWAP)
				|| hasFlag(event.getOffHandItem(), CANCEL_SWAP))
		{
			event.setCancelled(true);
		}
	}
}
