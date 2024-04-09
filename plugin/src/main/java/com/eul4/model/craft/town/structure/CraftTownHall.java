package com.eul4.model.craft.town.structure;

import com.eul4.StructureType;
import com.eul4.exception.CannotConstructException;
import com.eul4.model.town.Town;
import com.eul4.model.town.TownBlock;
import com.eul4.model.town.structure.TownHall;
import com.eul4.rule.Rule;
import com.eul4.rule.TownHallAttribute;
import org.bukkit.util.Vector;

import java.io.IOException;

public class CraftTownHall extends CraftStructure implements TownHall
{
	public CraftTownHall(Town town, TownBlock centerTownBlock) throws CannotConstructException, IOException
	{
		super(town, centerTownBlock, true);
	}
	
	public CraftTownHall(Town town)
	{
		super(town);
	}
	
	public Rule<TownHallAttribute> getRule()
	{
		return town.getPlugin().getTownHallRule();
	}
	
	@Override
	public StructureType getStructureType()
	{
		return StructureType.TOWN_HALL;
	}
}
