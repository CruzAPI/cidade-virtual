package com.eul4.externalizer.writer;

import com.eul4.common.externalizer.writer.CommonPlayerWriter;
import com.eul4.common.type.player.Writers;
import com.eul4.model.player.PluginPlayer;

import java.io.IOException;

public abstract class PluginPlayerWriter<P extends PluginPlayer> extends CommonPlayerWriter<P>
{
	public PluginPlayerWriter(Writers writers, Class<P> type)
	{
		super(writers, type);
	}
	
	@Override
	protected void writeObject(P pluginPlayer) throws IOException
	{
		super.writeObject(pluginPlayer);
		
		writers.getWriter(TownPlayerDataWriter.class).writeReference(pluginPlayer.getTownPlayerData());
	}
}
