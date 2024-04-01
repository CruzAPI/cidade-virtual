package com.eul4.function;

import com.eul4.exception.CannotConstructException;
import com.eul4.model.town.Town;
import com.eul4.model.town.TownBlock;
import com.eul4.model.town.structure.Structure;

import java.io.IOException;

@FunctionalInterface
public interface StructureInstantiation
{
	Structure newInstance(Town town, TownBlock centerTownBlock) throws CannotConstructException, IOException;
}
