package com.eul4.authenticator.service;

import com.eul4.authenticator.Main;
import lombok.RequiredArgsConstructor;

import java.io.*;
import java.util.UUID;

@RequiredArgsConstructor
public class PasswordFiler
{
	private final Main plugin;
	
	public void saveHashedPassword(UUID uuid, String hashedPassword) throws IOException
	{
		File file = plugin.getFileManager().createPasswordFileIfNotExists(uuid);
		
		try(FileOutputStream fileOutputStream = new FileOutputStream(file);
				DataOutputStream out = new DataOutputStream(fileOutputStream))
		{
			out.writeUTF(hashedPassword);
		}
	}
	
	public String loadHashedPassword(UUID uuid) throws FileNotFoundException, IOException
	{
		File file = plugin.getFileManager().getPasswordFile(uuid);
		
		try(FileInputStream fileInputStream = new FileInputStream(file);
				DataInputStream in = new DataInputStream(fileInputStream))
		{
			return in.readUTF();
		}
	}
}
