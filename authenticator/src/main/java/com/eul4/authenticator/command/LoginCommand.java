package com.eul4.authenticator.command;

import com.eul4.authenticator.Main;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.Collections;
import java.util.logging.Level;

import static com.eul4.authenticator.util.MessageUtil.CONTACT_SERVER_ADMINISTRATOR_MESSAGE;
import static com.eul4.authenticator.util.MessageUtil.UNEXPECTED_ERROR_MESSAGE;
import static net.md_5.bungee.api.ChatColor.GREEN;
import static net.md_5.bungee.api.ChatColor.RED;

public class LoginCommand extends Command implements TabExecutor
{
	private static final String USAGE = "Uso: /login <senha>";
	
	private static final String NOT_REGISTERED = "Você não está registrado!";
	private static final String USE_REGISTER_FIRST = "Para se registrar use: /register <senha> [senha]";
	
	private static final String INVALID_PASSWORD = "Senha inválida!";
	
	private static final String LOGGED_SUCCESSFULLY = "Você foi autenticado com sucesso!";
	
	private final Main plugin;
	
	public LoginCommand(Main plugin)
	{
		super("login");
		
		this.plugin = plugin;
	}
	
	@Override
	public Iterable<String> onTabComplete(CommandSender sender, String[] args)
	{
		return Collections.emptyList();
	}
	
	@Override
	public void execute(CommandSender sender, String[] args)
	{
		if(!(sender instanceof ProxiedPlayer player))
		{
			return;
		}
		
		if(!player.getServer().getInfo().getName().equals("limbo"))
		{
			return;
		}
		
		if(args.length == 1)
		{
			final String plainPassword = args[0];
			
			try
			{
				boolean validPassword = plugin.getPasswordService().validatePassword(player.getUniqueId(), plainPassword);
				
				if(validPassword)
				{
					TextComponent textComponent = new TextComponent(LOGGED_SUCCESSFULLY);
					textComponent.setColor(GREEN);
					sender.sendMessage(textComponent);
					
					player.connect(plugin.getProxy().getServerInfo("cidade-virtual"));
				}
				else
				{
					TextComponent textComponent = new TextComponent(INVALID_PASSWORD);
					textComponent.setColor(RED);
					sender.sendMessage(textComponent);
				}
			}
			catch(FileNotFoundException e)
			{
				TextComponent[] textComponents = new TextComponent[2];
				
				textComponents[0] = new TextComponent(NOT_REGISTERED);
				textComponents[1] = new TextComponent(USE_REGISTER_FIRST);
				
				textComponents[0].setColor(RED);
				textComponents[1].setColor(RED);
				
				sender.sendMessage(textComponents[0]);
				sender.sendMessage(textComponents[1]);
			}
			catch(IOException e)
			{
				plugin.getLogger().log(Level.SEVERE, MessageFormat.format(
						"Failed to load {0} password! uuid={1}",
						player.getName(),
						player.getUniqueId()), e);
				
				TextComponent[] textComponents = new TextComponent[2];
				
				textComponents[0] = new TextComponent(UNEXPECTED_ERROR_MESSAGE);
				textComponents[1] = new TextComponent(CONTACT_SERVER_ADMINISTRATOR_MESSAGE);
				
				textComponents[0].setColor(RED);
				textComponents[1].setColor(RED);
				
				sender.sendMessage(textComponents[0]);
				sender.sendMessage(textComponents[1]);
			}
		}
		else
		{
			TextComponent textComponent = new TextComponent(USAGE);
			textComponent.setColor(RED);
			sender.sendMessage(textComponent);
		}
	}
}
