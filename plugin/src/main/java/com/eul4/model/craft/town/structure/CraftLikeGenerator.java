package com.eul4.model.craft.town.structure;

import com.eul4.StructureType;
import com.eul4.enums.Currency;
import com.eul4.event.GeneratorCollectEvent;
import com.eul4.event.LikeGeneratorCollectEvent;
import com.eul4.exception.CannotConstructException;
import com.eul4.i18n.PluginMessage;
import com.eul4.model.town.Town;
import com.eul4.model.town.TownBlock;
import com.eul4.model.town.structure.LikeGenerator;
import com.eul4.rule.Rule;
import com.eul4.rule.attribute.LikeGeneratorAttribute;
import com.eul4.wrapper.Resource;
import com.sk89q.worldedit.math.BlockVector3;
import lombok.Getter;

import java.io.IOException;
import java.util.Set;

public class CraftLikeGenerator extends CraftGenerator implements LikeGenerator
{
	@Getter
	private final Set<Resource> resources = Set.of(Resource.builder()
			.type(Resource.Type.LIKE)
			.relativePosition(BlockVector3.at(0, 1, 0))
			.subtractOperation(this::subtract)
			.emptyChecker(this::isEmpty)
			.build());
	
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
		town.setLikes(balance);
	}
	
	@Override
	public StructureType getStructureType()
	{
		return StructureType.LIKE_GENERATOR;
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public Rule<LikeGeneratorAttribute> getRule()
	{
		return (Rule<LikeGeneratorAttribute>) getStructureType().getRule(town.getPlugin());
	}
	
	@Override
	protected PluginMessage getStructureBalanceMessageUnderAttack()
	{
		return PluginMessage.STRUCTURE_LIKE_GENERATOR_BALANCE;
	}
	
	@Override
	public Currency getCurrency()
	{
		return Currency.LIKE;
	}
	
	@Override
	public boolean hasReachedMaxTownBalanceCapacity()
	{
		return town.hasReachedMaxLikeCapacity();
	}
	
	@Override
	protected int getTownGeneratorsBalance()
	{
		return town.getLikesInGenerators();
	}
	
	@Override
	protected void setTownGeneratorsBalance(int balance)
	{
		town.setLikesInGenerators(balance);
	}
	
	@Override
	protected LikeGeneratorCollectEvent newGeneratorCollectEvent(int amountCollected)
	{
		return new LikeGeneratorCollectEvent(town, amountCollected);
	}
}
