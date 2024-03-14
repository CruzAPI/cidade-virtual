package com.eul4.type.player;

import com.eul4.Common;
import com.eul4.model.craft.player.CraftTownPlayer;
import com.eul4.model.player.CommonPlayer;
import com.eul4.model.player.TownPlayer;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;

import java.util.function.BiFunction;
import java.util.function.Function;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class PluginPlayerType<CP extends CommonPlayer> extends PlayerType<CP>
{
	public static final PluginPlayerType<TownPlayer> TOWN_PLAYER = new PluginPlayerType<>(CraftTownPlayer::new);
	
	private final BiFunction<Player, Common, CP> newInstanceBiFunction;
}