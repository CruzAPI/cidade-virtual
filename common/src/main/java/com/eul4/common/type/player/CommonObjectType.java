package com.eul4.common.type.player;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum CommonObjectType implements ObjectType
{
	BIG_DECIMAL((byte) 0, CommonExternalizerType.BIG_DECIMAL),
	BLOCK((byte) 0, CommonExternalizerType.BLOCK),
	BLOCK_VECTOR((byte) 0, CommonExternalizerType.BLOCK_VECTOR),
	CHUNK((byte) 0, CommonExternalizerType.CHUNK),
	COMMON_PLAYER((byte) 0, null),
	COMMON_PLAYER_DATA((byte) 2, CommonExternalizerType.COMMON_PLAYER_DATA),
	ENTITY((byte) 0, CommonExternalizerType.ENTITY),
	GROUP((byte) 1, CommonExternalizerType.GROUP),
	GROUP_GROUP((byte) 0, CommonExternalizerType.GROUP_GROUP),
	GROUP_GROUP_MAP((byte) 0, CommonExternalizerType.GROUP_GROUP_MAP),
	GROUP_MAP((byte) 0, CommonExternalizerType.GROUP_MAP),
	GROUP_USER((byte) 0, CommonExternalizerType.GROUP_USER),
	GROUP_USER_MAP((byte) 0, CommonExternalizerType.GROUP_USER_MAP),
	HOLOGRAM((byte) 0, CommonExternalizerType.HOLOGRAM),
	INVENTORY((byte) 0, CommonExternalizerType.INVENTORY),
	ITEM_STACK((byte) 0, CommonExternalizerType.ITEM_STACK),
	LOCATION((byte) 0, CommonExternalizerType.LOCATION),
	OBJECT((byte) 0, null),
	PERMISSION((byte) 0, CommonExternalizerType.PERMISSION),
	PERMISSION_MAP((byte) 0, CommonExternalizerType.PERMISSION_MAP),
	PLAYER_DATA((byte) 0, CommonExternalizerType.PLAYER_DATA),
	POTION_EFFECT((byte) 1, CommonExternalizerType.POTION_EFFECT),
	POTION_EFFECT_COLLECTION((byte) 0, CommonExternalizerType.POTION_EFFECT_COLLECTION),
	TIMED_TICK((byte) 0, CommonExternalizerType.TIMED_TICK),
	TRANSLATED_HOLOGRAM_LINE((byte) 1, CommonExternalizerType.TRANSLATED_HOLOGRAM_LINE),
	USER((byte) 0, CommonExternalizerType.USER),
	UUID((byte) 0, CommonExternalizerType.UUID),
	UUID_HASH_SET((byte) 0, CommonExternalizerType.UUID_HASH_SET);
	
	private final byte version;
	private final CommonExternalizerType externalizerType;
}
