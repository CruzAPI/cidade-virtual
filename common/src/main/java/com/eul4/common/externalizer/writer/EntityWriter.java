package com.eul4.common.externalizer.writer;

import com.eul4.common.type.player.Writers;
import org.bukkit.entity.Entity;

import java.io.IOException;
import java.util.UUID;

public class EntityWriter extends ObjectWriter<Entity>
{
	public EntityWriter(Writers writers)
	{
		super(writers, Entity.class);
	}
	
	@Override
	protected void writeObject(Entity entity) throws IOException
	{
		UUID uuid = entity.getUniqueId();
		
		out.writeLong(uuid.getMostSignificantBits());
		out.writeLong(uuid.getLeastSignificantBits());
		
		writers.getWriter(ChunkWriter.class).writeReferenceNotNull(entity.getChunk());
	}
}
