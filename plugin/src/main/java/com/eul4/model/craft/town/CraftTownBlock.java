package com.eul4.model.craft.town;

import com.eul4.model.town.TownBlock;

public class CraftTownBlock implements TownBlock
{
	
	@Override
	public boolean canBuild()
	{
		return false;
	}
	
	@Override
	public boolean hasStructure()
	{
		return false;
	}
}
