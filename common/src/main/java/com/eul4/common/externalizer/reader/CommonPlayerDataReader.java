package com.eul4.common.externalizer.reader;

import com.eul4.common.Common;
import com.eul4.common.exception.InvalidVersionException;
import com.eul4.common.model.data.CommonPlayerData;
import com.eul4.common.model.data.PlayerData;
import com.eul4.common.wrapper.CommonVersions;
import com.eul4.common.wrapper.Reader;

import java.io.IOException;
import java.io.ObjectInput;

public class CommonPlayerDataReader extends ObjectReader<CommonPlayerData>
{
	private final Reader<CommonPlayerData> reader;
	
	private final PlayerDataReader playerDataReader;
	
	public CommonPlayerDataReader(ObjectInput in, CommonVersions commonVersions, Common plugin) throws InvalidVersionException
	{
		super(in, commonVersions);
		
		this.playerDataReader = new PlayerDataReader(in, commonVersions, plugin);
		
		if(commonVersions.getCommonPlayerDataVersion() == 0)
		{
			this.reader = this::readerVersion0;
		}
		else
		{
			throw new InvalidVersionException("Invalid CommonPlayerData version: " + commonVersions.getCommonPlayerDataVersion());
		}
	}
	
	private CommonPlayerData readerVersion0() throws IOException, ClassNotFoundException
	{
		PlayerData playerData = playerDataReader.readReference();
		
		return CommonPlayerData.builder()
				.playerData(playerData)
				.build();
	}
	
	@Override
	protected CommonPlayerData readObject() throws IOException, ClassNotFoundException
	{
		return reader.readObject();
	}
}
