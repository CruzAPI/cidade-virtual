package com.eul4.externalizer.reader;

import com.eul4.Versions;
import com.eul4.common.externalizer.reader.ObjectReader;
import com.eul4.common.exception.InvalidVersionException;
import com.eul4.common.wrapper.Reader;
import com.eul4.model.playerdata.TownPlayerData;

import java.io.IOException;
import java.io.ObjectInput;

public class TownPlayerDataReader extends ObjectReader<TownPlayerData>
{
	private final Reader<TownPlayerData> reader;
	
	public TownPlayerDataReader(ObjectInput in, Versions versions) throws InvalidVersionException
	{
		super(in, versions);
		
		if(versions.getTownPlayerVersion() == 0)
		{
			this.reader = this::readerVersion0;
		}
		else
		{
			throw new InvalidVersionException("Invalid TownPlayerData version: " + versions.getTownPlayerDataVersion());
		}
	}
	
	private TownPlayerData readerVersion0() throws IOException
	{
		boolean test = in.readBoolean();
		
		return TownPlayerData.builder()
				.test(test)
				.build();
	}
	
	@Override
	protected TownPlayerData readObject() throws IOException, ClassNotFoundException
	{
		return reader.readObject();
	}
}
