package com.eul4.common.type.player;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum CommonObjectType implements ObjectType
{
	BLOCK((byte) 0, CommonExternalizerType.BLOCK),
	BLOCK_VECTOR((byte) 0, CommonExternalizerType.BLOCK_VECTOR),
	CHUNK((byte) 0, CommonExternalizerType.CHUNK),
	COMMON_PLAYER_DATA((byte) 0, CommonExternalizerType.COMMON_PLAYER_DATA),
	COMMON_PLAYER((byte) 0, null),
	ENTITY((byte) 0, CommonExternalizerType.ENTITY),
	HOLOGRAM((byte) 0, CommonExternalizerType.HOLOGRAM),
	INVENTORY((byte) 0, CommonExternalizerType.INVENTORY),
	ITEM_STACK((byte) 0, CommonExternalizerType.ITEM_STACK),
	LOCATION((byte) 0, CommonExternalizerType.LOCATION),
	OBJECT((byte) 0, null),
	PLAYER_DATA((byte) 0, CommonExternalizerType.PLAYER_DATA),
	POTION_EFFECT_COLLECTION((byte) 0, CommonExternalizerType.POTION_EFFECT_COLLECTION),
	POTION_EFFECT((byte) 0, CommonExternalizerType.POTION_EFFECT),
	TRANSLATED_HOLOGRAM_LINE((byte) 0, CommonExternalizerType.TRANSLATED_HOLOGRAM_LINE);
	
	private final byte version;
	private final CommonExternalizerType externalizerType;
}
