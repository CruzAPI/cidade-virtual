package com.eul4.model.craft.town.structure;

import com.eul4.StructureType;
import com.eul4.exception.CannotConstructException;
import com.eul4.i18n.PluginMessage;
import com.eul4.model.town.Town;
import com.eul4.model.town.TownBlock;
import com.eul4.model.town.structure.DislikeGenerator;
import com.eul4.rule.attribute.DislikeGeneratorAttribute;
import com.eul4.rule.Rule;

import java.io.IOException;

public class CraftDislikeGenerator extends CraftGenerator implements DislikeGenerator
{
	public CraftDislikeGenerator(Town town)
	{
		super(town);
	}
	
	public CraftDislikeGenerator(Town town, TownBlock centerTownBlock) throws CannotConstructException, IOException
	{
		this(town, centerTownBlock, false);
	}
	
	public CraftDislikeGenerator(Town town, TownBlock centerTownBlock, boolean isBuilt) throws CannotConstructException, IOException
	{
		super(town, centerTownBlock, isBuilt);
	}
	
	@Override
	public int getTownBalanceLimit()
	{
		return town.getDislikeCapacity();
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
		return StructureType.DISLIKE_GENERATOR;
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public Rule<DislikeGeneratorAttribute> getRule()
	{
		return (Rule<DislikeGeneratorAttribute>) getStructureType().getRule(town.getPlugin());
	}
	
	@Override
	protected PluginMessage getStructureBalanceMessageUnderAttack()
	{
		return PluginMessage.STRUCTURE_DISLIKE_GENERATOR_BALANCE;
	}
}
