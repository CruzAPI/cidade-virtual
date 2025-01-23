package com.eul4.common.externalizer.reader;

import com.eul4.common.exception.InvalidVersionException;
import com.eul4.common.type.player.CommonObjectType;
import com.eul4.common.type.player.ObjectType;
import com.eul4.common.type.player.Readers;
import com.eul4.common.wrapper.Readable;
import com.eul4.common.wrapper.Reader;
import lombok.Getter;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;

public class BigDecimalReader extends ObjectReader<BigDecimal>
{
	@Getter
	private final Reader<BigDecimal> reader;
	private final Readable<BigDecimal> readable;
	
	public BigDecimalReader(Readers readers) throws InvalidVersionException
	{
		super(readers, BigDecimal.class);
		
		final ObjectType objectType = CommonObjectType.BIG_DECIMAL;
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
	
	private BigDecimal readableVersion0() throws IOException
	{
		int lenght = in.readInt();
		byte[] unscaledValueBytes = new byte[lenght];
		in.readFully(unscaledValueBytes);
		int scale = in.readInt();
		
		BigInteger unscaledValue = new BigInteger(unscaledValueBytes);
		return new BigDecimal(unscaledValue, scale);
	}
	
	public BigDecimal readReference() throws IOException, ClassNotFoundException
	{
		return super.readReference(readable);
	}
}
