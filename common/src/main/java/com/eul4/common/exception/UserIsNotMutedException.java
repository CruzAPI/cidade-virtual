package com.eul4.common.exception;

import com.eul4.common.i18n.CommonMessage;
import lombok.Getter;
import org.bukkit.OfflinePlayer;

public class UserIsNotMutedException extends CommonException
{
	@Getter
	private final OfflinePlayer offlinePlayer;
	
	public UserIsNotMutedException(OfflinePlayer offlinePlayer)
	{
		this(offlinePlayer, null);
	}
	
	protected UserIsNotMutedException(OfflinePlayer offlinePlayer, Throwable cause)
	{
		super(CommonMessage.EXCEPTION_USER_IS_NOT_MUTED.withArgs(offlinePlayer.getName()), cause);
		this.offlinePlayer = offlinePlayer;
	}
}
