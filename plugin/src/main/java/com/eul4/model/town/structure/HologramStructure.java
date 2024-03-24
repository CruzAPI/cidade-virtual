package com.eul4.model.town.structure;

import com.eul4.common.hologram.Hologram;
import org.bukkit.Location;
import org.bukkit.util.Vector;

public interface HologramStructure extends Structure
{
	Hologram getHologram();
	Vector getHologramRelativeLocation();
	default void teleportHologram()
	{
		getHologram().teleport(getLocation().add(getHologramRelativeLocation()));
	}
}
