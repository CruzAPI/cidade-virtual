package com.eul4.externalizer.writer;

import com.eul4.common.type.player.Writers;
import com.eul4.model.town.structure.DislikeGenerator;

import java.io.IOException;

public class DislikeGeneratorWriter extends GeneratorWriter<DislikeGenerator>
{
	public DislikeGeneratorWriter(Writers writers)
	{
		super(writers, DislikeGenerator.class);
	}
	
	@Override
	protected void writeObject(DislikeGenerator dislikeGenerator) throws IOException
	{
		super.writeObject(dislikeGenerator);
	}
}
