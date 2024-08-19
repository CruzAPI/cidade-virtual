package com.eul4.common.exception;

import com.eul4.common.i18n.CommonMessage;
import lombok.Getter;
import org.bukkit.OfflinePlayer;

public class UserAlreadyMutedException extends CommonException
{
	@Getter
	private final OfflinePlayer offlinePlayer;
	
	public UserAlreadyMutedException(OfflinePlayer offlinePlayer)
	{
		this(offlinePlayer, null);
	}
	
	protected UserAlreadyMutedException(OfflinePlayer offlinePlayer, Throwable cause)
	{
		super(CommonMessage.EXCEPTION_USER_ALREADY_MUTED.withArgs(offlinePlayer.getName()), cause);
		this.offlinePlayer = offlinePlayer;
	}
}
