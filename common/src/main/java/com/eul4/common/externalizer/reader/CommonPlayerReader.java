package com.eul4.common.externalizer.reader;

import com.eul4.common.Common;
import com.eul4.common.exception.InvalidVersionException;
import com.eul4.common.model.data.CommonPlayerData;
import com.eul4.common.model.player.CommonPlayer;
import com.eul4.common.type.player.CommonObjectType;
import com.eul4.common.type.player.Readers;
import com.eul4.common.type.player.ObjectType;
import com.eul4.common.wrapper.Reader;
import lombok.Getter;
import org.bukkit.entity.Player;

import java.io.IOException;

public abstract class CommonPlayerReader<P extends CommonPlayer> extends ObjectReader<P>
{
	@Getter
	private final Reader<P> reader;
	
	public CommonPlayerReader(Readers readers, Class<P> type) throws InvalidVersionException
	{
		super(readers, type);
		
		final ObjectType objectType = CommonObjectType.COMMON_PLAYER;
		final byte version = readers.getVersions().get(objectType);
		
		switch(version)
		{
		case 0:
			this.reader = this::readerVersion0;
			break;
		default:
			throw new InvalidVersionException("Invalid " + objectType + " version: " + version);
		}
	}
	
	private P readerVersion0(P commonPlayer) throws IOException, ClassNotFoundException
	{
		CommonPlayerData commonPlayerData = readers.getReader(CommonPlayerDataReader.class).readReference(commonPlayer.getPlugin());
		
		commonPlayer.setCommonPlayerData(commonPlayerData);
		
		return commonPlayer;
	}
	
	public abstract P readReference(Player player, Common plugin) throws IOException, ClassNotFoundException;
}
