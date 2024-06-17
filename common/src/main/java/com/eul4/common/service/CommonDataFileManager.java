package com.eul4.common.service;

import com.eul4.common.Common;
import lombok.RequiredArgsConstructor;

import java.io.File;
import java.io.IOException;

@RequiredArgsConstructor
public class CommonDataFileManager
{
	private final Common plugin;
	
	public File getServerTickFile()
	{
		return new File(plugin.getDataFolder(), "server_tick.yml");
	}
	
	public File createServerTickFileIfNotExists() throws IOException
	{
		return createFileIfNotExists(getServerTickFile());
	}
	
	private File createFileIfNotExists(File file) throws IOException
	{
		if(!file.exists())
		{
			file.toPath().getParent().toFile().mkdirs();
			file.createNewFile();
		}
		
		return file;
	}
}
