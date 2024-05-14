package com.eul4.common.externalizer.writer;

import com.eul4.common.model.player.CommonPlayer;
import com.eul4.common.type.player.Writers;

import java.io.IOException;

public abstract class CommonPlayerWriter<P extends CommonPlayer> extends ObjectWriter<P>
{
	public CommonPlayerWriter(Writers writers, Class<P> type)
	{
		super(writers, type);
	}
	
	@Override
	protected void writeObject(P commonPlayer) throws IOException
	{
		writers.getWriter(CommonPlayerDataWriter.class).writeReference(commonPlayer.getCommonPlayerData());
	}
}
