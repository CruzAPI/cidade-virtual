package com.eul4.model.craft.town.structure;

import com.eul4.StructureType;
import com.eul4.exception.CannotConstructException;
import com.eul4.model.town.Town;
import com.eul4.model.town.TownBlock;
import org.bukkit.util.Vector;

import java.io.IOException;

public class CraftTownHall extends CraftStructure
{
	public CraftTownHall(Town town, TownBlock centerTownBlock) throws CannotConstructException, IOException
	{
		super(town, centerTownBlock, new Vector(-5.0D, 3.0D, 0.5D), true);
	}
	
	public CraftTownHall(Town town)
	{
		super(town);
	}
	
	@Override
	public StructureType getStructureType()
	{
		return StructureType.TOWN_HALL;
	}
}
