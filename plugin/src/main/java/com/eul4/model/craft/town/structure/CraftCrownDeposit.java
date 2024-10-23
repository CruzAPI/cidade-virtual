package com.eul4.model.craft.town.structure;

import com.eul4.StructureType;
import com.eul4.calculator.BigDecimalCalculator;
import com.eul4.enums.Currency;
import com.eul4.exception.*;
import com.eul4.holder.CapacitatedCrownHolder;
import com.eul4.i18n.PluginMessage;
import com.eul4.model.town.Town;
import com.eul4.model.town.TownBlock;
import com.eul4.model.town.structure.CrownDeposit;
import com.eul4.model.town.structure.CapacitatedCrownTransactionResourceStructure;
import com.eul4.rule.Rule;
import com.eul4.rule.attribute.CrownDepositAttribute;
import com.eul4.wrapper.TransactionalResource;
import com.sk89q.worldedit.math.BlockVector3;
import lombok.Getter;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Set;

public class CraftCrownDeposit extends CraftPhysicalDeposit<BigDecimal> implements
		CrownDeposit,
		CapacitatedCrownTransactionResourceStructure
{
	@Getter
	private final Set<TransactionalResource<BigDecimal>> transactionalResources = Set.of
	(
		new TransactionalResource<>
		(
			BlockVector3.at(0, 1, 0),
			TransactionalResource.Type.CROWN,
			this::createStoleCrownTransaction,
			BigDecimalCalculator.INSTANCE,
			() -> getCapacitatedCrownHolder().isEmpty(),
			this::getAmountOfCrownsToSteal
		)
	);
	
	private CapacitatedCrownHolder capacitatedCrownHolder;
	private transient BigDecimal capacity;
	
	public CraftCrownDeposit(Town town)
	{
		super(town);
	}
	
	public CraftCrownDeposit(Town town, TownBlock centerTownBlock) throws CannotConstructException, IOException
	{
		this(town, centerTownBlock, false);
	}
	
	public CraftCrownDeposit(Town town, TownBlock centerTownBlock, boolean isBuilt) throws CannotConstructException, IOException
	{
		super(town, centerTownBlock, isBuilt);
		capacitatedCrownHolder = new CapacitatedCrownHolder(town);
		capacitatedCrownHolder.setCapacity(capacity);
	}
	
	@Override
	public void reloadAttributes()
	{
		super.reloadAttributes();
		
		capacity = getRule().getAttributeOrDefault(getBuiltLevel()).getCapacity();
		
		if(capacitatedCrownHolder != null)
		{
			capacitatedCrownHolder.setCapacity(capacity);
		}
	}
	
	@Override
	public StructureType getStructureType()
	{
		return StructureType.CROWN_DEPOSIT;
	}
	
	@Override
	public Currency getCurrency()
	{
		return Currency.CROWN;
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public Rule<CrownDepositAttribute> getRule()
	{
		return (Rule<CrownDepositAttribute>) getStructureType().getRule(town.getPlugin());
	}
	
	@Override
	protected PluginMessage getStructureBalanceMessageUnderAttack()
	{
		return PluginMessage.STRUCTURE_CROWN_DEPOSIT_BALANCE;
	}
	
	@Override
	public CapacitatedCrownHolder getHolder()
	{
		return capacitatedCrownHolder;
	}
	
	@Override
	public void setHolder(CapacitatedCrownHolder capacitatedCrownHolder)
	{
		this.capacitatedCrownHolder = capacitatedCrownHolder;
	}
	
	@Override
	public CapacitatedCrownHolder getCapacitatedCrownHolder()
	{
		return capacitatedCrownHolder;
	}
}
