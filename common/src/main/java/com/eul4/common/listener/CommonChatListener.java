package com.eul4.common.listener;

import com.eul4.common.Common;
import com.eul4.common.i18n.CommonMessage;
import com.eul4.common.model.player.CommonPlayer;
import io.papermc.paper.event.player.AsyncChatEvent;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

@RequiredArgsConstructor
public class CommonChatListener implements Listener
{
	public static final String PERMISSION_CHAT_BYPASS = "chat.bypass";
	
	private final Common plugin;
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void cancelIfGlobalChatIsDisabled(AsyncChatEvent event)
	{
		CommonPlayer commonPlayer = plugin.getPlayerManager().get(event.getPlayer());
		
		if(!plugin.getChatCommand().isEnabled() && !commonPlayer.hasPermission(PERMISSION_CHAT_BYPASS))
		{
			commonPlayer.sendMessage(CommonMessage.COMMAND_CHAT_CHAT_IS_DISABLED);
			event.setCancelled(true);
		}
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void cancelIfSenderChatIsDisabled(AsyncChatEvent event)
	{
		CommonPlayer commonPlayer = plugin.getPlayerManager().get(event.getPlayer());
		
		if(!commonPlayer.isChatEnabled())
		{
			commonPlayer.sendMessage(CommonMessage.COMMAND_DISABLE_CHAT_CHAT_IS_DISABLED);
			event.setCancelled(true);
		}
	}
	
	@EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
	public void filterAudience(AsyncChatEvent event)
	{
		Player playerSender = event.getPlayer();
		CommonPlayer commonPlayerSender = plugin.getPlayerManager().get(playerSender);
		
		if(commonPlayerSender.hasPermission(PERMISSION_CHAT_BYPASS))
		{
			return;
		}
		
		event.viewers().removeIf(audience -> audience instanceof Player player
				&& plugin.getPlayerManager().get(player) instanceof CommonPlayer commonPlayer
				&& (commonPlayer.hasIgnored(playerSender) || !commonPlayer.isChatEnabled()));
	}
}
