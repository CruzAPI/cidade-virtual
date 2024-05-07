package com.eul4.common.externalizer.writer;

import com.eul4.common.model.player.CommonPlayer;

import java.io.IOException;
import java.io.ObjectOutput;

public abstract class CommonPlayerWriter<P extends CommonPlayer> extends ObjectWriter<P>
{
	private final CommonPlayerDataWriter commonPlayerDataWriter;
	
	public CommonPlayerWriter(ObjectOutput out)
	{
		super(out);
		
		this.commonPlayerDataWriter = new CommonPlayerDataWriter(out);
	}
	
	@Override
	protected void writeObject(P commonPlayer) throws IOException
	{
		commonPlayerDataWriter.writeReference(commonPlayer.getCommonPlayerData());
	}
}
