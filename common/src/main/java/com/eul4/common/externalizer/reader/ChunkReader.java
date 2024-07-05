package com.eul4.common.externalizer.reader;

import com.eul4.common.exception.InvalidVersionException;
import com.eul4.common.type.player.CommonObjectType;
import com.eul4.common.type.player.ObjectType;
import com.eul4.common.type.player.Readers;
import com.eul4.common.wrapper.ParameterizedReadable;
import com.eul4.common.wrapper.Readable;
import com.eul4.common.wrapper.Reader;
import lombok.Getter;
import org.bukkit.Chunk;
import org.bukkit.plugin.Plugin;

import java.io.IOException;
import java.util.UUID;

public class ChunkReader extends ObjectReader<Chunk>
{
	@Getter
	private final Reader<Chunk> reader;
	private final ParameterizedReadable<Chunk, Plugin> parameterizedReadable;
	
	public ChunkReader(Readers readers) throws InvalidVersionException
	{
		super(readers, Chunk.class);
		
		final ObjectType objectType = CommonObjectType.CHUNK;
		final byte version = readers.getVersions().get(objectType);
		
		switch(version)
		{
		case 0:
			this.reader = Reader.identity();
			this.parameterizedReadable = this::parameterizedReadableVersion0;
			break;
		default:
			throw new InvalidVersionException("Invalid " + objectType + " version: " + version);
		}
	}
	
	private Readable<Chunk> parameterizedReadableVersion0(Plugin plugin)
	{
		return () ->
		{
			UUID uuid = new UUID(in.readLong(), in.readLong());
			
			int x = in.readInt();
			int z = in.readInt();
			
			return plugin.getServer().getWorld(uuid).getChunkAt(x, z);
		};
	}
	
	public Chunk readReference(Plugin plugin) throws IOException, ClassNotFoundException
	{
		return super.readReference(parameterizedReadable.getReadable(plugin));
	}
}
