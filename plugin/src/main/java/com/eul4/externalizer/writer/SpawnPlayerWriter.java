package com.eul4.externalizer.writer;

import com.eul4.common.type.player.Writers;
import com.eul4.model.player.SpawnPlayer;

import java.io.IOException;

public class SpawnPlayerWriter extends PhysicalPlayerWriter<SpawnPlayer>
{
	public SpawnPlayerWriter(Writers writers)
	{
		super(writers, SpawnPlayer.class);
	}
	
	@Override
	protected void writeObject(SpawnPlayer spawnPlayer) throws IOException
	{
		super.writeObject(spawnPlayer);
	}
}
