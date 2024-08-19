package com.eul4.common.exception;

import com.eul4.common.i18n.CommonMessage;
import lombok.Getter;

public class PlayerNotFoundException extends CommonException
{
	@Getter
	private final String playerName;
	
	public PlayerNotFoundException(String playerName)
	{
		this(playerName, null);
	}
	
	protected PlayerNotFoundException(String playerName, Throwable cause)
	{
		super(CommonMessage.EXCEPTION_PLAYER_NOT_FOUND.withArgs(playerName), cause);
		this.playerName = playerName;
	}
}
