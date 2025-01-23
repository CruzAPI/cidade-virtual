package com.eul4.model.player.spiritual;

import com.eul4.model.player.Fighter;
import com.eul4.model.player.PluginPlayer;
import com.eul4.model.player.SpiritualPlayer;
import com.eul4.model.player.Warder;
import com.eul4.wrapper.TownAttack;

import java.util.Optional;

public interface Defender extends PluginPlayer, SpiritualPlayer, Warder, Fighter
{
	Optional<TownAttack> findTownAttack();
}
