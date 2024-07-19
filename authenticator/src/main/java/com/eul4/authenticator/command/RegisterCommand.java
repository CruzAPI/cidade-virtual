package com.eul4.authenticator.command;

import com.eul4.authenticator.Main;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.Collections;
import java.util.logging.Level;

import static com.eul4.authenticator.util.MessageUtil.CONTACT_SERVER_ADMINISTRATOR_MESSAGE;
import static com.eul4.authenticator.util.MessageUtil.UNEXPECTED_ERROR_MESSAGE;
import static net.md_5.bungee.api.ChatColor.GREEN;
import static net.md_5.bungee.api.ChatColor.RED;

public class RegisterCommand extends Command implements TabExecutor
{
	private static final String USAGE_MESSAGE = "Uso: /register <senha> [senha]";
	private static final String ALREADY_REGISTERED = "Você já está registrado!";
	private static final String USE_LOGIN_INSTEAD = "Para entrar use: /login <senha>";
	private static final String CONFIRMATION_PASSWORD_DID_NOT_MATCH = "As senhas não são iguais. Tente novamente.";
	
	private static final String MINIMUM_PASSWORD_LENGTH = "Sua senha deve ter no mínimo 8 caracteres.";
	private static final String ONLY_LETTERS_NUMBERS_AND_COMMON_PUNCTUATION = "Use apenas letras, números e caracteres de pontuação comuns.";
	
	private static final String CHOOSE_A_SAFER_PASSWORD = "Escolha uma senha mais segura.";
	private static final String USE_LETTERS_NUMBERS_AND_SYMBOLS_COMBINATION = "Use uma combinação de letras, números e símbolos.";
	
	private static final String REGISTERED_SUCCESSFULLY = "Você foi registrado com sucesso!";
	private static final String NEXT_TIME_USE_LOGIN = "A próxima vez que você entrar use: /login <senha>";
	
	private final Main plugin;
	
	public RegisterCommand(Main plugin)
	{
		super("register");
		
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
		
		if(args.length == 1 || args.length == 2)
		{
			try
			{
				if(plugin.getPasswordService().isRegistered(player.getUniqueId()))
				{
					TextComponent[] textComponents = new TextComponent[2];
					
					textComponents[0] = new TextComponent(ALREADY_REGISTERED);
					textComponents[1] = new TextComponent(USE_LOGIN_INSTEAD);
					
					textComponents[0].setColor(RED);
					textComponents[1].setColor(RED);
					
					sender.sendMessage(textComponents[0]);
					sender.sendMessage(textComponents[1]);
					
					return;
				}
			}
			catch(IOException e)
			{
				plugin.getLogger().log(Level.SEVERE, MessageFormat.format(
						"Failed to check if {0} is registered! uuid={1}",
						player.getName(),
						player.getUniqueId()), e);
				
				TextComponent[] textComponents = new TextComponent[2];
				
				textComponents[0] = new TextComponent(UNEXPECTED_ERROR_MESSAGE);
				textComponents[1] = new TextComponent(CONTACT_SERVER_ADMINISTRATOR_MESSAGE);
				
				textComponents[0].setColor(RED);
				textComponents[1].setColor(RED);
				
				sender.sendMessage(textComponents[0]);
				sender.sendMessage(textComponents[1]);
				
				return;
			}
			
			if(args.length == 2 && !args[0].equals(args[1]))
			{
				TextComponent textComponent = new TextComponent(CONFIRMATION_PASSWORD_DID_NOT_MATCH);
				textComponent.setColor(RED);
				sender.sendMessage(textComponent);
				return;
			}
			
			final String plainPassword = args[0];
			
			if(plainPassword.length() < 8)
			{
				TextComponent textComponent = new TextComponent(MINIMUM_PASSWORD_LENGTH);
				textComponent.setColor(RED);
				sender.sendMessage(textComponent);
				return;
			}
			
			if(!plainPassword.matches("[\\p{Punct}\\w]*"))
			{
				TextComponent textComponent = new TextComponent(ONLY_LETTERS_NUMBERS_AND_COMMON_PUNCTUATION);
				textComponent.setColor(RED);
				sender.sendMessage(textComponent);
				return;
			}
			
			if(!plainPassword.matches(".*\\p{Alpha}.*")
					|| !plainPassword.matches(".*\\p{Digit}.*")
					|| !plainPassword.matches(".*\\p{Punct}.*"))
			{
				TextComponent[] textComponents = new TextComponent[2];
				
				textComponents[0] = new TextComponent(CHOOSE_A_SAFER_PASSWORD);
				textComponents[1] = new TextComponent(USE_LETTERS_NUMBERS_AND_SYMBOLS_COMBINATION);
				
				textComponents[0].setColor(RED);
				textComponents[1].setColor(RED);
				
				sender.sendMessage(textComponents[0]);
				sender.sendMessage(textComponents[1]);
				return;
			}
			
			String hashedPassword = plugin.getPasswordService().hashPassword(plainPassword);
			
			try
			{
				plugin.getPasswordFiler().saveHashedPassword(player.getUniqueId(), hashedPassword);
				
				TextComponent[] textComponents = new TextComponent[2];
				
				textComponents[0] = new TextComponent(REGISTERED_SUCCESSFULLY);
				textComponents[1] = new TextComponent(NEXT_TIME_USE_LOGIN);
				
				textComponents[0].setColor(GREEN);
				textComponents[1].setColor(GREEN);
				
				sender.sendMessage(textComponents[0]);
				sender.sendMessage(textComponents[1]);
				
				player.connect(plugin.getProxy().getServerInfo("cidade-virtual"));
			}
			catch(IOException e)
			{
				plugin.getLogger().log(Level.SEVERE, MessageFormat.format(
						"Failed to register {0}! uuid={1}",
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
			TextComponent textComponent = new TextComponent(USAGE_MESSAGE);
			textComponent.setColor(RED);
			sender.sendMessage(textComponent);
		}
	}
}
