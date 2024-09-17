package com.eul4.externalizer.reader;

import com.eul4.common.exception.InvalidVersionException;
import com.eul4.common.externalizer.reader.BigDecimalReader;
import com.eul4.common.externalizer.reader.ObjectReader;
import com.eul4.common.type.player.ObjectType;
import com.eul4.common.type.player.Readers;
import com.eul4.common.wrapper.Readable;
import com.eul4.common.wrapper.Reader;
import com.eul4.type.player.PluginObjectType;
import com.eul4.wrapper.CrownInfo;
import lombok.Getter;

import java.io.IOException;
import java.math.BigDecimal;

public class CrownInfoReader extends ObjectReader<CrownInfo>
{
	@Getter
	private final Reader<CrownInfo> reader;
	private final Readable<CrownInfo> readable;
	
	public CrownInfoReader(Readers readers) throws InvalidVersionException
	{
		super(readers, CrownInfo.class);
		
		final ObjectType objectType = PluginObjectType.CROWN_INFO;
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
	
	private CrownInfo readableVersion0() throws IOException, ClassNotFoundException
	{
		BigDecimalReader bigDecimalReader = readers.getReader(BigDecimalReader.class);
		
		BigDecimal serverTreasure = bigDecimalReader.readReference();
		BigDecimal jackpot = bigDecimalReader.readReference();
		BigDecimal townHallVault = bigDecimalReader.readReference();
		BigDecimal eul4Insights = bigDecimalReader.readReference();
		
		return new CrownInfo(serverTreasure, jackpot, townHallVault, eul4Insights);
	}
	
	public CrownInfo readReference() throws IOException, ClassNotFoundException
	{
		return super.readReference(readable);
	}
}
