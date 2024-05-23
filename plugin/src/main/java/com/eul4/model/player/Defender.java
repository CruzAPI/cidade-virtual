package com.eul4.model.player;

import com.eul4.wrapper.TownAttack;

import java.util.Optional;

public interface Defender extends PluginPlayer, SpiritualPlayer, Warder
{
	Optional<TownAttack> findTownAttack();
}
