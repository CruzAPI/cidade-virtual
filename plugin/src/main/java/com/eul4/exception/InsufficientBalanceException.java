package com.eul4.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class InsufficientBalanceException extends Exception
{
	private final int like;
	private final int dislike;
	
	public boolean isMissingLikes()
	{
		return like > 0;
	}
	
	public boolean isMissingDislikes()
	{
		return dislike > 0;
	}
}
