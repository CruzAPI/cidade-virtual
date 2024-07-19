package com.eul4.authenticator.listener;

import com.eul4.authenticator.Main;
import com.eul4.authenticator.util.PlayerUtil;
import lombok.RequiredArgsConstructor;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ServerConnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

@RequiredArgsConstructor
public class LoginListener implements Listener
{
	private final Main plugin;
	
	@EventHandler
	public void onLogin(ServerConnectEvent event)
	{
		ProxiedPlayer player = event.getPlayer();
		
		ServerInfo target = event.getTarget();
		
		if(target.getName().equals("limbo") && PlayerUtil.isPremium(player))
		{
			event.setTarget(plugin.getProxy().getServerInfo("cidade-virtual"));
		}
	}
}
