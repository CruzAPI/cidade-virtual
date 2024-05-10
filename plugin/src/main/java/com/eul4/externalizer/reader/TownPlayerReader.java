package com.eul4.externalizer.reader;

import com.eul4.Main;
import com.eul4.Versions;
import com.eul4.common.Common;
import com.eul4.common.exception.InvalidVersionException;
import com.eul4.common.wrapper.BiParameterizedReadable;
import com.eul4.common.wrapper.Readable;
import com.eul4.common.wrapper.Reader;
import com.eul4.model.craft.player.CraftTownPlayer;
import com.eul4.model.player.TownPlayer;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.io.ObjectInput;

public class TownPlayerReader extends PluginPlayerReader<TownPlayer>
{
	private final Reader<TownPlayer> reader;
	private final BiParameterizedReadable<TownPlayer, Player, Main> biParameterizedReadable;
	
	public TownPlayerReader(ObjectInput in, Versions versions) throws InvalidVersionException
	{
		super(in, versions);
		
		if(versions.getTownPlayerVersion() == 0)
		{
			this.reader = this::readerVersion0;
			this.biParameterizedReadable = this::biParameterizedReadableVersion0;
		}
		else
		{
			throw new InvalidVersionException("Invalid TownPlayer version: " + versions.getTownPlayerVersion());
		}
	}
	
	private Readable<TownPlayer> biParameterizedReadableVersion0(Player player, Main plugin) throws IOException, ClassNotFoundException
	{
		return () -> new CraftTownPlayer(player, plugin);
	}
	
	private TownPlayer readerVersion0(TownPlayer townPlayer) throws IOException, ClassNotFoundException
	{
		//TODO: read TownPlayer fields...
		return townPlayer;
	}
	
	@Override
	public TownPlayer readReference(Player player, Common plugin) throws IOException, ClassNotFoundException
	{
		return super.readReference(biParameterizedReadable.getReadable(player, (Main) plugin));
	}
	
	@Override
	protected TownPlayer readObject(TownPlayer townPlayer) throws IOException, ClassNotFoundException
	{
		super.readObject(townPlayer);
		
		return reader.readObject(townPlayer);
	}
}
