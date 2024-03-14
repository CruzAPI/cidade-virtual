package com.eul4.type.player;

import com.eul4.model.craft.player.CraftTownPlayer;
import com.eul4.model.player.CommonPlayer;
import com.eul4.model.player.TownPlayer;
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