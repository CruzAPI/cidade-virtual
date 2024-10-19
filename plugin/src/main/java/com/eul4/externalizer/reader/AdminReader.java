package com.eul4.externalizer.reader;

import com.eul4.Main;
import com.eul4.common.Common;
import com.eul4.common.exception.InvalidVersionException;
import com.eul4.common.type.player.ObjectType;
import com.eul4.common.type.player.Readers;
import com.eul4.common.wrapper.BiParameterizedReadable;
import com.eul4.common.wrapper.Readable;
import com.eul4.common.wrapper.Reader;
import com.eul4.model.craft.player.CraftAdmin;
import com.eul4.model.player.physical.Admin;
import com.eul4.type.player.PluginObjectType;
import lombok.Getter;
import org.bukkit.entity.Player;

import java.io.IOException;

@Getter
public class AdminReader extends PhysicalPlayerReader<Admin>
{
	private final Reader<Admin> reader;
	private final BiParameterizedReadable<Admin, Player, Main> biParameterizedReadable;
	
	public AdminReader(Readers readers) throws InvalidVersionException
	{
		super(readers, Admin.class);
		
		final ObjectType objectType = PluginObjectType.ADMIN;
		final byte version = readers.getVersions().get(objectType);
		
		switch(version)
		{
		case 0:
			this.reader = this::readerVersion0;
			this.biParameterizedReadable = this::biParameterizedReadableVersion0;
			break;
		default:
			throw new InvalidVersionException("Invalid " + objectType + " version: " + version);
		}
	}
	
	private Readable<Admin> biParameterizedReadableVersion0(Player player, Main plugin)
	{
		return () -> new CraftAdmin(player, plugin);
	}
	
	@Override
	public Admin readReference(Player player, Common plugin) throws IOException, ClassNotFoundException
	{
		return super.readReference(biParameterizedReadable.getReadable(player, (Main) plugin));
	}
	
	private void readerVersion0(Admin admin) throws IOException, ClassNotFoundException
	{
		super.getReader().readObject(admin);
	}
}
