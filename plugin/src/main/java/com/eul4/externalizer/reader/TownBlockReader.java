package com.eul4.externalizer.reader;

import com.eul4.common.exception.InvalidVersionException;
import com.eul4.common.externalizer.reader.ObjectReader;
import com.eul4.common.type.player.Readers;
import com.eul4.common.type.player.ObjectType;
import com.eul4.common.wrapper.ParameterizedReadable;
import com.eul4.common.wrapper.Readable;
import com.eul4.common.wrapper.Reader;
import com.eul4.model.craft.town.CraftTownBlock;
import com.eul4.model.town.Town;
import com.eul4.model.town.TownBlock;
import com.eul4.type.player.PluginObjectType;

import java.io.IOException;

public class TownBlockReader extends ObjectReader<TownBlock>
{
	private final Reader<TownBlock> reader;
	private final ParameterizedReadable<TownBlock, Town> parameterizedReadable;
	
	public TownBlockReader(Readers readers) throws InvalidVersionException
	{
		super(readers, TownBlock.class);
		
		final ObjectType objectType = PluginObjectType.TOWN_BLOCK;
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
	
	private TownBlock readerVersion0(TownBlock townBlock) throws IOException, ClassNotFoundException
	{
		townBlock.setStructure(readers.getReader(GenericStructureReader.class).readReference(townBlock.getTown()));
		return townBlock;
	}
	
	private Readable<TownBlock> parameterizedReadableVersion0(Town town)
	{
		return () ->
		{
			final int position = in.readUnsignedShort();
			final int relativeX = position % Town.TOWN_FULL_DIAMATER - Town.TOWN_FULL_RADIUS;
			final int relativeZ = position / Town.TOWN_FULL_DIAMATER - Town.TOWN_FULL_RADIUS;
			
			return new CraftTownBlock(town,
				town.getBlock().getRelative(relativeX, 0, relativeZ),
				in.readBoolean());
		};
	}
	
	public TownBlock readReference(Town town) throws IOException, ClassNotFoundException
	{
		return super.readReference(parameterizedReadable.getReadable(town));
	}
	
	@Override
	protected TownBlock readObject(TownBlock townBlock) throws IOException, ClassNotFoundException
	{
		return reader.readObject(townBlock);
	}
}
