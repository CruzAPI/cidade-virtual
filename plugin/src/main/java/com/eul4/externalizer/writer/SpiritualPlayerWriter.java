package com.eul4.externalizer.writer;

import com.eul4.common.type.player.Writers;
import com.eul4.model.player.SpiritualPlayer;

import java.io.IOException;

public abstract non-sealed class SpiritualPlayerWriter<P extends SpiritualPlayer> extends PluginPlayerWriter<P>
{
	public SpiritualPlayerWriter(Writers writers, Class<P> type)
	{
		super(writers, type);
	}
	
	@Override
	protected void writeObject(P spiritualPlayer) throws IOException
	{
		super.writeObject(spiritualPlayer);
		
		out.writeInt(spiritualPlayer.getReincarnationType().ordinal());
	}
}
