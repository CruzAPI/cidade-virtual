package com.eul4.externalizer.reader;

import com.eul4.common.exception.InvalidVersionException;
import com.eul4.common.externalizer.reader.ObjectReader;
import com.eul4.common.type.player.ObjectType;
import com.eul4.common.type.player.Readers;
import com.eul4.common.wrapper.ParameterizedReadable;
import com.eul4.common.wrapper.Readable;
import com.eul4.common.wrapper.Reader;
import com.eul4.model.town.Town;
import com.eul4.model.town.TownBlock;
import com.eul4.type.player.PluginObjectType;
import com.eul4.wrapper.BoughtTileMapByDepth;
import lombok.Getter;

import java.io.IOException;

@Getter
public class BoughtTileMapByDepthReader extends ObjectReader<BoughtTileMapByDepth>
{
	private final Reader<BoughtTileMapByDepth> reader;
	private final ParameterizedReadable<BoughtTileMapByDepth, Town> parameterizedReadable;
	
	public BoughtTileMapByDepthReader(Readers readers) throws InvalidVersionException
	{
		super(readers, BoughtTileMapByDepth.class);
		
		final ObjectType objectType = PluginObjectType.BOUGHT_TILE_MAP_BY_DEPTH;
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
	
	private Readable<BoughtTileMapByDepth> parameterizedReadableVersion0(Town town)
	{
		return () ->
		{
			BoughtTileMapByDepth boughtTileMapByDepth = new BoughtTileMapByDepth(town.getOwnerUUID());
			
			int size = in.readInt();
			
			for(int i = 0; i < size; i++)
			{
				final int depth = in.readInt();
				final int amount = in.readInt();
				
				boughtTileMapByDepth.put(depth, amount);
			}
			
			return boughtTileMapByDepth;
		};
	}
	
	public BoughtTileMapByDepth readReference(Town town) throws IOException, ClassNotFoundException
	{
		return super.readReference(parameterizedReadable.getReadable(town));
	}
}
