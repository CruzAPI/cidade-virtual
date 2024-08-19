package com.eul4.common.externalizer.reader;

import com.eul4.common.exception.InvalidVersionException;
import com.eul4.common.type.player.CommonObjectType;
import com.eul4.common.type.player.ObjectType;
import com.eul4.common.type.player.Readers;
import com.eul4.common.wrapper.Readable;
import com.eul4.common.wrapper.Reader;
import com.eul4.common.wrapper.UUIDHashSet;
import lombok.Getter;

import java.io.IOException;

public class UUIDHashSetReader extends ObjectReader<UUIDHashSet>
{
	@Getter
	private final Reader<UUIDHashSet> reader;
	private final Readable<UUIDHashSet> readable;
	
	public UUIDHashSetReader(Readers readers) throws InvalidVersionException
	{
		super(readers, UUIDHashSet.class);
		
		final ObjectType objectType = CommonObjectType.UUID_HASH_SET;
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
	
	private UUIDHashSet readableVersion0() throws IOException, ClassNotFoundException
	{
		UUIDReader uuidReader = readers.getReader(UUIDReader.class);
		
		UUIDHashSet uuidHashSet = new UUIDHashSet();
		
		int size = in.readInt();
		
		for(int i = 0; i < size; i++)
		{
			uuidHashSet.add(uuidReader.readReference());
		}
		
		return uuidHashSet;
	}
	
	public UUIDHashSet readReference() throws IOException, ClassNotFoundException
	{
		return super.readReference(readable);
	}
}
