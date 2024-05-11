package com.eul4.externalizer.writer;

import com.eul4.common.externalizer.writer.ObjectWriter;
import com.eul4.common.type.player.Writers;
import com.eul4.common.wrapper.BlockSerializable;
import com.eul4.model.town.TownBlock;
import com.eul4.model.town.structure.TownHall;

import java.io.IOException;

public class TownBlockWriter extends ObjectWriter<TownBlock>
{
	public TownBlockWriter(Writers writers)
	{
		super(writers);
	}
	
	@Override
	protected void writeObject(TownBlock townBlock) throws IOException
	{
		//TODO: write fields...
//		out.writeObject(new BlockSerializable(block));
//		out.writeBoolean(available);
	}
}
