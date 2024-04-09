package com.eul4.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class UpgradeLockedException extends Exception
{
	private final int unlocksTownHallLevel;
}
