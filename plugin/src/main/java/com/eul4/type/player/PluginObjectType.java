package com.eul4.type.player;

import com.eul4.common.type.player.ObjectType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum PluginObjectType implements ObjectType
{
	ADMIN((byte) 0, PluginExternalizerType.ADMIN),
	ATTACKER((byte) 0, PluginExternalizerType.ATTACKER),
	DEPOSIT((byte) 0, null),
	DISLIKE_DEPOSIT((byte) 0, PluginExternalizerType.DISLIKE_DEPOSIT),
	DISLIKE_GENERATOR((byte) 0, PluginExternalizerType.DISLIKE_GENERATOR),
	GENERATOR((byte) 0, null),
	GENERIC_PLUGIN_PLAYER((byte) 0, PluginExternalizerType.GENERIC_PLUGIN_PLAYER),
	GENERIC_STRUCTURE((byte) 0, PluginExternalizerType.GENERIC_STRUCTURE),
	LIKE_DEPOSIT((byte) 0, PluginExternalizerType.LIKE_DEPOSIT),
	LIKE_GENERATOR((byte) 0, PluginExternalizerType.LIKE_GENERATOR),
	PLUGIN_PLAYER((byte) 0, null),
	RAID_ANALYZER((byte) 0, PluginExternalizerType.RAID_ANALYZER),
	STRUCTURE((byte) 0, null),
	STRUCTURE_SET((byte) 0, PluginExternalizerType.STRUCTURE_SET),
	TOWN_BLOCK_MAP((byte) 0, PluginExternalizerType.TOWN_BLOCK_MAP),
	TOWN_BLOCK((byte) 0, PluginExternalizerType.TOWN_BLOCK),
	TOWN_HALL((byte) 0, PluginExternalizerType.TOWN_HALL),
	TOWN_MAP((byte) 0, PluginExternalizerType.TOWN_MAP),
	TOWN_PLAYER_DATA((byte) 0, PluginExternalizerType.TOWN_PLAYER_DATA),
	TOWN_PLAYER((byte) 0, PluginExternalizerType.TOWN_PLAYER),
	TOWN((byte) 0, PluginExternalizerType.TOWN),
	TOWN_TILE_MAP((byte) 0, PluginExternalizerType.TOWN_TILE_MAP),
	TOWN_TILE((byte) 0, PluginExternalizerType.TOWN_TILE);
	
	private final byte version;
	private final PluginExternalizerType externalizerType;
}
