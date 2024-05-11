package com.eul4.type.player;

import com.eul4.Main;
import com.eul4.common.Common;
import com.eul4.common.externalizer.reader.ObjectReader;
import com.eul4.common.externalizer.writer.ObjectWriter;
import com.eul4.common.model.player.CommonPlayer;
import com.eul4.common.type.player.PlayerType;
import com.eul4.externalizer.reader.*;
import com.eul4.externalizer.writer.*;
import com.eul4.model.craft.player.CraftAdmin;
import com.eul4.model.craft.player.CraftAttacker;
import com.eul4.model.craft.player.CraftRaidAnalyzer;
import com.eul4.model.craft.player.CraftTownPlayer;
import com.eul4.model.player.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;

import java.util.function.BiFunction;

@RequiredArgsConstructor
@Getter
public enum PluginPlayerType implements PlayerType
{
	ADMIN(Admin.class, CraftAdmin::new, CraftAdmin::new, AdminWriter.class, AdminReader.class),
	ATTACKER(Attacker.class, CraftAttacker::new, CraftAttacker::new, AttackerWriter.class, AttackerReader.class),
	TOWN_PLAYER(TownPlayer.class, CraftTownPlayer::new, CraftTownPlayer::new, TownPlayerWriter.class, TownPlayerReader.class),
	RAID_ANALYZER(RaidAnalyzer.class, CraftRaidAnalyzer::new, CraftRaidAnalyzer::new, RaidAnalyzerWriter.class, RaidAnalyzerReader.class),
	;
	
	private final Class<? extends PluginPlayer> type;
	private final BiFunction<Player, Main, PluginPlayer> pluginConstructor;
	private final BiFunction<Player, PluginPlayer, PluginPlayer> commonPlayerConstructor;
	private final Class<? extends PluginPlayerWriter<?>> writerClass;
	private final Class<? extends PluginPlayerReader<?>> readerClass;
	
	@Override
	public CommonPlayer newInstance(Player player, Common common)
	{
		return pluginConstructor.apply(player, (Main) common);
	}
	
	@Override
	public CommonPlayer newInstance(Player player, CommonPlayer commonPlayer)
	{
		return commonPlayerConstructor.apply(player, (PluginPlayer) commonPlayer);
	}
}
