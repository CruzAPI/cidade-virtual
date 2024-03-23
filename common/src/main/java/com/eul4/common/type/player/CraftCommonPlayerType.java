package com.eul4.common.type.player;

import com.eul4.common.model.player.craft.CraftCommonAdmin;
import com.eul4.common.model.player.CommonAdmin;
import com.eul4.common.model.player.CommonPlayer;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.function.Function;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class CraftCommonPlayerType<CP extends CommonPlayer> extends CommonPlayerType<CP>
{
	public static final	CraftCommonPlayerType<CommonAdmin> COMMON_ADMIN = new CraftCommonPlayerType<>(CraftCommonAdmin::new);
	
	private final Function<CommonPlayer, CP> newInstanceFunction;
}