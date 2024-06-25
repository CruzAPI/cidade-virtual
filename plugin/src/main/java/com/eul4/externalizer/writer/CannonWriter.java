package com.eul4.externalizer.writer;

import com.eul4.common.type.player.Writers;
import com.eul4.model.town.structure.Cannon;

import java.io.IOException;

public class CannonWriter extends StructureWriter<Cannon>
{
	public CannonWriter(Writers writers)
	{
		super(writers, Cannon.class);
	}
	
	@Override
	protected void writeObject(Cannon cannon) throws IOException
	{
		super.writeObject(cannon);
	}
}
