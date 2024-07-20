package com.eul4.authenticator.listener;

import com.eul4.authenticator.Main;
import com.eul4.authenticator.util.EventPriority;
import com.eul4.authenticator.util.MessageUtil;
import com.eul4.authenticator.util.PlayerUtil;
import lombok.RequiredArgsConstructor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.ServerConnectEvent;
import net.md_5.bungee.api.event.ServerSwitchEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.scheduler.ScheduledTask;
import net.md_5.bungee.event.EventHandler;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

@RequiredArgsConstructor
public class LoginListener implements Listener
{
	private static final Random RANDOM = new Random();
	
	private static final String[] LOGIN_SENTENCE = new String[]
	{
		"§7Faça o login para voltar a Cidade Virtual!",
		"§7Você precisa fazer o login para voltar ao servidor!",
		"§7Para a segurança da sua cidade você deve realizar o login.",
		"§7Precisamos saber se é você mesmo...",
		"§7Precisamos saber se não é o Kevin tentando invadir sua conta.",
		"§7Por favor, realize o login para entrar no servidor.",
	};
	
	private static final String[] WELCOME_BACK = new String[]
	{
		"§5Que bom te ver denovo, §d{0}§d!",
		"§2Boas vindas de volta, §a{0}§2!",
		"§7Olá, §f{0}§7! Que bom te ver por aqui.",
		"§6Oi, §e{0}§6. Você vem sempre aqui?",
		"§bQuanto tempo, §3{0}§b!"
	};
	
	private static final String
	LOGIN_HINT = "§8Para logar, use o comando:§7 /login <senha>",
	REGISTER_HINT = "§8Para se registrar, use o comando:§7 /register <senha> [senha]",
	
	WELCOME = """
			§6Seja bem-vindo ao servidor da §f§lCIDADE VIRTUAL§6, {0}!
   
			§7Aqui você terá a experiência de estar na §fcidade virtual§7
			dentro do §oMinecraft§7, com um modo de jogo exclusivo e
			tematizado na §fcidade virtual§7 e muito mais ainda por vir!
			
			§7Mas antes você deve se registrar para entrar no servidor.
			§7Use uma senha §fforte§7, mas cuidado para não se §fesquecer§7 dela!""";
	
	private final Main plugin;
	
	private final Map<UUID, ScheduledTask> tasks = new HashMap<>();
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onLimboDisconnect(ServerSwitchEvent event)
	{
		ServerInfo from = event.getFrom();
		
		if(from != null && from.getName().equals("limbo"))
		{
			cancelTaskIfRunning(event.getPlayer());
		}
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onProxyDisconnect(PlayerDisconnectEvent event)
	{
		cancelTaskIfRunning(event.getPlayer());
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onLimboConnectMonitor(ServerConnectEvent event)
	{
		if(!event.getTarget().getName().equals("limbo"))
		{
			return;
		}
		
		ProxiedPlayer player = event.getPlayer();
		
		try
		{
			boolean isRegistered = plugin.getPasswordService().isRegistered(player.getUniqueId());
			
			if(isRegistered)
			{
				player.sendMessage(getRandomWelcomeBackMessage(player));
				player.sendMessage(getRandomLoginSentence());
			}
			else
			{
				player.sendMessage(TextComponent.fromLegacyText(MessageFormat.format(WELCOME, player.getName())));
			}
			
			scheduleTask(player, isRegistered);
		}
		catch(IOException e)
		{
			plugin.getLogger().log(Level.SEVERE, MessageFormat.format(
					"Failed to check if player {0} is registered on limbo connect! uuid={!}",
					player.getName(),
					player.getUniqueId()), e);
			
			MessageUtil.sendUnexpectedErrorMessage(player);
		}
	}
	
	@EventHandler
	public void onServerConnect(ServerConnectEvent event)
	{
		ProxiedPlayer player = event.getPlayer();
		
		ServerInfo target = event.getTarget();
		
		if(target.getName().equals("limbo") && PlayerUtil.isPremium(player))
		{
			event.setTarget(plugin.getProxy().getServerInfo("cidade-virtual"));
		}
	}
	
	private static BaseComponent[] getRandomLoginSentence()
	{
		String randomMessage = LOGIN_SENTENCE[RANDOM.nextInt(LOGIN_SENTENCE.length)];
		return TextComponent.fromLegacyText(randomMessage);
	}
	
	private static BaseComponent[] getRandomWelcomeBackMessage(ProxiedPlayer player)
	{
		String randomMessage = WELCOME_BACK[RANDOM.nextInt(WELCOME_BACK.length)];
		return TextComponent.fromLegacyText(MessageFormat.format(randomMessage, player.getName()));
	}
	
	private void scheduleTask(ProxiedPlayer player, boolean isRegistered)
	{
		cancelTaskIfRunning(player);
		
		ScheduledTask scheduledTask = plugin.getProxy().getScheduler().schedule(plugin,
				() -> player.sendMessage(TextComponent.fromLegacyText(isRegistered ? LOGIN_HINT : REGISTER_HINT)),
				7L, 20L, TimeUnit.SECONDS);
		
		tasks.put(player.getUniqueId(), scheduledTask);
	}
	
	private void cancelTaskIfRunning(ProxiedPlayer player)
	{
		cancelTaskIfRunning(player.getUniqueId());
	}
	
	private void cancelTaskIfRunning(UUID uuid)
	{
		Optional.ofNullable(tasks.remove(uuid)).ifPresent(ScheduledTask::cancel);
	}
}
