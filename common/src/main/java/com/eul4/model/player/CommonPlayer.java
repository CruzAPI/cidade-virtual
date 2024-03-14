package com.eul4.model.player;

import com.eul4.Common;
import com.eul4.i18n.Messageable;
import org.bukkit.entity.Player;

import java.util.UUID;

public interface CommonPlayer extends Messageable
{
	Player getPlayer();
	Common getPlugin();
	UUID getUniqueId();
}
