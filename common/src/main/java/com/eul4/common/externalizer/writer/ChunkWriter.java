package com.eul4.common.externalizer.writer;

import com.eul4.common.type.player.Writers;
import org.bukkit.Chunk;

import java.io.IOException;
import java.util.UUID;

public class ChunkWriter extends ObjectWriter<Chunk>
{
	public ChunkWriter(Writers writers)
	{
		super(writers, Chunk.class);
	}
	
	@Override
	protected void writeObject(Chunk chunk) throws IOException
	{
		UUID uuid = chunk.getWorld().getUID();
		
		out.writeLong(uuid.getMostSignificantBits());
		out.writeLong(uuid.getLeastSignificantBits());
		
		out.writeInt(chunk.getX());
		out.writeInt(chunk.getZ());
	}
}
