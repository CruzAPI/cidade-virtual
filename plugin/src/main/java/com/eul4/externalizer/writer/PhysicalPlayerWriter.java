package com.eul4.externalizer.writer;

import com.eul4.common.type.player.Writers;
import com.eul4.model.player.PhysicalPlayer;

import java.io.IOException;

public abstract non-sealed class PhysicalPlayerWriter<P extends PhysicalPlayer> extends PluginPlayerWriter<P>
{
	public PhysicalPlayerWriter(Writers writers, Class<P> type)
	{
		super(writers, type);
	}
	
	@Override
	protected void writeObject(P physicalPlayer) throws IOException
	{
		super.writeObject(physicalPlayer);
	}
}
