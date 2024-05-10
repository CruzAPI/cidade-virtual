package com.eul4.externalizer.reader;

import com.eul4.Main;
import com.eul4.Versions;
import com.eul4.common.exception.InvalidVersionException;
import com.eul4.common.externalizer.reader.ObjectReader;
import com.eul4.common.wrapper.BiParameterizedReadable;
import com.eul4.common.wrapper.Readable;
import com.eul4.common.wrapper.Reader;
import com.eul4.model.player.PluginPlayer;
import com.eul4.type.player.PluginPlayerType;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.io.ObjectInput;
import java.util.HashMap;
import java.util.Map;

public class GenericPluginPlayerReader extends ObjectReader<PluginPlayer>
{
	private final Reader<PluginPlayer> reader;
	private final BiParameterizedReadable<PluginPlayer, Player, Main> biParameterizedReadable;
	
	private final Map<PluginPlayerType, PluginPlayerReader<?>> readers = new HashMap<>();
	
	public GenericPluginPlayerReader(ObjectInput in, Versions versions) throws InvalidVersionException
	{
		super(in, versions);
		
		for(PluginPlayerType type : PluginPlayerType.values())
		{
			readers.put(type, type.getPluginReaderConstructor().newInstance(in, versions));
		}
		
		if(versions.getGenericPluginPlayerVersion() == 0)
		{
			this.reader = Reader.identity();
			this.biParameterizedReadable = this::biParameterizedReadableVersion0;
		}
		else
		{
			throw new InvalidVersionException("Invalid GenericPluginPlayer version: " + versions.getGenericPluginPlayerVersion());
		}
	}
	
	private Readable<PluginPlayer> biParameterizedReadableVersion0(Player player, Main plugin) throws IOException, ClassNotFoundException
	{
		return () -> readers.get(PluginPlayerType.values()[in.readInt()]).readReference(player, plugin);
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
