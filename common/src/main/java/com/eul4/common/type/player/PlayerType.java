package com.eul4.common.type.player;

import com.eul4.common.Common;
import com.eul4.common.model.player.CommonPlayer;
import org.bukkit.entity.Player;

import java.util.function.BiFunction;

public abstract class PlayerType<PL extends Common, PP extends CommonPlayer>
{
	public abstract BiFunction<Player, PL, PP> getNewInstanceBiFunction();
}
