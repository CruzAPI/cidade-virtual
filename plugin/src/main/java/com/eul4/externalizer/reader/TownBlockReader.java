package com.eul4.externalizer.reader;

import com.eul4.Versions;
import com.eul4.common.exception.InvalidVersionException;
import com.eul4.common.externalizer.reader.ObjectReader;
import com.eul4.common.wrapper.ParameterizedReadable;
import com.eul4.common.wrapper.Readable;
import com.eul4.common.wrapper.Reader;
import com.eul4.model.craft.town.CraftTownBlock;
import com.eul4.model.town.Town;
import com.eul4.model.town.TownBlock;

import java.io.IOException;
import java.io.ObjectInput;

public class TownBlockReader extends ObjectReader<TownBlock>
{
	private final Reader<TownBlock> reader;
	private final ParameterizedReadable<TownBlock, Town> parameterizedReadable;
	
	public TownBlockReader(ObjectInput in, Versions versions) throws InvalidVersionException
	{
		super(in, versions);
		
		if(versions.getTownBlockVersion() == 0)
		{
			this.reader = this::readerVersion0;
			this.parameterizedReadable = this::parameterizedReadableVersion0;
		}
		else
		{
			throw new InvalidVersionException("Invalid TownBlock version: " + versions.getTownBlockVersion());
		}
	}
	
	private TownBlock readerVersion0(TownBlock townBlock)
	{
		return townBlock;
	}
	
	private Readable<TownBlock> parameterizedReadableVersion0(Town town)
	{
		return () -> new CraftTownBlock(town,
				town.getPlugin().getTownWorld().getBlockAt(in.readInt(), Town.Y, in.readInt()),
				in.readBoolean());
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
