package com.eul4.externalizer.writer;

import com.eul4.common.type.player.Writers;
import com.eul4.model.player.spiritual.Defender;

import java.io.IOException;

public class DefenderWriter extends SpiritualPlayerWriter<Defender>
{
	public DefenderWriter(Writers writers)
	{
		super(writers, Defender.class);
	}
	
	@Override
	protected void writeObject(Defender defender) throws IOException
	{
		super.writeObject(defender);
	}
}
