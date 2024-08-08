package com.eul4.externalizer.reader;

import com.eul4.common.exception.InvalidVersionException;
import com.eul4.common.externalizer.reader.ObjectReader;
import com.eul4.common.type.player.Readers;
import com.eul4.common.type.player.ObjectType;
import com.eul4.common.wrapper.ParameterizedReadable;
import com.eul4.common.wrapper.Readable;
import com.eul4.common.wrapper.Reader;
import com.eul4.model.player.PluginPlayer;
import com.eul4.model.playerdata.TownPlayerData;
import com.eul4.type.player.PluginObjectType;
import lombok.Getter;

import java.io.IOException;

@Getter
public class TownPlayerDataReader extends ObjectReader<TownPlayerData>
{
	private final Reader<TownPlayerData> reader;
	private final ParameterizedReadable<TownPlayerData, PluginPlayer> parameterizedReadable;
	
	public TownPlayerDataReader(Readers readers) throws InvalidVersionException
	{
		super(readers, TownPlayerData.class);
		
		final ObjectType objectType = PluginObjectType.TOWN_PLAYER_DATA;
		final byte version = readers.getVersions().get(objectType);
		
		switch(version)
		{
		case 0:
			this.reader = Reader.identity();
			this.parameterizedReadable = this::getParameterizedReadableVersion0;
			break;
		case 1:
			this.reader = Reader.identity();
			this.parameterizedReadable = this::getParameterizedReadableVersion1;
			break;
		default:
			throw new InvalidVersionException("Invalid " + objectType + " version: " + version);
		}
	}
	
	private Readable<TownPlayerData> getParameterizedReadableVersion0(PluginPlayer pluginPlayer)
	{
		return () ->
		{
			final boolean test = in.readBoolean();
			final boolean firstTimeJoiningTown = !pluginPlayer.hasTown();
			
			return TownPlayerData.builder()
					.test(test)
					.firstTimeJoiningTown(firstTimeJoiningTown)
					.build();
		};
	}
	
	private Readable<TownPlayerData> getParameterizedReadableVersion1(PluginPlayer pluginPlayer)
	{
		return () ->
		{
			final boolean test = in.readBoolean();
			final boolean firstTimeJoiningTown = in.readBoolean();
			
			return TownPlayerData.builder()
					.test(test)
					.firstTimeJoiningTown(firstTimeJoiningTown)
					.build();
		};
	}
	
	public TownPlayerData readReference(PluginPlayer pluginPlayer) throws IOException, ClassNotFoundException
	{
		return super.readReference(parameterizedReadable.getReadable(pluginPlayer));
	}
}
