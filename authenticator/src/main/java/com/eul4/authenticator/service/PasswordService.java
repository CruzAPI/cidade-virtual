package com.eul4.authenticator.service;

import com.eul4.authenticator.Main;
import lombok.RequiredArgsConstructor;
import org.mindrot.jbcrypt.BCrypt;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Objects;
import java.util.UUID;

@RequiredArgsConstructor
public class PasswordService
{
	private final Main plugin;
	
	public boolean validatePassword(UUID uuid, String plainPassword) throws FileNotFoundException, IOException
	{
		Objects.requireNonNull(plainPassword);
		String hashedPassword = plugin.getPasswordFiler().loadHashedPassword(uuid);
		return BCrypt.checkpw(plainPassword, hashedPassword);
	}
	
	public boolean isRegistered(UUID uuid) throws IOException
	{
		try
		{
			return plugin.getPasswordFiler().loadHashedPassword(uuid) != null;
		}
		catch(FileNotFoundException e)
		{
			return false;
		}
	}
	
	public String hashPassword(String plainPassword)
	{
		return BCrypt.hashpw(plainPassword, BCrypt.gensalt());
	}
}
