package com.eul4.authenticator;

import com.eul4.authenticator.command.LoginCommand;
import com.eul4.authenticator.command.RegisterCommand;
import com.eul4.authenticator.listener.LoginListener;
import com.eul4.authenticator.service.FileManager;
import com.eul4.authenticator.service.PasswordFiler;
import com.eul4.authenticator.service.PasswordService;
import lombok.Getter;
import net.md_5.bungee.api.plugin.Plugin;

@Getter
public class Main extends Plugin
{
	private FileManager fileManager;
	private PasswordFiler passwordFiler;
	private PasswordService passwordService;
	
	@Override
	public void onEnable()
	{
		registerServices();
		registerCommands();
		registerListeners();
	}
	
	private void registerServices()
	{
		passwordFiler = new PasswordFiler(this);
		passwordService = new PasswordService(this);
		fileManager = new FileManager(this);
	}
	
	private void registerCommands()
	{
		getProxy().getPluginManager().registerCommand(this, new RegisterCommand(this));
		getProxy().getPluginManager().registerCommand(this, new LoginCommand(this));
	}
	
	private void registerListeners()
	{
		getProxy().getPluginManager().registerListener(this, new LoginListener(this));
	}
}