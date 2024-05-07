package com.eul4.externalizer.writer;

import com.eul4.common.externalizer.writer.CommonPlayerWriter;
import com.eul4.model.player.PluginPlayer;

import java.io.IOException;
import java.io.ObjectOutput;

public abstract class PluginPlayerWriter<P extends PluginPlayer> extends CommonPlayerWriter<P>
{
	private final TownPlayerDataWriter townPlayerDataWriter;
	
	public PluginPlayerWriter(ObjectOutput out)
	{
		super(out);
		
		this.townPlayerDataWriter = new TownPlayerDataWriter(out);
	}
	
	@Override
	protected void writeObject(P pluginPlayer) throws IOException
	{
		super.writeObject(pluginPlayer);
		
		townPlayerDataWriter.writeReference(pluginPlayer.getTownPlayerData());
	}
}
