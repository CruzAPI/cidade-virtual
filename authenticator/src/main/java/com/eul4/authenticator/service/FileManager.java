package com.eul4.authenticator.service;

import com.eul4.authenticator.Main;
import lombok.RequiredArgsConstructor;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@RequiredArgsConstructor
public class FileManager
{
	private final Main plugin;
	
	public File getPasswordFile(UUID uuid)
	{
		return new File(plugin.getDataFolder() + "/passwords", uuid.toString() + ".dat");
	}
	
	public File createPasswordFileIfNotExists(UUID uuid) throws IOException
	{
		return createFileIfNotExists(getPasswordFile(uuid));
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
