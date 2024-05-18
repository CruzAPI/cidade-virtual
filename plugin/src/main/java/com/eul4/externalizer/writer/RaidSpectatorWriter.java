package com.eul4.externalizer.writer;

import com.eul4.common.type.player.Writers;
import com.eul4.model.player.RaidSpectator;

import java.io.IOException;

public class RaidSpectatorWriter extends SpiritualPlayerWriter<RaidSpectator>
{
	public RaidSpectatorWriter(Writers writers)
	{
		super(writers, RaidSpectator.class);
	}
	
	@Override
	protected void writeObject(RaidSpectator raidSpectator) throws IOException
	{
		super.writeObject(raidSpectator);
	}
}
