package com.eul4.externalizer.reader;

import com.eul4.Main;
import com.eul4.common.Common;
import com.eul4.common.exception.InvalidVersionException;
import com.eul4.common.type.player.Readers;
import com.eul4.common.type.player.ObjectType;
import com.eul4.common.wrapper.BiParameterizedReadable;
import com.eul4.common.wrapper.Readable;
import com.eul4.common.wrapper.Reader;
import com.eul4.model.craft.player.CraftTownPlayer;
import com.eul4.model.player.TownPlayer;
import com.eul4.type.player.PluginObjectType;
import org.bukkit.entity.Player;

import java.io.IOException;

public class TownPlayerReader extends PluginPlayerReader<TownPlayer>
{
	private final Reader<TownPlayer> reader;
	private final BiParameterizedReadable<TownPlayer, Player, Main> biParameterizedReadable;
	
	public TownPlayerReader(Readers readers) throws InvalidVersionException
	{
		super(readers);
		
		final ObjectType objectType = PluginObjectType.TOWN_PLAYER;
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
	
	private Readable<TownPlayer> biParameterizedReadableVersion0(Player player, Main plugin) throws IOException, ClassNotFoundException
	{
		return () -> new CraftTownPlayer(player, plugin);
	}
	
	private TownPlayer readerVersion0(TownPlayer townPlayer)
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
