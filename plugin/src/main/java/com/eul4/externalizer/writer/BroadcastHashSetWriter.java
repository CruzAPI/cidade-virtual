package com.eul4.externalizer.writer;

import com.eul4.common.externalizer.writer.ObjectWriter;
import com.eul4.common.type.player.Writers;
import com.eul4.i18n.BroadcastRichMessage;
import com.eul4.wrapper.BroadcastHashSet;

import java.io.IOException;
import java.util.Iterator;

public class BroadcastHashSetWriter extends ObjectWriter<BroadcastHashSet>
{
	public BroadcastHashSetWriter(Writers writers)
	{
		super(writers, BroadcastHashSet.class);
	}
	
	@Override
	protected void writeObject(BroadcastHashSet broadcastHashSet) throws IOException
	{
		out.writeInt(broadcastHashSet.size());
		
		Iterator<BroadcastRichMessage> iterator = broadcastHashSet.iterator();
		
		while(iterator.hasNext())
		{
			out.writeByte(iterator.next().getId());
		}
	}
}
