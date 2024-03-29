package com.eul4.service;

import com.eul4.common.Common;
import lombok.RequiredArgsConstructor;
import org.bukkit.Chunk;

import java.io.File;
import java.io.IOException;

@RequiredArgsConstructor
public class DataFileManager
{
	private final Common plugin;
	
	public File getBlockDataFile(Chunk chunk)
	{
		return new File(plugin.getDataFolder() + "/block_data/" + chunk.getWorld().getName(),
				"c." + chunk.getX() + "." + chunk.getZ() + ".dat");
	}
	
	public File createBlockDataFileIfNotExists(Chunk chunk) throws IOException
	{
		final File file = getBlockDataFile(chunk);
		
		if(!file.exists())
		{
			file.toPath().getParent().toFile().mkdirs();
			file.createNewFile();
		}
		
		return file;
	}
}
