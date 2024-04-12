package com.eul4.model.craft.town.structure;

import com.eul4.StructureType;
import com.eul4.StructureTypeEnum;
import com.eul4.enums.Currency;
import com.eul4.exception.CannotConstructException;
import com.eul4.model.town.Town;
import com.eul4.model.town.TownBlock;
import com.eul4.model.town.structure.DislikeDeposit;
import com.eul4.model.town.structure.DislikeGenerator;
import com.eul4.rule.Rule;
import com.eul4.rule.attribute.DislikeDepositAttribute;
import com.eul4.rule.attribute.DislikeGeneratorAttribute;

import java.io.IOException;

public class CraftDislikeDeposit extends CraftDeposit implements DislikeDeposit
{
	public CraftDislikeDeposit(Town town)
	{
		super(town);
	}
	
	public CraftDislikeDeposit(Town town, TownBlock centerTownBlock) throws CannotConstructException, IOException
	{
		this(town, centerTownBlock, false);
	}
	
	public CraftDislikeDeposit(Town town, TownBlock centerTownBlock, boolean isBuilt) throws CannotConstructException, IOException
	{
		super(town, centerTownBlock, isBuilt);
	}
	
	@Override
	public StructureType<DislikeDeposit, DislikeDepositAttribute> getStructureType()
	{
		return StructureTypeEnum.DISLIKE_DEPOSIT;
	}
	
	@Override
	public Currency getCurrency()
	{
		return Currency.DISLIKE;
	}
	
	@Override
	public Rule<DislikeDepositAttribute> getRule()
	{
		return getStructureType().getRule(town.getPlugin());
	}
}
