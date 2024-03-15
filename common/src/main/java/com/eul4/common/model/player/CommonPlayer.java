package com.eul4.common.model.player;

import com.eul4.common.Common;
import com.eul4.common.i18n.Messageable;
import com.eul4.common.type.player.CommonPlayerType;
import org.bukkit.entity.Player;

import java.util.UUID;

public interface CommonPlayer extends Messageable
{
	Player getPlayer();
	Common getPlugin();
	UUID getUniqueId();
	void reset();
}
