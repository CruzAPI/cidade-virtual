package com.eul4.externalizer.reader;

import com.eul4.common.exception.InvalidVersionException;
import com.eul4.common.externalizer.reader.ObjectReader;
import com.eul4.common.type.player.Readers;
import com.eul4.common.type.player.ObjectType;
import com.eul4.common.wrapper.Readable;
import com.eul4.common.wrapper.Reader;
import com.eul4.model.playerdata.TownPlayerData;
import com.eul4.type.player.PluginObjectType;

import java.io.IOException;

public class TownPlayerDataReader extends ObjectReader<TownPlayerData>
{
	private final Reader<TownPlayerData> reader;
	private final Readable<TownPlayerData> readable;
	
	public TownPlayerDataReader(Readers readers) throws InvalidVersionException
	{
		super(readers, TownPlayerData.class);
		
		final ObjectType objectType = PluginObjectType.TOWN_PLAYER_DATA;
		final byte version = readers.getVersions().get(objectType);
		
		switch(version)
		{
		case 0:
			this.reader = Reader.identity();
			this.readable = this::readableVersion0;
			break;
		default:
			throw new InvalidVersionException("Invalid " + objectType + " version: " + version);
		}
	}
	
	private TownPlayerData readableVersion0() throws IOException
	{
		boolean test = in.readBoolean();
		
		return TownPlayerData.builder()
				.test(test)
				.build();
	}
	
	public TownPlayerData readReference() throws IOException, ClassNotFoundException
	{
		return super.readReference(readable);
	}
	
	@Override
	protected TownPlayerData readObject(TownPlayerData townPlayerData) throws IOException, ClassNotFoundException
	{
		return reader.readObject(townPlayerData);
	}
}
