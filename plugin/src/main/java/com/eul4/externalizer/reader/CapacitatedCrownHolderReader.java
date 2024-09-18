package com.eul4.externalizer.reader;

import com.eul4.common.exception.InvalidVersionException;
import com.eul4.common.externalizer.reader.BigDecimalReader;
import com.eul4.common.externalizer.reader.ObjectReader;
import com.eul4.common.type.player.ObjectType;
import com.eul4.common.type.player.Readers;
import com.eul4.common.wrapper.Readable;
import com.eul4.common.wrapper.Reader;
import com.eul4.holder.CapacitatedCrownHolder;
import com.eul4.type.player.PluginObjectType;
import lombok.Getter;

import java.io.IOException;

public class CapacitatedCrownHolderReader extends ObjectReader<CapacitatedCrownHolder>
{
	@Getter
	private final Reader<CapacitatedCrownHolder> reader;
	private final Readable<CapacitatedCrownHolder> readable;
	
	public CapacitatedCrownHolderReader(Readers readers) throws InvalidVersionException
	{
		super(readers, CapacitatedCrownHolder.class);
		
		final ObjectType objectType = PluginObjectType.CAPACITATED_CROWN_HOLDER;
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
	
	private CapacitatedCrownHolder readableVersion0() throws IOException, ClassNotFoundException
	{
		return new CapacitatedCrownHolder(readers.getReader(BigDecimalReader.class).readReference());
	}
	
	public CapacitatedCrownHolder readReference() throws IOException, ClassNotFoundException
	{
		return super.readReference(readable);
	}
}
