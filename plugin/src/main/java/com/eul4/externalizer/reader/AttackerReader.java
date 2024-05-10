package com.eul4.externalizer.reader;

import com.eul4.Main;
import com.eul4.Versions;
import com.eul4.common.Common;
import com.eul4.common.exception.InvalidVersionException;
import com.eul4.common.wrapper.BiParameterizedReadable;
import com.eul4.common.wrapper.Readable;
import com.eul4.common.wrapper.Reader;
import com.eul4.model.craft.player.CraftAttacker;
import com.eul4.model.player.Attacker;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.io.ObjectInput;

public class AttackerReader extends PluginPlayerReader<Attacker>
{
	private final Reader<Attacker> reader;
	private final BiParameterizedReadable<Attacker, Player, Main> biParameterizedReadable;
	
	public AttackerReader(ObjectInput in, Versions versions) throws InvalidVersionException
	{
		super(in, versions);
		
		if(versions.getAttackerVersion() == 0)
		{
			this.reader = this::readerVersion0;
			this.biParameterizedReadable = this::biParameterizedReadableVersion0;
		}
		else
		{
			throw new InvalidVersionException("Invalid Attacker version: " + versions.getAttackerVersion());
		}
	}
	
	private Readable<Attacker> biParameterizedReadableVersion0(Player player, Main plugin) throws IOException, ClassNotFoundException
	{
		return () -> new CraftAttacker(player, plugin);
	}
	
	private Attacker readerVersion0(Attacker attacker) throws IOException, ClassNotFoundException
	{
		//TODO: read Attacker fields...
		return attacker;
	}
	
	@Override
	public Attacker readReference(Player player, Common plugin) throws IOException, ClassNotFoundException
	{
		return super.readReference(biParameterizedReadable.getReadable(player, (Main) plugin));
	}
	
	@Override
	protected Attacker readObject(Attacker attacker) throws IOException, ClassNotFoundException
	{
		super.readObject(attacker);
		
		return reader.readObject(attacker);
	}
}
