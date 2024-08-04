package com.eul4.externalizer.writer;

import com.eul4.common.type.player.Writers;
import com.eul4.model.player.TownPlayer;

import java.io.IOException;

public class TownPlayerWriter<TP extends TownPlayer> extends PhysicalPlayerWriter<TP>
{
	public TownPlayerWriter(Writers writers)
	{
		this(writers, (Class<TP>) TownPlayer.class);
	}
	
	public TownPlayerWriter(Writers writers, Class<TP> type)
	{
		super(writers, type);
	}
	
	@Override
	protected void writeObject(TP townPlayer) throws IOException
	{
		super.writeObject(townPlayer);
	}
}
