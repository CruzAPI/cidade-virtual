package com.eul4.externalizer.writer;

import com.eul4.common.type.player.Writers;
import com.eul4.model.player.Attacker;

import java.io.IOException;

public class AttackerWriter extends PluginPlayerWriter<Attacker>
{
	public AttackerWriter(Writers writers)
	{
		super(writers, Attacker.class);
	}
	
	@Override
	protected void writeObject(Attacker attacker) throws IOException
	{
		super.writeObject(attacker);
	}
}
