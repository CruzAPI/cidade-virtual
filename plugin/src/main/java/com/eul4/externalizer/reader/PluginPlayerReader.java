package com.eul4.externalizer.reader;

import com.eul4.Main;
import com.eul4.common.exception.InvalidVersionException;
import com.eul4.common.externalizer.reader.CommonPlayerReader;
import com.eul4.common.type.player.ObjectType;
import com.eul4.common.type.player.Readers;
import com.eul4.common.wrapper.BiParameterizedReadable;
import com.eul4.common.wrapper.Reader;
import com.eul4.model.player.PluginPlayer;
import com.eul4.model.playerdata.TownPlayerData;
import com.eul4.type.player.PluginObjectType;
import org.bukkit.entity.Player;

import java.io.IOException;

public abstract class PluginPlayerReader<P extends PluginPlayer> extends CommonPlayerReader<P>
{
	private final Reader<P> reader;
	
	public PluginPlayerReader(Readers readers, Class<P> type) throws InvalidVersionException
	{
		super(readers, type);
		
		final ObjectType objectType = PluginObjectType.PLUGIN_PLAYER;
		final byte version = readers.getVersions().get(objectType);
		
		switch(version)
		{
		case 0:
			this.reader = this::readerVersion0;
			break;
		default:
			throw new InvalidVersionException("Invalid " + objectType + " version: " + version);
		}
	}
	
	private P readerVersion0(P pluginPlayer) throws IOException, ClassNotFoundException
	{
		TownPlayerData townPlayerData = readers.getReader(TownPlayerDataReader.class).readReference();
		
		pluginPlayer.setTownPlayerData(townPlayerData);
		
		return pluginPlayer;
	}
	
	public abstract BiParameterizedReadable<P, Player, Main> getBiParameterizedReadable();
	
	@Override
	protected P readObject(P pluginPlayer) throws IOException, ClassNotFoundException
	{
		super.readObject(pluginPlayer);
		
		return reader.readObject(pluginPlayer);
	}
}
