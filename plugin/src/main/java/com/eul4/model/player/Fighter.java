package com.eul4.model.player;

import com.eul4.model.town.Town;
import com.eul4.wrapper.TownAttack;

import java.util.Optional;

public interface Fighter extends PluginPlayer, SpiritualPlayer
{
	Town getAttackedTown();
	
	default TownAttack getTownAttack()
	{
		return Optional.ofNullable(getAttackedTown()).map(Town::getCurrentAttack).orElse(null);
	}
}
