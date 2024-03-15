package com.eul4.common.type.player;

import com.eul4.common.model.player.CommonPlayer;

import java.util.function.Function;

public abstract class CommonPlayerType<CP extends CommonPlayer>
{
	public abstract Function<CommonPlayer, CP> getNewInstanceFunction();
}
