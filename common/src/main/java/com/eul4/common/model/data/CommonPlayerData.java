package com.eul4.common.model.data;

import com.eul4.common.wrapper.UUIDHashSet;
import lombok.Builder;
import lombok.Data;

import java.util.Optional;
import java.util.UUID;

@Data
public class CommonPlayerData
{
	private PlayerData playerData;
	
	private boolean scoreboardEnabled;
	private boolean chatEnabled;
	private boolean tellEnabled;
	private UUIDHashSet ignoredPlayers;
	private UUID lastReplied;
	
	public CommonPlayerData()
	{
		this.scoreboardEnabled = true;
		this.chatEnabled = true;
		this.tellEnabled = true;
		this.ignoredPlayers = new UUIDHashSet();
	}
	
	@Builder
	public CommonPlayerData(PlayerData playerData,
			Boolean scoreboardEnabled,
			Boolean chatEnabled,
			Boolean tellEnabled,
			UUIDHashSet ignoredPlayers,
			UUID lastReplied)
	{
		this.playerData = playerData;
		this.scoreboardEnabled = Optional.ofNullable(scoreboardEnabled).orElse(true);
		this.chatEnabled = Optional.ofNullable(chatEnabled).orElse(true);
		this.tellEnabled = Optional.ofNullable(tellEnabled).orElse(true);
		this.ignoredPlayers = Optional.ofNullable(ignoredPlayers).orElseGet(UUIDHashSet::new);
		this.lastReplied = lastReplied;
	}
}
