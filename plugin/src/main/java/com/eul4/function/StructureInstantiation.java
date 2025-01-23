package com.eul4.function;

import com.eul4.model.town.Town;
import com.eul4.model.town.TownBlock;
import com.eul4.model.town.structure.Structure;

@FunctionalInterface
public interface StructureInstantiation
{
	Structure newInstance(Town town, TownBlock centerTownBlock);
}
