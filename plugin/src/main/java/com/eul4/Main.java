package com.eul4;

import com.eul4.command.TownCommand;
import com.eul4.i18n.BundleBaseName;
import com.eul4.i18n.CommonBundleBaseName;
import com.eul4.i18n.PluginBundleBaseName;
import com.eul4.i18n.ResourceBundleHandler;
import com.eul4.model.player.TownPlayer;
import com.eul4.type.player.PlayerType;
import com.eul4.type.player.PluginPlayerType;

import java.util.Locale;
import java.util.ResourceBundle;

public class Main extends Common
{
	@Override
	public void onEnable()
	{
		super.onEnable();
		
		registerResourceBundles();
		
		getCommand("town").setExecutor(new TownCommand(this));
		getLogger().info("Plugin enabled.");
	}
	
	private void registerResourceBundles()
	{
		for(Locale locale : ResourceBundleHandler.SUPPORTED_LOCALES)
		{
			for(BundleBaseName bundleBaseName : PluginBundleBaseName.values())
			{
				ResourceBundleHandler.registerBundle(ResourceBundle.getBundle(bundleBaseName.getName(), locale));
			}
		}
	}
	
	@Override
	public void onDisable()
	{
		super.onDisable();
		
		getLogger().info("Plugin disabled.");
	}
	
	@Override
	public PlayerType<TownPlayer> getDefaultPlayerType()
	{
		return PluginPlayerType.TOWN_PLAYER;
	}
}
