package com.eul4.type.player;

import com.eul4.Main;
import com.eul4.PluginReaderConstructor;
import com.eul4.PluginWriterConstructor;
import com.eul4.common.Common;
import com.eul4.common.model.player.CommonPlayer;
import com.eul4.common.type.player.PlayerType;
import com.eul4.externalizer.reader.AdminReader;
import com.eul4.externalizer.reader.AttackerReader;
import com.eul4.externalizer.reader.RaidAnalyzerReader;
import com.eul4.externalizer.reader.TownPlayerReader;
import com.eul4.externalizer.writer.AdminWriter;
import com.eul4.externalizer.writer.AttackerWriter;
import com.eul4.externalizer.writer.RaidAnalyzerWriter;
import com.eul4.externalizer.writer.TownPlayerWriter;
import com.eul4.model.craft.player.CraftAdmin;
import com.eul4.model.craft.player.CraftAttacker;
import com.eul4.model.craft.player.CraftRaidAnalyzer;
import com.eul4.model.craft.player.CraftTownPlayer;
import com.eul4.model.player.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;

import java.util.function.BiFunction;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public enum PluginPlayerType implements PlayerType
{
	ADMIN(Admin.class, CraftAdmin::new, CraftAdmin::new, AdminWriter::new, AdminReader::new),
	ATTACKER(Attacker.class, CraftAttacker::new, CraftAttacker::new, AttackerWriter::new, AttackerReader::new),
	TOWN_PLAYER(TownPlayer.class, CraftTownPlayer::new, CraftTownPlayer::new, TownPlayerWriter::new, TownPlayerReader::new),
	RAID_ANALYZER(RaidAnalyzer.class, CraftRaidAnalyzer::new, CraftRaidAnalyzer::new, RaidAnalyzerWriter::new, RaidAnalyzerReader::new),
	;
	
	private final Class<? extends PluginPlayer> type;
	private final BiFunction<Player, Main, PluginPlayer> pluginConstructor;
	private final BiFunction<Player, PluginPlayer, PluginPlayer> commonPlayerConstructor;
	private final PluginWriterConstructor pluginWriterConstructor;
	private final PluginReaderConstructor pluginReaderConstructor;
	
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