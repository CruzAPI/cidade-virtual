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
	public static final byte GENERIC_PLUGIN_PLAYER = 0;
	public static final byte PLUGIN_PLAYER = 0;
	public static final byte RAID_ANALYZER = 0;
	public static final byte TOWN_PLAYER_DATA = 0;
	public static final byte TOWN_PLAYER = 0;
	
	private Byte adminVersion;
	private Byte attackerVersion;
	private Byte genericPluginPlayerVersion;
	private Byte pluginPlayerVersion;
	private Byte raidAnalyzerVersion;
	private Byte townPlayerDataVersion;
	private Byte townPlayerVersion;
}
