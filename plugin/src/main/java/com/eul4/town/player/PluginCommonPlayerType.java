package com.eul4.town.player;

import com.eul4.common.model.player.CommonPlayer;
import com.eul4.common.type.player.CommonPlayerType;
import com.eul4.town.model.craft.player.CraftTownPlayer;
import com.eul4.town.model.player.TownPlayer;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.function.Function;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class PluginCommonPlayerType<CP extends CommonPlayer> extends CommonPlayerType<CP>
{
	public static final	PluginCommonPlayerType<TownPlayer> TOWN_PLAYER = new PluginCommonPlayerType<>(CraftTownPlayer::new);
	
	private final Function<CommonPlayer, CP> newInstanceFunction;
}