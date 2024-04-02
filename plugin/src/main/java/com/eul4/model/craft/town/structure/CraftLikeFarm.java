package com.eul4.model.craft.town.structure;

import com.eul4.StructureType;
import com.eul4.exception.CannotConstructException;
import com.eul4.model.town.Town;
import com.eul4.model.town.TownBlock;
import com.eul4.model.town.structure.LikeGenerator;
import org.bukkit.Location;

import java.io.IOException;

public class CraftLikeFarm extends CraftFarmStructure implements LikeGenerator
{
	public CraftLikeFarm(Town town)
	{
		super(town);
	}
	
	public CraftLikeFarm(Town town, TownBlock centerTownBlock) throws CannotConstructException, IOException
	{
		super(town, centerTownBlock);
	}
	
	@Override
	public int getTownBalanceLimit()
	{
		return town.getLikeLimit();
	}
	
	@Override
	public int getTownBalance()
	{
		return town.getLikes();
	}
	
	@Override
	public void setTownBalance(int balance)
	{
		town.setCappedLikes(balance);
	}
	
	@Override
	public StructureType getStructureType()
	{
		return StructureType.LIKE_FARM;
	}
}
