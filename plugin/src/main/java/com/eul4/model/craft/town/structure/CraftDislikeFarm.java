package com.eul4.model.craft.town.structure;

import com.eul4.StructureType;
import com.eul4.exception.CannotConstructException;
import com.eul4.model.town.Town;
import com.eul4.model.town.TownBlock;
import org.bukkit.Location;

import java.io.IOException;

public class CraftDislikeFarm extends CraftFarmStructure
{
	public CraftDislikeFarm(Town town, TownBlock centerTownBlock) throws CannotConstructException, IOException
	{
		super(town, centerTownBlock);
	}
	
	@Override
	public StructureType getStructureType()
	{
		return StructureType.DISLIKE_FARM;
	}
}
