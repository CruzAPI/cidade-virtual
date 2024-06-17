package com.eul4.common.service;

import com.eul4.common.Common;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.function.Consumer;
import java.util.logging.Level;

public final class ServerTickCounter
{
	private static final String SERVER_TICK = "server-tick";
	
	private final Common plugin;
	private final File file;
	private final YamlConfiguration config;
	private final long lastTotalTick;
	
	public ServerTickCounter(Common plugin) throws IOException
	{
		this.plugin = plugin;
		this.file = getPlugin().getCommonDataFileManager().createServerTickFileIfNotExists();
		this.config = YamlConfiguration.loadConfiguration(file);
		this.lastTotalTick = config.getLong(SERVER_TICK, 0L);
	}
	
	public void saveOrLogError()
	{
		trySaveOrCatch(e -> plugin.getLogger().log(Level.SEVERE,
				"Failed to save last tick on disable. totalTick=" + getTotalTick(), e));
	}
	
	private void trySaveOrCatch(Consumer<IOException> consumer)
	{
		try
		{
			save();
		}
		catch(IOException e)
		{
			consumer.accept(e);
		}
	}
	
	private void save() throws IOException
	{
		config.set(SERVER_TICK, getTotalTick());
		config.save(file);
	}
	
	public long getTotalTick()
	{
		return lastTotalTick + plugin.getServer().getCurrentTick();
	}
	
	private Common getPlugin()
	{
		return plugin;
	}
}
