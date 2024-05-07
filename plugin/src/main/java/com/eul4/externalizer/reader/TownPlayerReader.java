package com.eul4.externalizer.reader;

import com.eul4.Main;
import com.eul4.Versions;
import com.eul4.common.exception.InvalidVersionException;
import com.eul4.common.wrapper.Reader;
import com.eul4.model.craft.player.CraftTownPlayer;
import com.eul4.model.player.TownPlayer;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.io.ObjectInput;

public class TownPlayerReader extends PluginPlayerReader<TownPlayer>
{
	private final Reader<TownPlayer> reader;
	
	public TownPlayerReader(ObjectInput in, Versions versions, Player player, Main plugin) throws InvalidVersionException
	{
		super(in, versions, plugin, () -> new CraftTownPlayer(player, plugin));
		
		if(versions.getTownPlayerVersion() == 0)
		{
			this.reader = this::readerVersion0;
		}
		else
		{
			throw new InvalidVersionException("Invalid TownPlayer version: " + versions.getTownPlayerVersion());
		}
	}
	
	private TownPlayer readerVersion0() throws IOException, ClassNotFoundException
	{
		TownPlayer townPlayer = getInstance();
		//TODO: ...?
		return townPlayer;
	}
	
	@Override
	protected TownPlayer readObject() throws IOException, ClassNotFoundException
	{
		super.readObject();
		
		return reader.readObject();
	}
}
