package com.eul4.externalizer.reader;

import com.eul4.Main;
import com.eul4.Versions;
import com.eul4.common.exception.InvalidVersionException;
import com.eul4.common.wrapper.Reader;
import com.eul4.model.craft.player.CraftAdmin;
import com.eul4.model.player.Admin;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.io.ObjectInput;

public class AdminReader extends PluginPlayerReader<Admin>
{
	private final Reader<Admin> reader;
	
	public AdminReader(ObjectInput in, Versions versions, Player player, Main plugin) throws InvalidVersionException
	{
		super(in, versions, plugin, () -> new CraftAdmin(player, plugin));
		
		if(versions.getAdminVersion() == 0)
		{
			this.reader = this::readerVersion0;
		}
		else
		{
			throw new InvalidVersionException("Invalid Admin version: " + versions.getAdminVersion());
		}
	}
	
	private Admin readerVersion0() throws IOException, ClassNotFoundException
	{
		Admin admin = getInstance();
		//TODO: ...?
		return admin;
	}
	
	@Override
	protected Admin readObject() throws IOException, ClassNotFoundException
	{
		super.readObject();
		
		return reader.readObject();
	}
}
