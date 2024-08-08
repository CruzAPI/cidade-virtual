package com.eul4.externalizer.writer;

import com.eul4.common.externalizer.writer.ObjectWriter;
import com.eul4.common.type.player.Writers;
import com.eul4.model.playerdata.TownPlayerData;

import java.io.IOException;

public class TownPlayerDataWriter extends ObjectWriter<TownPlayerData>
{
	public TownPlayerDataWriter(Writers writers)
	{
		super(writers, TownPlayerData.class);
	}
	
	@Override
	protected void writeObject(TownPlayerData townPlayerData) throws IOException
	{
		out.writeBoolean(townPlayerData.isTest());
		out.writeBoolean(townPlayerData.isFirstTimeJoiningTown());
	}
}
