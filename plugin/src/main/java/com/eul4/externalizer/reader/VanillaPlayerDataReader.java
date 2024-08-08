package com.eul4.externalizer.reader;

import com.eul4.Main;
import com.eul4.common.exception.InvalidVersionException;
import com.eul4.common.externalizer.reader.ObjectReader;
import com.eul4.common.type.player.ObjectType;
import com.eul4.common.type.player.Readers;
import com.eul4.common.wrapper.ParameterizedReadable;
import com.eul4.common.wrapper.Readable;
import com.eul4.common.wrapper.Reader;
import com.eul4.model.playerdata.VanillaPlayerData;
import com.eul4.type.player.PluginObjectType;
import com.eul4.wrapper.HomeMap;
import lombok.Getter;

import java.io.IOException;

@Getter
public class VanillaPlayerDataReader extends ObjectReader<VanillaPlayerData>
{
	private final Reader<VanillaPlayerData> reader;
	private final ParameterizedReadable<VanillaPlayerData, Main> parameterizedReadable;
	
	public VanillaPlayerDataReader(Readers readers) throws InvalidVersionException
	{
		super(readers, VanillaPlayerData.class);
		
		final ObjectType objectType = PluginObjectType.VANILLA_PLAYER_DATA;
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
	
	private Readable<VanillaPlayerData> parameterizedReadableVersion0(Main plugin)
	{
		return () ->
		{
			HomeMap homeMap = readers.getReader(HomeMapReader.class).readReference(plugin);
			
			return VanillaPlayerData.builder()
					.homeMap(homeMap)
					.build();
		};
	}
	
	public VanillaPlayerData readReference(Main plugin) throws IOException, ClassNotFoundException
	{
		return super.readReference(parameterizedReadable.getReadable(plugin));
	}
}
