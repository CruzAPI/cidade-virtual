package com.eul4.model.craft.town.structure;

import com.eul4.StructureType;
import com.eul4.enums.Currency;
import com.eul4.exception.CannotConstructException;
import com.eul4.i18n.PluginMessage;
import com.eul4.model.town.Town;
import com.eul4.model.town.TownBlock;
import com.eul4.model.town.structure.LikeDeposit;
import com.eul4.rule.Rule;
import com.eul4.rule.attribute.LikeDepositAttribute;
import com.eul4.wrapper.Resource;
import com.sk89q.worldedit.math.BlockVector3;
import lombok.Getter;

import java.io.IOException;
import java.util.Set;

public class CraftLikeDeposit extends CraftDeposit<Integer> implements LikeDeposit
{
	@Getter
	private final Set<Resource> resources = Set.of(Resource.builder()
			.type(Resource.Type.LIKE)
			.relativePosition(BlockVector3.at(0, 1, 0))
			.emptyChecker(this::isEmpty)
			.subtractOperation(this::subtract)
			.build());
	
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
	protected Integer subtract(Integer balance)
	{
		return null; //TODO is it a bug?
	}
	
	@Override
	public boolean isEmpty()
	{
		return getVirtualBalance() <= 0;
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public Rule<LikeDepositAttribute> getRule()
	{
		return (Rule<LikeDepositAttribute>) getStructureType().getRule(town.getPlugin());
	}
	
	@Override
	protected PluginMessage getStructureBalanceMessageUnderAttack()
	{
		return PluginMessage.STRUCTURE_LIKE_DEPOSIT_BALANCE;
	}
	
	@Override
	public Integer getVirtualBalance()
	{
		return null;
	}
	
	@Override
	public Integer getTotalBalance()
	{
		return town.getLikes();
	}
	
	//	@Override
//	protected int subtract(int balance)
//	{
//		return subtractVirtualBalance(this::setRemainingCapacity,
//				town::subtractLikes,
//				this::getVirtualBalance,
//				this::getRemainingCapacity,
//				balance);
//	}
	
	@Override
	public void onTownLikeBalanceChange()
	{
		super.onTownLikeBalanceChange();
		ifUnderAttackUpdateResources();
	}
}
