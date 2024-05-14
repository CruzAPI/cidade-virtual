package com.eul4.common.externalizer.writer;

import com.eul4.common.model.data.CommonPlayerData;
import com.eul4.common.type.player.Writers;

import java.io.IOException;

public class CommonPlayerDataWriter extends ObjectWriter<CommonPlayerData>
{
	public CommonPlayerDataWriter(Writers writers)
	{
		super(writers, CommonPlayerData.class);
	}
	
	@Override
	protected void writeObject(CommonPlayerData commonPlayerData) throws IOException
	{
		writers.getWriter(PlayerDataWriter.class).writeReference(commonPlayerData.getPlayerData());
	}
}
