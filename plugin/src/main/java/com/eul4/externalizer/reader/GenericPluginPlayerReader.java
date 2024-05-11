package com.eul4.externalizer.reader;

import com.eul4.Main;
import com.eul4.common.exception.InvalidVersionException;
import com.eul4.common.externalizer.reader.ObjectReader;
import com.eul4.common.type.player.ObjectType;
import com.eul4.common.type.player.Readers;
import com.eul4.common.wrapper.BiParameterizedReadable;
import com.eul4.common.wrapper.Readable;
import com.eul4.common.wrapper.Reader;
import com.eul4.model.player.PluginPlayer;
import com.eul4.type.player.PluginObjectType;
import com.eul4.type.player.PluginPlayerType;
import org.bukkit.entity.Player;

import java.io.IOException;

public class GenericPluginPlayerReader extends ObjectReader<PluginPlayer>
{
	private final Reader<PluginPlayer> reader;
	private final BiParameterizedReadable<PluginPlayer, Player, Main> biParameterizedReadable;
	
	public GenericPluginPlayerReader(Readers readers) throws InvalidVersionException
	{
		super(readers);
		
		final ObjectType objectType = PluginObjectType.GENERIC_PLUGIN_PLAYER;
		final byte version = readers.getVersions().get(objectType);
		
		switch(version)
		{
		case 0:
			this.reader = Reader.identity();
			this.biParameterizedReadable = this::biParameterizedReadableVersion0;
			break;
		default:
			throw new InvalidVersionException("Invalid " + objectType + " version: " + version);
		}
	}
	
	private Readable<PluginPlayer> biParameterizedReadableVersion0(Player player, Main plugin)
	{
		return () -> readers
				.getReader(PluginPlayerType.values()[in.readInt()].getReaderClass())
				.readReference(player, plugin);
	}
	
	public PluginPlayer readReference(Player player, Main plugin) throws IOException, ClassNotFoundException
	{
		return super.readReference(biParameterizedReadable.getReadable(player, plugin));
	}
	
	@Override
	protected PluginPlayer readObject(PluginPlayer pluginPlayer) throws IOException, ClassNotFoundException
	{
		return reader.readObject(pluginPlayer);
	}
}
