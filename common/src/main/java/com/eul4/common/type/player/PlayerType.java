package com.eul4.common.type.player;

import com.eul4.common.Common;
import com.eul4.common.model.player.CommonPlayer;
import org.bukkit.entity.Player;

public interface PlayerType
{
	CommonPlayer newInstance(Player player, Common common);
	CommonPlayer newInstance(Player player, CommonPlayer commonPlayer);
	Class<? extends CommonPlayer> getType();
}
