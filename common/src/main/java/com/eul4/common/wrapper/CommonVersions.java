package com.eul4.common.wrapper;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class CommonVersions
{
	public static final byte COMMON_PLAYER_DATA = 0;
	public static final byte COMMON_PLAYER = 0;
	public static final byte INVENTORY = 0;
	public static final byte ITEM_STACK = 0;
	public static final byte LOCATION = 0;
	public static final byte OBJECT = 0;
	public static final byte PLAYER_DATA = 0;
	public static final byte POTION_EFFECT_COLLECTION = 0;
	public static final byte POTION_EFFECT = 0;
	
	private Byte commonPlayerDataVersion;
	private Byte commonPlayerVersion;
	private Byte inventoryVersion;
	private Byte itemStackVersion;
	private Byte locationVersion;
	private Byte objectVersion;
	private Byte playerDataVersion;
	private Byte potionEffectCollectionVersion;
	private Byte potionEffectVersion;
}
