package com.eul4.common.externalizer.reader;

import com.eul4.common.exception.InvalidVersionException;
import com.eul4.common.model.data.CommonPlayerData;
import com.eul4.common.model.data.PlayerData;
import com.eul4.common.type.player.CommonObjectType;
import com.eul4.common.type.player.ObjectType;
import com.eul4.common.type.player.Readers;
import com.eul4.common.wrapper.ParameterizedReadable;
import com.eul4.common.wrapper.Readable;
import com.eul4.common.wrapper.Reader;
import com.eul4.common.wrapper.UUIDHashSet;
import lombok.Getter;
import org.bukkit.plugin.Plugin;

import java.io.IOException;
import java.util.UUID;

public class CommonPlayerDataReader extends ObjectReader<CommonPlayerData>
{
	@Getter
	private final Reader<CommonPlayerData> reader;
	private final ParameterizedReadable<CommonPlayerData, Plugin> parameterizedReadable;
	
	public CommonPlayerDataReader(Readers readers) throws InvalidVersionException
	{
		super(readers, CommonPlayerData.class);
		
		final ObjectType objectType = CommonObjectType.COMMON_PLAYER_DATA;
		final byte version = readers.getVersions().get(objectType);
		
		switch(version)
		{
		case 0:
			this.reader = Reader.identity();
			this.parameterizedReadable = this::parameterizedReadableVersion0;
			break;
		case 1:
			this.reader = Reader.identity();
			this.parameterizedReadable = this::parameterizedReadableVersion1;
			break;
		case 2:
			this.reader = Reader.identity();
			this.parameterizedReadable = this::parameterizedReadableVersion2;
			break;
		default:
			throw new InvalidVersionException("Invalid " + objectType + " version: " + version);
		}
	}
	
	private Readable<CommonPlayerData> parameterizedReadableVersion0(Plugin plugin) throws IOException, ClassNotFoundException
	{
		return () ->
		{
			PlayerData playerData = readers.getReader(PlayerDataReader.class).readReference(plugin);
			
			return CommonPlayerData.builder()
					.playerData(playerData)
					.build();
		};
	}
	
	private Readable<CommonPlayerData> parameterizedReadableVersion1(Plugin plugin) throws IOException, ClassNotFoundException
	{
		return () ->
		{
			PlayerData playerData = readers.getReader(PlayerDataReader.class).readReference(plugin);
			boolean scoreboardEnabled = in.readBoolean();
			
			return CommonPlayerData.builder()
					.playerData(playerData)
					.scoreboardEnabled(scoreboardEnabled)
					.build();
		};
	}
	
	private Readable<CommonPlayerData> parameterizedReadableVersion2(Plugin plugin) throws IOException, ClassNotFoundException
	{
		return () ->
		{
			PlayerData playerData = readers.getReader(PlayerDataReader.class).readReference(plugin);
			boolean scoreboardEnabled = in.readBoolean();
			boolean chatEnabled = in.readBoolean();
			boolean tellEnabled = in.readBoolean();
			UUIDHashSet ignoredPlayers = readers.getReader(UUIDHashSetReader.class).readReference();
			UUID lastReplied = readers.getReader(UUIDReader.class).readReference();
			
			return CommonPlayerData.builder()
					.playerData(playerData)
					.scoreboardEnabled(scoreboardEnabled)
					.chatEnabled(chatEnabled)
					.tellEnabled(tellEnabled)
					.ignoredPlayers(ignoredPlayers)
					.lastReplied(lastReplied)
					.build();
		};
	}
	
	public CommonPlayerData readReference(Plugin plugin) throws IOException, ClassNotFoundException
	{
		return super.readReference(parameterizedReadable.getReadable(plugin));
	}
}
