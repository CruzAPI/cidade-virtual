package com.eul4.common.externalizer.reader;

import com.eul4.common.exception.InvalidVersionException;
import com.eul4.common.type.player.CommonObjectType;
import com.eul4.common.type.player.ObjectType;
import com.eul4.common.type.player.Readers;
import com.eul4.common.wrapper.Readable;
import com.eul4.common.wrapper.Reader;
import lombok.Getter;
import org.bukkit.util.BlockVector;

import java.io.IOException;

public class BlockVectorReader extends ObjectReader<BlockVector>
{
	@Getter
	private final Reader<BlockVector> reader;
	private final Readable<BlockVector> readable;
	
	public BlockVectorReader(Readers readers) throws InvalidVersionException
	{
		super(readers, BlockVector.class);
		
		final ObjectType objectType = CommonObjectType.BLOCK_VECTOR;
		final byte version = readers.getVersions().get(objectType);
		
		switch(version)
		{
		case 0:
			this.reader = Reader.identity();
			this.readable = this::readableVersion0;
			break;
		default:
			throw new InvalidVersionException("Invalid " + objectType + " version: " + version);
		}
	}
	
	private BlockVector readableVersion0() throws IOException
	{
		return new BlockVector(in.readInt(), in.readInt(), in.readInt());
	}
}
