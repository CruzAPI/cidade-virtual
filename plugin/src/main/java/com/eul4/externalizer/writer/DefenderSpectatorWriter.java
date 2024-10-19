package com.eul4.externalizer.writer;

import com.eul4.common.type.player.Writers;
import com.eul4.model.player.spiritual.DefenderSpectator;

import java.io.IOException;

public class DefenderSpectatorWriter extends SpiritualPlayerWriter<DefenderSpectator>
{
	public DefenderSpectatorWriter(Writers writers)
	{
		super(writers, DefenderSpectator.class);
	}
	
	@Override
	protected void writeObject(DefenderSpectator defenderSpectator) throws IOException
	{
		super.writeObject(defenderSpectator);
	}
}
