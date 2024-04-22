package com.eul4.common.type.player;

import com.eul4.common.model.player.CommonPlayer;

import java.util.function.Function;

public abstract class CommonPlayerType<PP extends CommonPlayer, P extends PP>
{
	public abstract Function<PP, P> getNewInstanceFunction();
}
