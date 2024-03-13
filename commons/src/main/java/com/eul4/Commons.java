package com.eul4;

import org.bukkit.plugin.java.JavaPlugin;

public class Commons extends JavaPlugin
{
	@Override
	public void onEnable()
	{
		getLogger().info("Commons enabled!");
	}

	@Override
	public void onDisable()
	{
		getLogger().info("Commons disabled!");
	}
}