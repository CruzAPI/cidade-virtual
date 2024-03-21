package com.eul4.model.craft.town.structure;

import com.eul4.StructureType;
import com.eul4.exception.CannotConstructException;
import com.eul4.model.town.Town;
import org.bukkit.Location;

public class CraftDislikeFarm extends CraftStructure
{
	public CraftDislikeFarm(Town town, Location location) throws CannotConstructException
	{
		super(town, location);
	}
	
	@Override
	public StructureType getStructureType()
	{
		return StructureType.DISLIKE_FARM;
	}
}
