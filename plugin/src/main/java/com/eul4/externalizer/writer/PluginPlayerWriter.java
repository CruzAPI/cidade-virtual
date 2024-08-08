package com.eul4.externalizer.writer;

import com.eul4.common.externalizer.writer.CommonPlayerWriter;
import com.eul4.common.type.player.Writers;
import com.eul4.model.player.PluginPlayer;

import java.io.IOException;

public abstract sealed class PluginPlayerWriter<P extends PluginPlayer> extends CommonPlayerWriter<P>
	permits PhysicalPlayerWriter, SpiritualPlayerWriter
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
		writers.getWriter(TutorialTownPlayerDataWriter.class).writeReference(pluginPlayer.getTutorialTownPlayerData());
		writers.getWriter(VanillaPlayerDataWriter.class).writeReference(pluginPlayer.getVanillaPlayerData());
	}
}
