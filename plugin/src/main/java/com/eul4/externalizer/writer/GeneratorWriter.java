package com.eul4.externalizer.writer;

import com.eul4.common.type.player.Writers;
import com.eul4.model.town.structure.Generator;

import java.io.IOException;

public abstract class GeneratorWriter<G extends Generator> extends StructureWriter<G>
{
	public GeneratorWriter(Writers writers)
	{
		super(writers);
	}
	
	@Override
	protected void writeObject(G generator) throws IOException
	{
		out.writeInt(generator.getBalance());
	}
}
