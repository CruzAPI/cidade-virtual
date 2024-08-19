package com.eul4.common.externalizer.writer;

import com.eul4.common.type.player.Writers;

import java.io.IOException;
import java.util.UUID;

public class UUIDWriter extends ObjectWriter<UUID>
{
	public UUIDWriter(Writers writers)
	{
		super(writers, UUID.class);
	}
	
	@Override
	protected void writeObject(UUID uuid) throws IOException
	{
		out.writeLong(uuid.getMostSignificantBits());
		out.writeLong(uuid.getLeastSignificantBits());
	}
}
