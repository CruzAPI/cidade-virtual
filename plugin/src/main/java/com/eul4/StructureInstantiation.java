package com.eul4;

import com.eul4.exception.CannotConstructException;
import com.eul4.model.town.Town;
import com.eul4.model.town.structure.Structure;
import org.bukkit.Location;

@FunctionalInterface
public interface StructureInstantiation
{
	Structure newInstance(Town town, Location location) throws CannotConstructException;
}
