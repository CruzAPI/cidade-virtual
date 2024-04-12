package com.eul4.model.craft.town.structure;

import com.eul4.StructureType;
import com.eul4.StructureTypeEnum;
import com.eul4.exception.CannotConstructException;
import com.eul4.model.town.Town;
import com.eul4.model.town.TownBlock;
import com.eul4.model.town.structure.LikeGenerator;
import com.eul4.rule.LikeGeneratorAttribute;
import com.eul4.rule.Rule;

import java.io.IOException;

public class CraftLikeGenerator extends CraftGenerator implements LikeGenerator
{
	public CraftLikeGenerator(Town town)
	{
		super(town);
	}
	
	public CraftLikeGenerator(Town town, TownBlock centerTownBlock) throws CannotConstructException, IOException
	{
		this(town, centerTownBlock, false);
	}
	
	public CraftLikeGenerator(Town town, TownBlock centerTownBlock, boolean isBuilt) throws CannotConstructException, IOException
	{
		super(town, centerTownBlock, isBuilt);
	}
	
	@Override
	public int getTownBalanceLimit()
	{
		return town.getLikeCapacity();
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
	public StructureType<LikeGenerator, LikeGeneratorAttribute> getStructureType()
	{
		return StructureTypeEnum.LIKE_GENERATOR;
	}
	
	@Override
	public Rule<LikeGeneratorAttribute> getRule()
	{
		return getStructureType().getRule(town.getPlugin());
	}
}
