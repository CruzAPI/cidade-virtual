package com.eul4.util;

import org.bukkit.plugin.Plugin;

public class ThreadUtil
{
	public static void runSynchronously(Plugin plugin, Runnable runnable)
	{
		if(plugin.getServer().isPrimaryThread())
		{
			runnable.run();
		}
		else
		{
			plugin.getServer().getScheduler().runTask(plugin, runnable);
		}
	}
}
