package com.eul4.externalizer.writer;

import com.eul4.model.player.Attacker;

import java.io.IOException;
import java.io.ObjectOutput;

public class AttackerWriter extends PluginPlayerWriter<Attacker>
{
	public AttackerWriter(ObjectOutput out)
	{
		super(out);
	}
	
	@Override
	protected void writeObject(Attacker attacker) throws IOException
	{
		super.writeObject(attacker);
	}
}
