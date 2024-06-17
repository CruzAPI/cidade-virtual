package com.eul4.model.craft.town.structure;

import com.eul4.StructureType;
import com.eul4.enums.Currency;
import com.eul4.exception.CannotConstructException;
import com.eul4.i18n.PluginMessage;
import com.eul4.model.town.Town;
import com.eul4.model.town.TownBlock;
import com.eul4.model.town.structure.DislikeDeposit;
import com.eul4.rule.Rule;
import com.eul4.rule.attribute.DislikeDepositAttribute;
import com.eul4.wrapper.Resource;
import com.sk89q.worldedit.math.BlockVector3;
import lombok.Getter;

import java.io.IOException;
import java.util.Set;

public class CraftDislikeDeposit extends CraftDeposit implements DislikeDeposit
{
	@Getter
	private final Set<Resource> resources = Set.of(Resource.builder()
			.type(Resource.Type.DISLIKE)
			.relativePosition(BlockVector3.at(0, 1, 0))
			.subtractOperation(this::subtract)
			.emptyChecker(this::isEmpty)
			.build());
	
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
	public StructureType getStructureType()
	{
		return StructureType.DISLIKE_DEPOSIT;
	}
	
	@Override
	public Currency getCurrency()
	{
		return Currency.DISLIKE;
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public Rule<DislikeDepositAttribute> getRule()
	{
		return (Rule<DislikeDepositAttribute>) getStructureType().getRule(town.getPlugin());
	}
	
	@Override
	protected PluginMessage getStructureBalanceMessageUnderAttack()
	{
		return PluginMessage.STRUCTURE_DISLIKE_DEPOSIT_BALANCE;
	}
	
	@Override
	protected int getTotalTownBalance()
	{
		return town.getDislikes();
	}
	
	@Override
	protected int subtract(int balance)
	{
		return subtractVirtualBalance(this::setRemainingCapacity,
				town::subtractDislikes,
				this::getVirtualBalance,
				this::getRemainingCapacity,
				balance);
	}
	
	@Override
	public void onTownDislikeBalanceChange()
	{
		super.onTownDislikeBalanceChange();
		ifUnderAttackUpdateResources();
	}
}
