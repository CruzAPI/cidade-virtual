package com.eul4.type.player;

import com.eul4.Main;
import com.eul4.common.type.player.CommonPlayerType;
import com.eul4.common.type.player.CommonPlayerTypeEnum;
import com.eul4.model.craft.player.CraftAdmin;
import com.eul4.model.craft.player.CraftRaidAnalyzer;
import com.eul4.model.craft.player.CraftTownPlayer;
import com.eul4.model.player.Admin;
import com.eul4.model.player.PluginPlayer;
import com.eul4.model.player.RaidAnalyzer;
import com.eul4.model.player.TownPlayer;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;

import java.util.function.BiFunction;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class PluginCommonPlayerType<P extends PluginPlayer> extends CommonPlayerType<PluginPlayer, Main, P>
{
	@Getter
	@RequiredArgsConstructor
	public enum Type implements CommonPlayerTypeEnum
	{
		TOWN_PLAYER(PluginCommonPlayerType.TOWN_PLAYER),
		RAID_ANALYZER(PluginCommonPlayerType.RAID_ANALYZER),
		ADMIN(PluginCommonPlayerType.ADMIN);
		
		private final PluginCommonPlayerType<?> commonPlayerType;
	}
	
	public static final	PluginCommonPlayerType<TownPlayer> TOWN_PLAYER = new PluginCommonPlayerType<>(CraftTownPlayer::new, CraftTownPlayer::new);
	public static final	PluginCommonPlayerType<RaidAnalyzer> RAID_ANALYZER = new PluginCommonPlayerType<>(CraftRaidAnalyzer::new, CraftRaidAnalyzer::new);
	public static final PluginCommonPlayerType<Admin> ADMIN = new PluginCommonPlayerType<>(CraftAdmin::new, CraftAdmin::new);
	
	private final BiFunction<Player, Main, P> pluginConstructor;
	private final BiFunction<Player, PluginPlayer, P> commonPlayerConstructor;
}