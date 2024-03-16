package com.eul4.model.craft.town;

import com.eul4.model.town.TownBlock;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class CraftTownBlock implements TownBlock
{
	private boolean isAvailable;
	private boolean hasStructure;
	
	public CraftTownBlock(boolean isAvailable)
	{
		this.isAvailable = isAvailable;
	}
	
	@Override
	public boolean canBuild()
	{
		return isAvailable && !hasStructure;
	}
	
	@Override
	public boolean hasStructure()
	{
		return hasStructure;
	}
}
