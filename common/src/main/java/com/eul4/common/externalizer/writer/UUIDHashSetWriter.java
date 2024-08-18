package com.eul4.common.externalizer.writer;

import com.eul4.common.type.player.Writers;
import com.eul4.common.wrapper.UUIDHashSet;

import java.io.IOException;
import java.util.HashSet;
import java.util.UUID;

public class UUIDHashSetWriter extends ObjectWriter<UUIDHashSet>
{
	public UUIDHashSetWriter(Writers writers)
	{
		super(writers, UUIDHashSet.class);
	}
	
	@Override
	protected void writeObject(UUIDHashSet uuidHashSet) throws IOException
	{
		UUIDWriter uuidWriter = writers.getWriter(UUIDWriter.class);
		
		HashSet<UUID> hashSet = uuidHashSet.getHashSet();
		
		out.writeInt(hashSet.size());
		
		for(UUID uuid : hashSet)
		{
			uuidWriter.writeReference(uuid);
		}
	}
}
