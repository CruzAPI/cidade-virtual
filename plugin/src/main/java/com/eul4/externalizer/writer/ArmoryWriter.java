package com.eul4.externalizer.writer;

import com.eul4.common.type.player.Writers;
import com.eul4.model.town.structure.Armory;

import java.io.IOException;

public class ArmoryWriter extends StructureWriter<Armory>
{
	public ArmoryWriter(Writers writers)
	{
		super(writers, Armory.class);
	}
	
	@Override
	protected void writeObject(Armory Armory) throws IOException
	{
		super.writeObject(Armory);
	}
}
