package com.eul4.externalizer.reader;

import com.eul4.common.exception.InvalidVersionException;
import com.eul4.common.externalizer.reader.BigDecimalReader;
import com.eul4.common.externalizer.reader.ObjectReader;
import com.eul4.common.type.player.ObjectType;
import com.eul4.common.type.player.Readers;
import com.eul4.common.wrapper.Readable;
import com.eul4.common.wrapper.Reader;
import com.eul4.type.player.PluginObjectType;
import com.eul4.wrapper.CryptoInfo;
import lombok.Getter;

import java.io.IOException;
import java.math.BigDecimal;

public class CryptoInfoReader extends ObjectReader<CryptoInfo>
{
	@Getter
	private final Reader<CryptoInfo> reader;
	private final Readable<CryptoInfo> readable;
	
	public CryptoInfoReader(Readers readers) throws InvalidVersionException
	{
		super(readers, CryptoInfo.class);
		
		final ObjectType objectType = PluginObjectType.CRYPTO_INFO;
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
	
	private CryptoInfo readableVersion0() throws IOException, ClassNotFoundException
	{
		BigDecimalReader bigDecimalReader = readers.getReader(BigDecimalReader.class);
		
		BigDecimal marketCap = bigDecimalReader.readReference();
		BigDecimal circulatingSupply = bigDecimalReader.readReference();
		
		return new CryptoInfo(marketCap, circulatingSupply);
	}
	
	public CryptoInfo readReference() throws IOException, ClassNotFoundException
	{
		return super.readReference(readable);
	}
}
