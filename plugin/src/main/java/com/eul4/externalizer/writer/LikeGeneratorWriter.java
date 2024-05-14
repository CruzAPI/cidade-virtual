package com.eul4.externalizer.writer;

import com.eul4.common.type.player.Writers;
import com.eul4.model.town.structure.LikeGenerator;

import java.io.IOException;

public class LikeGeneratorWriter extends GeneratorWriter<LikeGenerator>
{
	public LikeGeneratorWriter(Writers writers)
	{
		super(writers, LikeGenerator.class);
	}
	
	@Override
	protected void writeObject(LikeGenerator likeGenerator) throws IOException
	{
		super.writeObject(likeGenerator);
	}
}
