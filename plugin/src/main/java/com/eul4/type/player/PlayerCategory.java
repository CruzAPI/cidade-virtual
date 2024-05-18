package com.eul4.type.player;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PlayerCategory
{
	PHYSICAL(PhysicalPlayerType.values()),
	SPIRITUAL(SpiritualPlayerType.values()),
	;
	
	private final PluginPlayerType[] enumValues;
}
