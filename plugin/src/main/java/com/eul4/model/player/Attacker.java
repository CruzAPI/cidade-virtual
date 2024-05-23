package com.eul4.model.player;

import com.eul4.wrapper.TownAttack;

public interface Attacker extends PluginPlayer, SpiritualPlayer
{
	TownAttack getTownAttack();
}
