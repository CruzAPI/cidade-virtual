package com.eul4.common.externalizer.reader;

import com.eul4.common.exception.InvalidVersionException;
import com.eul4.common.model.data.CommonPlayerData;
import com.eul4.common.model.data.PlayerData;
import com.eul4.common.wrapper.CommonVersions;
import com.eul4.common.wrapper.ParameterizedReadable;
import com.eul4.common.wrapper.Readable;
import com.eul4.common.wrapper.Reader;
import org.bukkit.plugin.Plugin;

import java.io.IOException;
import java.io.ObjectInput;

public class CommonPlayerDataReader extends ObjectReader<CommonPlayerData>
{
	private final Reader<CommonPlayerData> reader;
	private final ParameterizedReadable<CommonPlayerData, Plugin> parameterizedReadable;
	
	private final PlayerDataReader playerDataReader;
	
	public CommonPlayerDataReader(ObjectInput in, CommonVersions commonVersions) throws InvalidVersionException
	{
		super(in, commonVersions);
		
		this.playerDataReader = new PlayerDataReader(in, commonVersions);
		
		switch(commonVersions.getCommonPlayerDataVersion())
		{
		case 0:
			this.reader = Reader.identity();
			this.parameterizedReadable = this::parameterizedReadableVersion0;
			break;
		default:
			throw new InvalidVersionException("Invalid CommonPlayerData version: " + commonVersions.getCommonPlayerDataVersion());
		}
	}
	
	private Readable<CommonPlayerData> parameterizedReadableVersion0(Plugin plugin) throws IOException, ClassNotFoundException
	{
		return () ->
		{
			PlayerData playerData = playerDataReader.readReference(plugin);
			
			return CommonPlayerData.builder()
					.playerData(playerData)
					.build();
		};
	}
	
	public CommonPlayerData readReference(Plugin plugin) throws IOException, ClassNotFoundException
	{
		return super.readReference(parameterizedReadable.getReadable(plugin));
	}
	
	@Override
	protected CommonPlayerData readObject(CommonPlayerData commonPlayerData) throws IOException, ClassNotFoundException
	{
		return reader.readObject(commonPlayerData);
	}
}
