package com.eul4.externalizer.writer;

import com.eul4.model.player.TownPlayer;

import java.io.IOException;
import java.io.ObjectOutput;

public class TownPlayerWriter extends PluginPlayerWriter<TownPlayer>
{
	public TownPlayerWriter(ObjectOutput out)
	{
		super(out);
	}
	
	@Override
	protected void writeObject(TownPlayer townPlayer) throws IOException
	{
		super.writeObject(townPlayer);
	}
}
