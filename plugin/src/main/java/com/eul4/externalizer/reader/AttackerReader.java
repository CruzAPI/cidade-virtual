package com.eul4.externalizer.reader;

import com.eul4.Main;
import com.eul4.Versions;
import com.eul4.common.exception.InvalidVersionException;
import com.eul4.common.wrapper.Reader;
import com.eul4.model.craft.player.CraftAttacker;
import com.eul4.model.player.Attacker;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.io.ObjectInput;

public class AttackerReader extends PluginPlayerReader<Attacker>
{
	private final Reader<Attacker> reader;
	
	public AttackerReader(ObjectInput in, Versions versions, Player player, Main plugin) throws InvalidVersionException
	{
		super(in, versions, plugin, () -> new CraftAttacker(player, plugin));
		
		if(versions.getAttackerVersion() == 0)
		{
			this.reader = this::readerVersion0;
		}
		else
		{
			throw new InvalidVersionException("Invalid Attacker version: " + versions.getAttackerVersion());
		}
	}
	
	private Attacker readerVersion0() throws IOException, ClassNotFoundException
	{
		Attacker attacker = getInstance();
		//TODO: ...?
		return attacker;
	}
	
	@Override
	protected Attacker readObject() throws IOException, ClassNotFoundException
	{
		super.readObject();
		
		return reader.readObject();
	}
}
