package com.eul4.externalizer.reader;

import com.eul4.Main;
import com.eul4.common.Common;
import com.eul4.common.exception.InvalidVersionException;
import com.eul4.common.type.player.ObjectType;
import com.eul4.common.type.player.Readers;
import com.eul4.common.wrapper.BiParameterizedReadable;
import com.eul4.common.wrapper.Readable;
import com.eul4.common.wrapper.Reader;
import com.eul4.model.craft.player.CraftDefender;
import com.eul4.model.player.spiritual.Defender;
import com.eul4.type.player.PluginObjectType;
import lombok.Getter;
import org.bukkit.entity.Player;

import java.io.IOException;

@Getter
public class DefenderReader extends SpiritualPlayerReader<Defender>
{
	private final Reader<Defender> reader;
	private final BiParameterizedReadable<Defender, Player, Main> biParameterizedReadable;
	
	public DefenderReader(Readers readers) throws InvalidVersionException
	{
		super(readers, Defender.class);
		
		final ObjectType objectType = PluginObjectType.DEFENDER;
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
	
	private void readerVersion0(Defender defender) throws IOException, ClassNotFoundException
	{
		super.getReader().readObject(defender);
	}
	
	private Readable<Defender> biParameterizedReadableVersion0(Player player, Main plugin)
	{
		return () -> new CraftDefender(player, plugin);
	}
	
	@Override
	public Defender readReference(Player player, Common plugin) throws IOException, ClassNotFoundException
	{
		return super.readReference(biParameterizedReadable.getReadable(player, (Main) plugin));
	}
}
