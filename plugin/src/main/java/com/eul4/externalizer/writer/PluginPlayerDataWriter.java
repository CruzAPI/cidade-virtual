package com.eul4.externalizer.writer;

import com.eul4.common.externalizer.writer.ObjectWriter;
import com.eul4.common.type.player.Writers;
import com.eul4.model.playerdata.PluginPlayerData;

import java.io.IOException;

public class PluginPlayerDataWriter extends ObjectWriter<PluginPlayerData>
{
	public PluginPlayerDataWriter(Writers writers)
	{
		super(writers, PluginPlayerData.class);
	}
	
	@Override
	protected void writeObject(PluginPlayerData pluginPlayerData) throws IOException
	{
		writers.getWriter(TagWriter.class).writeReference(pluginPlayerData.getTag());
		out.writeBoolean(pluginPlayerData.isTagHidden());
	}
}
