package com.eul4.externalizer.writer;

import com.eul4.common.externalizer.writer.ObjectWriter;
import com.eul4.model.playerdata.TownPlayerData;

import java.io.IOException;
import java.io.ObjectOutput;

public class TownPlayerDataWriter extends ObjectWriter<TownPlayerData>
{
	protected TownPlayerDataWriter(ObjectOutput out)
	{
		super(out);
	}
	
	@Override
	protected void writeObject(TownPlayerData townPlayerData) throws IOException
	{
		out.writeBoolean(townPlayerData.isTest());
	}
}
