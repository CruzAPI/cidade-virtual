package com.eul4.externalizer.writer;

import com.eul4.common.type.player.Writers;
import com.eul4.model.town.structure.TownHall;

import java.io.IOException;

public class TownHallWriter extends StructureWriter<TownHall>
{
	public TownHallWriter(Writers writers)
	{
		super(writers);
	}
	
	@Override
	protected void writeObject(TownHall townHall) throws IOException
	{
		//TODO: write fields...
	}
}
