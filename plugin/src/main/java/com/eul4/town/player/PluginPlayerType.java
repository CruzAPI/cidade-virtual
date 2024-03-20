package com.eul4.town.player;

import com.eul4.common.Common;
import com.eul4.common.type.player.PlayerType;
import com.eul4.town.model.craft.player.CraftTownPlayer;
import com.eul4.common.model.player.CommonPlayer;
import com.eul4.town.model.player.TownPlayer;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;

import java.util.function.BiFunction;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class PluginPlayerType<CP extends CommonPlayer> extends PlayerType<CP>
{
	public static final PluginPlayerType<TownPlayer> TOWN_PLAYER = new PluginPlayerType<>(CraftTownPlayer::new);
	
	private final BiFunction<Player, Common, CP> newInstanceBiFunction;
}