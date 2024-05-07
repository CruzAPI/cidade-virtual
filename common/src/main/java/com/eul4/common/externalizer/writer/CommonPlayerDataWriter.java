package com.eul4.common.externalizer.writer;

import com.eul4.common.model.data.CommonPlayerData;

import java.io.IOException;
import java.io.ObjectOutput;

public class CommonPlayerDataWriter extends ObjectWriter<CommonPlayerData>
{
	private final PlayerDataWriter playerDataWriter;
	
	public CommonPlayerDataWriter(ObjectOutput out)
	{
		super(out);
		
		this.playerDataWriter = new PlayerDataWriter(out);
	}
	
	@Override
	protected void writeObject(CommonPlayerData commonPlayerData) throws IOException
	{
		playerDataWriter.writeReference(commonPlayerData.getPlayerData());
	}
}
