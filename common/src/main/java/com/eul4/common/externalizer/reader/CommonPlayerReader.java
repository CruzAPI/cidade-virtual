package com.eul4.common.externalizer.reader;

import com.eul4.common.Common;
import com.eul4.common.exception.InvalidVersionException;
import com.eul4.common.model.data.CommonPlayerData;
import com.eul4.common.model.player.CommonPlayer;
import com.eul4.common.wrapper.CommonVersions;
import com.eul4.common.wrapper.Reader;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.io.ObjectInput;

public abstract class CommonPlayerReader<P extends CommonPlayer> extends ObjectReader<P>
{
	private final Reader<P> reader;
	
	private final CommonPlayerDataReader commonPlayerDataReader;
	
	public CommonPlayerReader(ObjectInput in, CommonVersions commonVersions) throws InvalidVersionException
	{
		super(in, commonVersions);
		
		this.commonPlayerDataReader = new CommonPlayerDataReader(in, commonVersions);
		
		if(commonVersions.getCommonPlayerVersion() == 0)
		{
			this.reader = this::readerVersion0;
		}
		else
		{
			throw new InvalidVersionException("Invalid CommonPlayer version: " + commonVersions.getCommonPlayerVersion());
		}
	}
	
	private P readerVersion0(P commonPlayer) throws IOException, ClassNotFoundException
	{
		CommonPlayerData commonPlayerData = commonPlayerDataReader.readReference(commonPlayer.getPlugin());
		
		commonPlayer.setCommonPlayerData(commonPlayerData);
		
		return commonPlayer;
	}
	
	public abstract P readReference(Player player, Common plugin) throws IOException, ClassNotFoundException;
	
	@Override
	protected P readObject(P commonPlayer) throws IOException, ClassNotFoundException
	{
		return reader.readObject(commonPlayer);
	}
}
