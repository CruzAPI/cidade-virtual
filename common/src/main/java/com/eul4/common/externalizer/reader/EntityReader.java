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
import org.bukkit.entity.Entity;
import org.bukkit.plugin.Plugin;

import java.io.IOException;
import java.util.Objects;
import java.util.UUID;

public class EntityReader extends ObjectReader<Entity>
{
	@Getter
	private final Reader<Entity> reader;
	private final ParameterizedReadable<Entity, Plugin> parameterizedReadable;
	
	public EntityReader(Readers readers) throws InvalidVersionException
	{
		super(readers, Entity.class);
		
		final ObjectType objectType = CommonObjectType.ENTITY;
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
	
	private Readable<Entity> parameterizedReadableVersion0(Plugin plugin)
	{
		return () ->
		{
			UUID uuid = new UUID(in.readLong(), in.readLong());
			Chunk chunk = readers.getReader(ChunkReader.class).readReference(plugin);
			chunk.load();
			return Objects.requireNonNull(chunk.getWorld().getEntity(uuid));
		};
	}
	
	public Entity readReference(Plugin plugin) throws IOException, ClassNotFoundException
	{
		return super.readReference(parameterizedReadable.getReadable(plugin));
	}
}
