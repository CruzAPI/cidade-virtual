package com.eul4.common.util;

import lombok.SneakyThrows;
import org.bukkit.plugin.Plugin;

import java.util.concurrent.Callable;

public class ThreadUtil
{
	private static Callable<Void> callable(Runnable runnable)
	{
		return () ->
		{
			runnable.run();
			return null;
		};
	}
	
	@SneakyThrows
	public static <T> T callSynchronouslyUntilTerminate(Plugin plugin, Callable<T> callable)
	{
		if(plugin.getServer().isPrimaryThread())
		{
			return callable.call();
		}
		else
		{
			return plugin.getServer().getScheduler().callSyncMethod(plugin, callable).get();
		}
	}
	
	@SneakyThrows
	public static void runSynchronouslyUntilTerminate(Plugin plugin, Runnable runnable)
	{
		Callable<Void> callable = callable(runnable);
		
		if(plugin.getServer().isPrimaryThread())
		{
			runnable.run();
		}
		else
		{
			plugin.getServer().getScheduler().callSyncMethod(plugin, callable).get();
		}
	}
	
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
