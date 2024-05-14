package com.eul4.externalizer.writer;

import com.eul4.common.type.player.Writers;
import com.eul4.model.town.structure.Generator;

import java.io.IOException;

public abstract class GeneratorWriter<G extends Generator> extends StructureWriter<G>
{
	public GeneratorWriter(Writers writers, Class<G> type)
	{
		super(writers, type);
	}
	
	@Override
	protected void writeObject(G generator) throws IOException
	{
		super.writeObject(generator);
		out.writeInt(generator.getBalance());
	}
}
