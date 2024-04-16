package com.eul4.service;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Getter
@Setter
@ToString
public class BlockData implements Serializable
{
	public static final long SERIAL_VERSION_UID = 1L;
	
	@Accessors(fluent = true)
	private boolean hasHardness;
}
