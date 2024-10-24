package com.eul4.model.craft.town.structure;

import com.eul4.StructureType;
import com.eul4.calculator.BigDecimalCalculator;
import com.eul4.common.i18n.MessageArgs;
import com.eul4.enums.Currency;
import com.eul4.exception.CannotConstructException;
import com.eul4.holder.CapacitatedCrownHolder;
import com.eul4.i18n.PluginMessage;
import com.eul4.model.town.Town;
import com.eul4.model.town.TownBlock;
import com.eul4.model.town.structure.CapacitatedCrownTransactionResourceStructure;
import com.eul4.model.town.structure.CrownDeposit;
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
			this,
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
		capacity = getRule().getAttributeOrDefault(getBuiltLevel()).getCapacity();
		
		if(capacitatedCrownHolder != null)
		{
			capacitatedCrownHolder.setCapacity(capacity);
		}
		
		super.reloadAttributes();
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
	protected MessageArgs getStructureBalanceMessageUnderAttack()
	{
		return PluginMessage.$BALANCE_$CURRENCY.withArgs
		(
			capacitatedCrownHolder.getBalance(),
			Currency.CROWN
		);
	}
	
	@Override
	protected MessageArgs getStructureBalanceMessage()
	{
		return PluginMessage.$CURRENCY_$BALANCE_$CAPACITY.withArgs
		(
			Currency.CROWN,
			capacitatedCrownHolder.getBalance(),
			capacitatedCrownHolder.getCapacity()
		);
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
