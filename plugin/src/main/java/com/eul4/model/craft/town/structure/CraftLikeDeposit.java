package com.eul4.model.craft.town.structure;

import com.eul4.StructureType;
import com.eul4.enums.Currency;
import com.eul4.exception.CannotConstructException;
import com.eul4.model.town.Town;
import com.eul4.model.town.TownBlock;
import com.eul4.model.town.structure.LikeDeposit;
import com.eul4.rule.Rule;
import com.eul4.rule.attribute.LikeDepositAttribute;

import java.io.IOException;

public class CraftLikeDeposit extends CraftDeposit implements LikeDeposit
{
	public CraftLikeDeposit(Town town)
	{
		super(town);
	}
	
	public CraftLikeDeposit(Town town, TownBlock centerTownBlock) throws CannotConstructException, IOException
	{
		this(town, centerTownBlock, false);
	}
	
	public CraftLikeDeposit(Town town, TownBlock centerTownBlock, boolean isBuilt) throws CannotConstructException, IOException
	{
		super(town, centerTownBlock, isBuilt);
	}
	
	@Override
	public StructureType getStructureType()
	{
		return StructureType.LIKE_DEPOSIT;
	}
	
	@Override
	public Currency getCurrency()
	{
		return Currency.LIKE;
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public Rule<LikeDepositAttribute> getRule()
	{
		return (Rule<LikeDepositAttribute>) getStructureType().getRule(town.getPlugin());
	}
}
