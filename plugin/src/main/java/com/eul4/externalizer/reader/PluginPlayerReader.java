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
import lombok.Getter;
import org.bukkit.entity.Player;

import java.io.IOException;

@Getter
public abstract sealed class PluginPlayerReader<P extends PluginPlayer> extends CommonPlayerReader<P>
	permits PhysicalPlayerReader, SpiritualPlayerReader
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
	
	private void readerVersion0(P pluginPlayer) throws IOException, ClassNotFoundException
	{
		super.getReader().readObject(pluginPlayer);
		
		TownPlayerData townPlayerData = readers.getReader(TownPlayerDataReader.class).readReference();
		
		pluginPlayer.setTownPlayerData(townPlayerData);
	}
	
	public abstract BiParameterizedReadable<P, Player, Main> getBiParameterizedReadable();
}
