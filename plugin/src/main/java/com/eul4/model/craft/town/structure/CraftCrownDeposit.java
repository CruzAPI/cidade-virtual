package com.eul4.model.craft.town.structure;

import com.eul4.StructureType;
import com.eul4.enums.Currency;
import com.eul4.exception.CannotConstructException;
import com.eul4.holder.CapacitatedCrownHolder;
import com.eul4.i18n.PluginMessage;
import com.eul4.model.town.Town;
import com.eul4.model.town.TownBlock;
import com.eul4.model.town.structure.CrownDeposit;
import com.eul4.rule.Rule;
import com.eul4.rule.attribute.CrownDepositAttribute;

import java.io.IOException;
import java.math.BigDecimal;

public class CraftCrownDeposit extends CraftPhysicalDeposit<BigDecimal> implements CrownDeposit
{
//	@Getter
//	private final Set<Resource> resources = Set.of(Resource.builder()
//			.type(Resource.Type.CROWN)
//			.relativePosition(BlockVector3.at(0, 1, 0))
//			.subtractOperation(this::subtract)
//			.emptyChecker(this::isEmpty)
//			.build());
//
	private CapacitatedCrownHolder capacitatedCrownHolder;
	private BigDecimal capacity;
	
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
		capacitatedCrownHolder = new CapacitatedCrownHolder();
		capacitatedCrownHolder.setCapacity(capacity);
	}
	
	@Override
	public void reloadAttributes()
	{
		super.reloadAttributes();
		
		capacity = getRule().getAttributeOrDefault(getBuiltLevel()).getCapacity();
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
