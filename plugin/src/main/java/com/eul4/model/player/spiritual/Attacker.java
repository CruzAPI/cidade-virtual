package com.eul4.model.player.spiritual;

import com.eul4.model.player.Fighter;
import com.eul4.model.player.PluginPlayer;
import com.eul4.model.player.SpiritualPlayer;
import com.eul4.wrapper.TownAttack;

public interface Attacker extends PluginPlayer, SpiritualPlayer, Fighter
{
	TownAttack getTownAttack();
}
