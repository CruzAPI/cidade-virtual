package com.eul4.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class StructureLimitException extends Exception
{
	private final int count;
	private final int limit;
}
