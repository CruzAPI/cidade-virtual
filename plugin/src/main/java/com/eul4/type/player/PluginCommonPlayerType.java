package com.eul4.type.player;

import com.eul4.common.type.player.CommonPlayerType;
import com.eul4.model.craft.player.CraftAdmin;
import com.eul4.model.craft.player.CraftRaidAnalyzer;
import com.eul4.model.craft.player.CraftTownPlayer;
import com.eul4.model.player.Admin;
import com.eul4.model.player.PluginPlayer;
import com.eul4.model.player.RaidAnalyzer;
import com.eul4.model.player.TownPlayer;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import java.util.function.Function;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class PluginCommonPlayerType<P extends PluginPlayer> extends CommonPlayerType<PluginPlayer, P>
{
	public static final	PluginCommonPlayerType<TownPlayer> TOWN_PLAYER = new PluginCommonPlayerType<>(CraftTownPlayer::new);
	public static final	PluginCommonPlayerType<RaidAnalyzer> RAID_ANALYZER = new PluginCommonPlayerType<>(CraftRaidAnalyzer::new);
	public static final PluginCommonPlayerType<Admin> ADMIN = new PluginCommonPlayerType<>(CraftAdmin::new);
	
	private final Function<PluginPlayer, P> newInstanceFunction;
	
	@Override
	public Function<PluginPlayer, P> getNewInstanceFunction()
	{
		return newInstanceFunction;
	}
}