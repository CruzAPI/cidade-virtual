package com.eul4.externalizer.reader;

import com.eul4.common.exception.InvalidVersionException;
import com.eul4.common.externalizer.reader.ObjectReader;
import com.eul4.common.type.player.ObjectType;
import com.eul4.common.type.player.Readers;
import com.eul4.common.wrapper.ParameterizedReadable;
import com.eul4.common.wrapper.Readable;
import com.eul4.common.wrapper.Reader;
import com.eul4.type.player.PluginObjectType;
import com.eul4.wrapper.BlockDataMap;
import lombok.Getter;
import org.bukkit.Chunk;

import java.io.IOException;

@Getter
public class BlockDataMapReader extends ObjectReader<BlockDataMap>
{
	private final Reader<BlockDataMap> reader;
	private final ParameterizedReadable<BlockDataMap, Chunk> parameterizedReadable;
	
	public BlockDataMapReader(Readers readers) throws InvalidVersionException
	{
		super(readers, BlockDataMap.class);
		
		final ObjectType objectType = PluginObjectType.BLOCK_DATA_MAP;
		final byte version = readers.getVersions().get(objectType);
		
		switch(version)
		{
		case 0:
			this.reader = this::readerVersion0;
			this.parameterizedReadable = this::parameterizedReadableVersion0;
			break;
		default:
			throw new InvalidVersionException("Invalid " + objectType + " version: " + version);
		}
	}
	
	private void readerVersion0(BlockDataMap blockDataMap) throws IOException, ClassNotFoundException
	{
		final int size = in.readInt();
		
		for(int i = 0; i < size; i++)
		{
			blockDataMap.put(
					readers.getReader(ShortCoordinateBlockChunkReader.class).readObject(blockDataMap.getChunk()),
					readers.getReader(BlockDataReader.class).readObject());
		}
	}
	
	private Readable<BlockDataMap> parameterizedReadableVersion0(Chunk chunk)
	{
		return () -> new BlockDataMap(chunk);
	}
	
	public BlockDataMap readReference(Chunk chunk) throws IOException, ClassNotFoundException
	{
		return super.readReference(parameterizedReadable.getReadable(chunk));
	}
}
