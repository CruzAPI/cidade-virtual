package com.eul4;

import com.eul4.common.wrapper.CommonVersions;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class Versions extends CommonVersions
{
	public static final byte ADMIN = 0;
	public static final byte ATTACKER = 0;
	public static final byte DEPOSIT = 0;
	public static final byte DISLIKE_DEPOSIT = 0;
	public static final byte DISLIKE_GENERATOR = 0;
	public static final byte GENERATOR = 0;
	public static final byte GENERIC_PLUGIN_PLAYER = 0;
	public static final byte LIKE_DEPOSIT = 0;
	public static final byte LIKE_GENERATOR = 0;
	public static final byte PLUGIN_PLAYER = 0;
	public static final byte RAID_ANALYZER = 0;
	public static final byte STRUCTURE_ANALYZER = 0;
	public static final byte TOWN_BLOCK_MAP = 0;
	public static final byte TOWN_HALL = 0;
	public static final byte TOWN_BLOCK = 0;
	public static final byte TOWN_MAP = 0;
	public static final byte TOWN_PLAYER_DATA = 0;
	public static final byte TOWN_PLAYER = 0;
	public static final byte TOWN = 0;
	public static final byte TOWN_TILE_MAP = 0;
	public static final byte TOWN_TILE = 0;
	
	private Byte adminVersion;
	private Byte attackerVersion;
	private Byte depositVersion;
	private Byte dislikeDepositVersion;
	private Byte dislikeGeneratorVersion;
	private Byte generatorVersion;
	private Byte genericPluginPlayerVersion;
	private Byte likeDepositVersion;
	private Byte likeGeneratorVersion;
	private Byte pluginPlayerVersion;
	private Byte raidAnalyzerVersion;
	private Byte structureVersion;
	private Byte townBlockMapVersion;
	private Byte townBlockVersion;
	private Byte townHallVersion;
	private Byte townMapVersion;
	private Byte townPlayerDataVersion;
	private Byte townPlayerVersion;
	private Byte townVersion;
	private Byte townTileMapVersion;
	private Byte townTileVersion;
}
