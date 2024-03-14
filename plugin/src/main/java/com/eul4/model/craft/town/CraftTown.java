package com.eul4.model.craft.town;

import com.eul4.model.player.TownPlayer;
import com.eul4.model.town.Town;
import lombok.RequiredArgsConstructor;
import org.bukkit.Location;

@RequiredArgsConstructor
public class CraftTown implements Town
{
	private final TownPlayer owner;
	private final Location location;
}
