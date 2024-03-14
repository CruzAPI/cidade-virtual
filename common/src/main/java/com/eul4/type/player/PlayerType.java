package com.eul4.type.player;

import com.eul4.Common;
import com.eul4.model.player.CommonPlayer;
import org.bukkit.entity.Player;

import java.util.function.BiFunction;

public abstract class PlayerType<CP extends CommonPlayer>
{
	public abstract BiFunction<Player, Common, CP> getNewInstanceBiFunction();
}
