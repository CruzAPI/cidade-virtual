package com.eul4.externalizer.writer;

import com.eul4.common.externalizer.writer.ObjectWriter;
import com.eul4.common.type.player.Writers;
import com.eul4.model.playerdata.VanillaPlayerData;

import java.io.IOException;

public class VanillaPlayerDataWriter extends ObjectWriter<VanillaPlayerData>
{
	public VanillaPlayerDataWriter(Writers writers)
	{
		super(writers, VanillaPlayerData.class);
	}
	
	@Override
	protected void writeObject(VanillaPlayerData vanillaPlayerData) throws IOException
	{
		writers.getWriter(HomeMapWriter.class).writeReference(vanillaPlayerData.getHomeMap());
	}
}
