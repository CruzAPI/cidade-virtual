package com.eul4.type.player;

import com.eul4.Main;
import com.eul4.common.model.player.CommonPlayer;
import com.eul4.common.type.player.PlayerType;
import com.eul4.model.craft.player.CraftAdmin;
import com.eul4.model.craft.player.CraftTownPlayer;
import com.eul4.model.player.Admin;
import com.eul4.model.player.TownPlayer;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;

import java.util.function.BiFunction;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class PluginPlayerType<CP extends CommonPlayer> extends PlayerType<Main, CP>
{
	public static final PluginPlayerType<TownPlayer> TOWN_PLAYER = new PluginPlayerType<>(CraftTownPlayer::new);
	public static final PluginPlayerType<Admin> ADMIN = new PluginPlayerType<>(CraftAdmin::new);
	
	private final BiFunction<Player, Main, CP> newInstanceBiFunction;
	
	@Override
	public BiFunction<Player, Main, CP> getNewInstanceBiFunction()
	{
		return newInstanceBiFunction;
	}
}