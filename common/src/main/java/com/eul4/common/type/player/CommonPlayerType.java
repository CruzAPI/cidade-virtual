package com.eul4.common.type.player;

import com.eul4.common.model.player.CommonPlayer;
import org.bukkit.entity.Player;

import java.util.function.BiFunction;

public abstract class CommonPlayerType<PP extends CommonPlayer, P extends PP>
{
	public abstract BiFunction<Player, PP, P> getNewInstanceBiFunction();
}
