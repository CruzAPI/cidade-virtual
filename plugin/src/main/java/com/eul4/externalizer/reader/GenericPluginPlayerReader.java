package com.eul4.externalizer.reader;

import com.eul4.Main;
import com.eul4.Versions;
import com.eul4.common.exception.InvalidVersionException;
import com.eul4.common.externalizer.reader.ObjectReader;
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
	
	private final Map<PluginPlayerType, PluginPlayerReader<?>> readers = new HashMap<>();
	
	public GenericPluginPlayerReader(ObjectInput in, Versions versions, Player player, Main plugin) throws InvalidVersionException
	{
		super(in, versions);
		
		for(PluginPlayerType type : PluginPlayerType.values())
		{
			readers.put(type, type.getPluginReaderConstructor().newInstance(in, versions, player, plugin));
		}
		
		if(versions.getGenericPluginPlayerVersion() == 0)
		{
			this.reader = this::readerVersion0;
		}
		else
		{
			throw new InvalidVersionException("Invalid GenericPluginPlayer version: " + versions.getGenericPluginPlayerVersion());
		}
	}
	
	private PluginPlayer readerVersion0() throws IOException, ClassNotFoundException
	{
		int ordinal = in.readInt();
		
		PluginPlayerType type = PluginPlayerType.values()[ordinal];
		
		if(readers.containsKey(type))
		{
			return readers.get(type).readReference();
		}
		
		return null;
	}
	
	@Override
	protected PluginPlayer readObject() throws IOException, ClassNotFoundException
	{
		return reader.readObject();
	}
}
