package com.eul4.type.player;

import com.eul4.Common;
import com.eul4.model.player.CommonPlayer;
import org.bukkit.entity.Player;

import java.util.function.BiFunction;
import java.util.function.Function;

public abstract class CommonPlayerType<CP extends CommonPlayer>
{
	public abstract Function<CommonPlayer, CP> getNewInstanceFunction();
}
