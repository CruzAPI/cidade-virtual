package com.eul4.common.externalizer.reader;

import com.eul4.common.exception.InvalidVersionException;
import com.eul4.common.wrapper.CommonVersions;
import com.eul4.common.wrapper.Readable;
import com.eul4.common.wrapper.Reader;
import org.bukkit.util.BlockVector;

import java.io.IOException;
import java.io.ObjectInput;

public class BlockVectorReader extends ObjectReader<BlockVector>
{
	private final Reader<BlockVector> reader;
	private final Readable<BlockVector> readable;
	
	public BlockVectorReader(ObjectInput in, CommonVersions commonVersions) throws InvalidVersionException
	{
		super(in, commonVersions);
		
		if(commonVersions.getBlockVectorVersion() == 0)
		{
			this.reader = this::readerVersion0;
			this.readable = this::readableVersion0;
		}
		else
		{
			throw new InvalidVersionException("Invalid BlockVector version: " + commonVersions.getBlockVectorVersion());
		}
	}
	
	private BlockVector readerVersion0(BlockVector blockVector) throws IOException, ClassNotFoundException
	{
		return blockVector;
	}
	
	private BlockVector readableVersion0() throws IOException, ClassNotFoundException
	{
		return new BlockVector(in.readInt(), in.readInt(), in.readInt());
	}
	
	@Override
	protected BlockVector readObject(BlockVector blockVector) throws IOException, ClassNotFoundException
	{
		return reader.readObject(blockVector);
	}
}
