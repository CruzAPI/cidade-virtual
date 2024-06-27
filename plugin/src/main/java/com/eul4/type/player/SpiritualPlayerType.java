package com.eul4.type.player;

import com.eul4.Main;
import com.eul4.externalizer.reader.*;
import com.eul4.externalizer.writer.*;
import com.eul4.model.craft.player.*;
import com.eul4.model.player.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;

import java.util.function.BiFunction;

@RequiredArgsConstructor
@Getter
public enum SpiritualPlayerType implements PluginPlayerType
{
	ATTACKER(Attacker.class, CraftAttacker::new, CraftAttacker::new, AttackerWriter.class, AttackerReader.class),
	RAID_ANALYZER(RaidAnalyzer.class, CraftRaidAnalyzer::new, CraftRaidAnalyzer::new, RaidAnalyzerWriter.class, RaidAnalyzerReader.class),
	RAID_SPECTATOR(RaidSpectator.class, CraftRaidSpectator::new, CraftRaidSpectator::new, RaidSpectatorWriter.class, RaidSpectatorReader.class),
	DEFENDER(Defender.class, CraftDefender::new, CraftDefender::new, DefenderWriter.class, DefenderReader.class),
	DEFENDER_SPECTATOR(DefenderSpectator.class, CraftDefenderSpectator::new, CraftDefenderSpectator::new, DefenderSpectatorWriter.class, DefenderSpectatorReader.class),
	INVENTORY_ORGANIZER_PLAYER(InventoryOrganizerPlayer.class, CraftInventoryOrganizerPlayer::new, CraftInventoryOrganizerPlayer::new, InventoryOrganizerPlayerWriter.class, InventoryOrganizerPlayerReader.class),
	
	;
	
	private final Class<? extends PluginPlayer> interfaceType;
	private final BiFunction<Player, Main, SpiritualPlayer> pluginConstructor;
	private final BiFunction<Player, PluginPlayer, SpiritualPlayer> commonPlayerConstructor;
	private final Class<? extends SpiritualPlayerWriter<?>> writerClass;
	private final Class<? extends SpiritualPlayerReader<?>> readerClass;
	
	@Override
	public SpiritualPlayer newInstance(Player player, Main plugin)
	{
		return pluginConstructor.apply(player, plugin);
	}
	
	@Override
	public SpiritualPlayer newInstance(Player player, PluginPlayer pluginPlayer)
	{
		return commonPlayerConstructor.apply(player, pluginPlayer);
	}
	
	@Override
	public PlayerCategory getCategory()
	{
		return PlayerCategory.SPIRITUAL;
	}
}
