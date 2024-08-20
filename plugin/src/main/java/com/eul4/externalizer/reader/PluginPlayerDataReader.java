package com.eul4.externalizer.reader;

import com.eul4.common.exception.InvalidVersionException;
import com.eul4.common.externalizer.reader.ObjectReader;
import com.eul4.common.type.player.ObjectType;
import com.eul4.common.type.player.Readers;
import com.eul4.common.wrapper.Readable;
import com.eul4.common.wrapper.Reader;
import com.eul4.model.playerdata.PluginPlayerData;
import com.eul4.type.player.PluginObjectType;
import com.eul4.wrapper.Tag;
import lombok.Getter;

import java.io.IOException;

@Getter
public class PluginPlayerDataReader extends ObjectReader<PluginPlayerData>
{
	private final Reader<PluginPlayerData> reader;
	private final Readable<PluginPlayerData> readable;
	
	public PluginPlayerDataReader(Readers readers) throws InvalidVersionException
	{
		super(readers, PluginPlayerData.class);
		
		final ObjectType objectType = PluginObjectType.PLUGIN_PLAYER_DATA;
		final byte version = readers.getVersions().get(objectType);
		
		switch(version)
		{
		case 0:
			this.reader = Reader.identity();
			this.readable = this::readableVersion0;
			break;
		case 1:
			this.reader = Reader.identity();
			this.readable = this::readableVersion1;
			break;
		case 2:
			this.reader = Reader.identity();
			this.readable = this::readableVersion2;
			break;
		default:
			throw new InvalidVersionException("Invalid " + objectType + " version: " + version);
		}
	}
	
	private PluginPlayerData readableVersion0() throws IOException, ClassNotFoundException
	{
		final Tag tag = readers.getReader(TagReader.class).readReference();
		
		return PluginPlayerData.builder()
				.tag(tag)
				.build();
	}
	
	private PluginPlayerData readableVersion1() throws IOException, ClassNotFoundException
	{
		final Tag tag = readers.getReader(TagReader.class).readReference();
		final boolean tagHidden = in.readBoolean();
		
		return PluginPlayerData.builder()
				.tag(tag)
				.tagHidden(tagHidden)
				.build();
	}
	
	private PluginPlayerData readableVersion2() throws IOException, ClassNotFoundException
	{
		final Tag tag = readers.getReader(TagReader.class).readReference();
		final boolean tagHidden = in.readBoolean();
		final boolean newCombat = in.readBoolean();
		
		return PluginPlayerData.builder()
				.tag(tag)
				.tagHidden(tagHidden)
				.newCombat(newCombat)
				.build();
	}
	
	public PluginPlayerData readReference() throws IOException, ClassNotFoundException
	{
		return super.readReference(readable);
	}
}
