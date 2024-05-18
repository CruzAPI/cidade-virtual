package com.eul4.externalizer.writer;

import com.eul4.common.externalizer.writer.ObjectWriter;
import com.eul4.common.type.player.Writers;
import com.eul4.model.player.PluginPlayer;

import java.io.IOException;

public class GenericPluginPlayerWriter extends ObjectWriter<PluginPlayer>
{
	public GenericPluginPlayerWriter(Writers writers)
	{
		super(writers, PluginPlayer.class);
	}
	
	@Override
	protected void writeObject(PluginPlayer pluginPlayer) throws IOException
	{
		out.writeInt(pluginPlayer.getPlayerType().getCategory().ordinal());
		out.writeInt(pluginPlayer.getPlayerType().ordinal());
		getWriterAndWriteReference(pluginPlayer, pluginPlayer.getPlayerType().getInterfaceType());
	}
	
	@SuppressWarnings("unchecked")
	private <P extends PluginPlayer> void getWriterAndWriteReference(PluginPlayer pluginPlayer, Class<P> type) throws IOException
	{
		PluginPlayerWriter<P> writer = (PluginPlayerWriter<P>) writers.getWriter(pluginPlayer.getPlayerType().getWriterClass());
		
		writer.writeReference(type.cast(pluginPlayer));
	}
}
