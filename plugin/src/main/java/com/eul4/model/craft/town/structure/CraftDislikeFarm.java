package com.eul4.model.craft.town.structure;

import com.eul4.StructureType;
import com.eul4.exception.CannotConstructException;
import com.eul4.model.town.Town;
import com.eul4.model.town.TownBlock;
import com.eul4.model.town.structure.DislikeGenerator;

import java.io.IOException;

public class CraftDislikeFarm extends CraftFarmStructure implements DislikeGenerator
{
	public CraftDislikeFarm(Town town)
	{
		super(town);
	}
	
	public CraftDislikeFarm(Town town, TownBlock centerTownBlock) throws CannotConstructException, IOException
	{
		this(town, centerTownBlock, false);
	}
	
	public CraftDislikeFarm(Town town, TownBlock centerTownBlock, boolean isBuilt) throws CannotConstructException, IOException
	{
		super(town, centerTownBlock, isBuilt);
	}
	
	@Override
	public int getTownBalanceLimit()
	{
		return town.getDislikeLimit();
	}
	
	@Override
	public int getTownBalance()
	{
		return town.getDislikes();
	}
	
	@Override
	public void setTownBalance(int balance)
	{
		town.setCappedDislikes(balance);
	}
	
	@Override
	public StructureType getStructureType()
	{
		return StructureType.DISLIKE_FARM;
	}
}
