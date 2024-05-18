package com.eul4.externalizer.writer;

import com.eul4.common.type.player.Writers;
import com.eul4.model.player.VanillaPlayer;

import java.io.IOException;

public class VanillaPlayerWriter extends PhysicalPlayerWriter<VanillaPlayer>
{
	public VanillaPlayerWriter(Writers writers)
	{
		super(writers, VanillaPlayer.class);
	}
	
	@Override
	protected void writeObject(VanillaPlayer vanillaPlayer) throws IOException
	{
		super.writeObject(vanillaPlayer);
	}
}
