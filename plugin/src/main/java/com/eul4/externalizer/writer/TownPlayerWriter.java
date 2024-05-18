package com.eul4.externalizer.writer;

import com.eul4.common.type.player.Writers;
import com.eul4.model.player.TownPlayer;

import java.io.IOException;

public class TownPlayerWriter extends PhysicalPlayerWriter<TownPlayer>
{
	public TownPlayerWriter(Writers writers)
	{
		super(writers, TownPlayer.class);
	}
	
	@Override
	protected void writeObject(TownPlayer townPlayer) throws IOException
	{
		super.writeObject(townPlayer);
	}
}
