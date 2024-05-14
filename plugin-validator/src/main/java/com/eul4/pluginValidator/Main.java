package com.eul4.pluginValidator;

import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Optional;

public class Main extends JavaPlugin
{
	private static final String[] PLUGIN_NAMES = new String[]
	{
		"Plugin",
	};
	
	@Override
	public void onEnable()
	{
		for(String pluginName : PLUGIN_NAMES)
		{
			boolean isEnabled = Optional
					.ofNullable(getServer().getPluginManager().getPlugin(pluginName))
					.map(Plugin::isEnabled)
					.orElse(false);
			
			if(!isEnabled)
			{
				getLogger().severe("Plugin " + pluginName + " was not enable. Server will shut down...");
				getServer().shutdown();
				return;
			}
		}
	}
}