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
	BOUGHT_TILE_MAP_BY_DEPTH((byte) 0, PluginExternalizerType.BOUGHT_TILE_MAP_BY_DEPTH),
	BLOCK_DATA_MAP((byte) 0, PluginExternalizerType.BLOCK_DATA_MAP),
	BLOCK_DATA((byte) 5, PluginExternalizerType.BLOCK_DATA),
	BROADCAST_HASH_SET((byte) 0, PluginExternalizerType.BROADCAST_HASH_SET),
	CANNON((byte) 0, PluginExternalizerType.CANNON),
	CAPACITATED_CROWN_HOLDER((byte) 0, PluginExternalizerType.CAPACITATED_CROWN_HOLDER),
	CHECKPOINT_STEP_ENUM((byte) 0, PluginExternalizerType.CHECKPOINT_STEP_ENUM),
	CROWN_DEPOSIT((byte) 0, PluginExternalizerType.CROWN_DEPOSIT),
	CROWN_INFO((byte) 0, PluginExternalizerType.CROWN_INFO),
	CRYPTO_INFO((byte) 0, PluginExternalizerType.CRYPTO_INFO),
	DEFENDER((byte) 0, PluginExternalizerType.DEFENDER),
	DEFENDER_SPECTATOR((byte) 0, PluginExternalizerType.DEFENDER_SPECTATOR),
	DEPOSIT((byte) 0, null),
	DISLIKE_DEPOSIT((byte) 0, PluginExternalizerType.DISLIKE_DEPOSIT),
	DISLIKE_GENERATOR((byte) 0, PluginExternalizerType.DISLIKE_GENERATOR),
	GENERATOR((byte) 0, null),
	GENERIC_PLUGIN_PLAYER((byte) 0, PluginExternalizerType.GENERIC_PLUGIN_PLAYER),
	GENERIC_STRUCTURE((byte) 0, PluginExternalizerType.GENERIC_STRUCTURE),
	HOME_MAP((byte) 0, PluginExternalizerType.HOME_MAP),
	INVENTORY_ORGANIZER_PLAYER((byte) 0, PluginExternalizerType.INVENTORY_ORGANIZER_PLAYER),
	LIKE_DEPOSIT((byte) 0, PluginExternalizerType.LIKE_DEPOSIT),
	LIKE_GENERATOR((byte) 0, PluginExternalizerType.LIKE_GENERATOR),
	PHYSICAL_DEPOSIT((byte) 0, null),
	PHYSICAL_PLAYER((byte) 0, null),
	PLUGIN_PLAYER((byte) 3, null),
	PLUGIN_PLAYER_DATA((byte) 3, PluginExternalizerType.PLUGIN_PLAYER_DATA),
	POINT_4_BIT((byte) 0, PluginExternalizerType.POINT_4_BIT),
	RAID_ANALYZER((byte) 0, PluginExternalizerType.RAID_ANALYZER),
	RAID_SPECTATOR((byte) 0, PluginExternalizerType.RAID_SPECTATOR),
	RAW_MATERIAL((byte) 0, PluginExternalizerType.RAW_MATERIAL),
	RAW_MATERIAL_MAP((byte) 0, PluginExternalizerType.RAW_MATERIAL_MAP),
	SHORT_COORDINATE_BLOCK_CHUNK((byte) 2, PluginExternalizerType.SHORT_COORDINATE_BLOCK_CHUNK),
	SPAWN_PLAYER((byte) 0, PluginExternalizerType.SPAWN_PLAYER),
	SPIRITUAL_PLAYER((byte) 0, null),
	STABILITY_FORMULA((byte) 0, PluginExternalizerType.STABILITY_FORMULA),
	STRUCTURE((byte) 1, null),
	STRUCTURE_MAP((byte) 0, PluginExternalizerType.STRUCTURE_MAP),
	TAG((byte) 0, PluginExternalizerType.TAG),
	TOWN_BLOCK_MAP((byte) 0, PluginExternalizerType.TOWN_BLOCK_MAP),
	TOWN_BLOCK((byte) 0, PluginExternalizerType.TOWN_BLOCK),
	TOWN_BLOCK_SET((byte) 0, PluginExternalizerType.TOWN_BLOCK_SET),
	TOWN_HALL((byte) 0, PluginExternalizerType.TOWN_HALL),
	TOWN_MAP((byte) 0, PluginExternalizerType.TOWN_MAP),
	TOWN_PLAYER_DATA((byte) 1, PluginExternalizerType.TOWN_PLAYER_DATA),
	TOWN_PLAYER((byte) 0, PluginExternalizerType.TOWN_PLAYER),
	TOWN((byte) 3, PluginExternalizerType.TOWN),
	TOWN_TILE_MAP((byte) 0, PluginExternalizerType.TOWN_TILE_MAP),
	TOWN_TILE((byte) 0, PluginExternalizerType.TOWN_TILE),
	TURRET((byte) 0, PluginExternalizerType.TURRET),
	TUTORIAL_TOWN_PLAYER_DATA((byte) 0, PluginExternalizerType.TUTORIAL_TOWN_PLAYER_DATA),
	TUTORIAL_TOWN_PLAYER((byte) 0, PluginExternalizerType.TUTORIAL_TOWN_PLAYER),
	VANILLA_PLAYER_DATA((byte) 0, PluginExternalizerType.VANILLA_PLAYER_DATA),
	VANILLA_PLAYER((byte) 0, PluginExternalizerType.VANILLA_PLAYER),
	VECTOR_3((byte) 0, PluginExternalizerType.VECTOR_3),
	;
	
	private final byte version;
	private final PluginExternalizerType externalizerType;
}
