package com.eul4.externalizer.reader;

import com.eul4.Main;
import com.eul4.common.exception.InvalidVersionException;
import com.eul4.common.externalizer.reader.ObjectReader;
import com.eul4.common.type.player.ObjectType;
import com.eul4.common.type.player.Readers;
import com.eul4.common.wrapper.ParameterizedReadable;
import com.eul4.common.wrapper.Readable;
import com.eul4.common.wrapper.Reader;
import com.eul4.holder.UnlimitedCrownHolder;
import com.eul4.type.player.PluginObjectType;
import com.eul4.wrapper.CrownInfo;
import lombok.Getter;

import java.io.IOException;

public class CrownInfoReader extends ObjectReader<CrownInfo>
{
	@Getter
	private final Reader<CrownInfo> reader;
	private final ParameterizedReadable<CrownInfo, Main> parameterizedReadable;
	
	public CrownInfoReader(Readers readers) throws InvalidVersionException
	{
		super(readers, CrownInfo.class);
		
		final ObjectType objectType = PluginObjectType.CROWN_INFO;
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
	
	private Readable<CrownInfo> parameterizedReadableVersion0(Main plugin)
	{
		return () ->
		{
			UnlimitedCrownHolderReader unlimitedCrownHolderReader = readers
					.getReader(UnlimitedCrownHolderReader.class);
			
			UnlimitedCrownHolder serverTreasure = unlimitedCrownHolderReader.readReference(plugin);
			UnlimitedCrownHolder jackpot = unlimitedCrownHolderReader.readReference(plugin);
			UnlimitedCrownHolder townHallVault = unlimitedCrownHolderReader.readReference(plugin);
			UnlimitedCrownHolder eul4Insights = unlimitedCrownHolderReader.readReference(plugin);
			
			return new CrownInfo(serverTreasure, jackpot, townHallVault, eul4Insights);
		};
	}
	
	public CrownInfo readReference(Main plugin) throws IOException, ClassNotFoundException
	{
		return super.readReference(parameterizedReadable.getReadable(plugin));
	}
}
