package com.eul4.common.externalizer.writer;

import com.eul4.common.model.permission.TimedTick;
import com.eul4.common.type.player.Writers;

import java.io.IOException;

public class TimedTickWriter extends ObjectWriter<TimedTick>
{
	public TimedTickWriter(Writers writers)
	{
		super(writers, TimedTick.class);
	}
	
	@Override
	protected void writeObject(TimedTick timedTick) throws IOException
	{
		out.writeLong(timedTick.getCreationTick());
		out.writeLong(timedTick.getDurationTick());
	}
}
