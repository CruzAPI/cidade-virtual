package com.eul4.externalizer.reader;

import com.eul4.common.exception.InvalidVersionException;
import com.eul4.common.externalizer.reader.ObjectReader;
import com.eul4.common.type.player.ObjectType;
import com.eul4.common.type.player.Readers;
import com.eul4.common.wrapper.Readable;
import com.eul4.common.wrapper.Reader;
import com.eul4.i18n.BroadcastRichMessage;
import com.eul4.type.player.PluginObjectType;
import com.eul4.wrapper.BroadcastHashSet;
import lombok.Getter;

import java.io.IOException;

public class BroadcastHashSetReader extends ObjectReader<BroadcastHashSet>
{
	@Getter
	private final Reader<BroadcastHashSet> reader;
	private final Readable<BroadcastHashSet> readable;
	
	public BroadcastHashSetReader(Readers readers) throws InvalidVersionException
	{
		super(readers, BroadcastHashSet.class);
		
		final ObjectType objectType = PluginObjectType.BROADCAST_HASH_SET;
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
	
	private BroadcastHashSet readableVersion0() throws IOException
	{
		BroadcastHashSet broadcastHashSet = new BroadcastHashSet();
		
		int size = in.readInt();
		
		for(int i = 0; i < size; i++)
		{
			int id = in.readByte();
			broadcastHashSet.add(BroadcastRichMessage.getByIdOrThrow(id));
		}
		
		return broadcastHashSet;
	}
	
	public BroadcastHashSet readReference() throws IOException, ClassNotFoundException
	{
		return super.readReference(readable);
	}
}
