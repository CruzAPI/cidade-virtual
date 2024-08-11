package com.eul4.common.service;

import com.eul4.common.Common;
import lombok.RequiredArgsConstructor;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@RequiredArgsConstructor
public class CommonDataFileManager
{
	private final Common plugin;
	
	public File getServerTickFile()
	{
		return new File(plugin.getDataFolder(), "server_tick.yml");
	}
	
	public File getGroupsFile()
	{
		return new File(plugin.getDataFolder() + "/permission", "groups.dat");
	}
	
	public File getUserFile(UUID uuid)
	{
		return new File(plugin.getDataFolder() + "/permission/users", uuid.toString() + ".dat");
	}
	
	public File createServerTickFileIfNotExists() throws IOException
	{
		return createFileIfNotExists(getServerTickFile());
	}
	
	public File createGroupsFileIfNotExists() throws IOException
	{
		return createFileIfNotExists(getGroupsFile());
	}
	
	public File createUserFileIfNotExists(UUID uuid) throws IOException
	{
		return createFileIfNotExists(getUserFile(uuid));
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
