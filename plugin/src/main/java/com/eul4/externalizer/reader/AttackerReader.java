package com.eul4.externalizer.reader;

import com.eul4.Main;
import com.eul4.common.Common;
import com.eul4.common.exception.InvalidVersionException;
import com.eul4.common.type.player.ObjectType;
import com.eul4.common.type.player.Readers;
import com.eul4.common.wrapper.BiParameterizedReadable;
import com.eul4.common.wrapper.Readable;
import com.eul4.common.wrapper.Reader;
import com.eul4.model.craft.player.CraftAttacker;
import com.eul4.model.player.spiritual.Attacker;
import com.eul4.type.player.PluginObjectType;
import lombok.Getter;
import org.bukkit.entity.Player;

import java.io.IOException;

@Getter
public class AttackerReader extends SpiritualPlayerReader<Attacker>
{
	private final Reader<Attacker> reader;
	private final BiParameterizedReadable<Attacker, Player, Main> biParameterizedReadable;
	
	public AttackerReader(Readers readers) throws InvalidVersionException
	{
		super(readers, Attacker.class);
		
		final ObjectType objectType = PluginObjectType.ATTACKER;
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
	
	private void readerVersion0(Attacker attacker) throws IOException, ClassNotFoundException
	{
		super.getReader().readObject(attacker);
	}
	
	private Readable<Attacker> biParameterizedReadableVersion0(Player player, Main plugin)
	{
		return () -> new CraftAttacker(player, plugin);
	}
	
	@Override
	public Attacker readReference(Player player, Common plugin) throws IOException, ClassNotFoundException
	{
		return super.readReference(biParameterizedReadable.getReadable(player, (Main) plugin));
	}
}
