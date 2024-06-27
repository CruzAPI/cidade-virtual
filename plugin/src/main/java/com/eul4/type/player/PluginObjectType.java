package com.eul4.type.player;

import com.eul4.common.type.player.ObjectType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum PluginObjectType implements ObjectType
{
	ADMIN((byte) 0, PluginExternalizerType.ADMIN),
	ARMORY((byte) 0, PluginExternalizerType.ARMORY),
	ATTACKER((byte) 0, PluginExternalizerType.ATTACKER),
	BLOCK_DATA_MAP((byte) 0, PluginExternalizerType.BLOCK_DATA_MAP),
	BLOCK_DATA((byte) 0, PluginExternalizerType.BLOCK_DATA),
	CANNON((byte) 0, PluginExternalizerType.CANNON),
	DEFENDER((byte) 0, PluginExternalizerType.DEFENDER),
	DEFENDER_SPECTATOR((byte) 0, PluginExternalizerType.DEFENDER_SPECTATOR),
	DEPOSIT((byte) 0, null),
	DISLIKE_DEPOSIT((byte) 0, PluginExternalizerType.DISLIKE_DEPOSIT),
	DISLIKE_GENERATOR((byte) 0, PluginExternalizerType.DISLIKE_GENERATOR),
	GENERATOR((byte) 0, null),
	GENERIC_PLUGIN_PLAYER((byte) 0, PluginExternalizerType.GENERIC_PLUGIN_PLAYER),
	GENERIC_STRUCTURE((byte) 0, PluginExternalizerType.GENERIC_STRUCTURE),
	INVENTORY_ORGANIZER_PLAYER((byte) 0, PluginExternalizerType.INVENTORY_ORGANIZER_PLAYER),
	LIKE_DEPOSIT((byte) 0, PluginExternalizerType.LIKE_DEPOSIT),
	LIKE_GENERATOR((byte) 0, PluginExternalizerType.LIKE_GENERATOR),
	PHYSICAL_PLAYER((byte) 0, null),
	PLUGIN_PLAYER((byte) 0, null),
	RAID_ANALYZER((byte) 0, PluginExternalizerType.RAID_ANALYZER),
	RAID_SPECTATOR((byte) 0, PluginExternalizerType.RAID_SPECTATOR),
	SHORT_COORDINATE_BLOCK_CHUNK((byte) 0, PluginExternalizerType.SHORT_COORDINATE_BLOCK_CHUNK),
	SPIRITUAL_PLAYER((byte) 0, null),
	STRUCTURE((byte) 0, null),
	STRUCTURE_SET((byte) 0, PluginExternalizerType.STRUCTURE_SET),
	TOWN_BLOCK_MAP((byte) 0, PluginExternalizerType.TOWN_BLOCK_MAP),
	TOWN_BLOCK((byte) 0, PluginExternalizerType.TOWN_BLOCK),
	TOWN_BLOCK_SET((byte) 0, PluginExternalizerType.TOWN_BLOCK_SET),
	TOWN_HALL((byte) 0, PluginExternalizerType.TOWN_HALL),
	TOWN_MAP((byte) 0, PluginExternalizerType.TOWN_MAP),
	TOWN_PLAYER_DATA((byte) 0, PluginExternalizerType.TOWN_PLAYER_DATA),
	TOWN_PLAYER((byte) 0, PluginExternalizerType.TOWN_PLAYER),
	TOWN((byte) 0, PluginExternalizerType.TOWN),
	TOWN_TILE_MAP((byte) 0, PluginExternalizerType.TOWN_TILE_MAP),
	TOWN_TILE((byte) 0, PluginExternalizerType.TOWN_TILE),
	VANILLA_PLAYER((byte) 0, PluginExternalizerType.VANILLA_PLAYER),
	;
	
	private final byte version;
	private final PluginExternalizerType externalizerType;
}
