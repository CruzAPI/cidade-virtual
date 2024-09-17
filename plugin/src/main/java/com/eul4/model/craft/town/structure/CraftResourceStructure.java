package com.eul4.model.craft.town.structure;

import com.eul4.common.wrapper.Pitch;
import com.eul4.enums.Currency;
import com.eul4.exception.CannotConstructException;
import com.eul4.model.player.Attacker;
import com.eul4.model.town.Town;
import com.eul4.model.town.TownBlock;
import com.eul4.wrapper.Resource;
import com.eul4.wrapper.TownAttack;
import net.kyori.adventure.text.Component;
import org.bukkit.Sound;
import org.bukkit.block.Block;

import java.io.IOException;
import java.util.Optional;
import java.util.Set;
import java.util.function.IntConsumer;
import java.util.function.IntSupplier;
import java.util.function.IntUnaryOperator;

public abstract class CraftResourceStructure extends CraftStructure implements ResourceStructure
{
//	private final Map<Block, Resource> resourceMap; TODO....
	
	public CraftResourceStructure(Town town)
	{
		super(town);
	}
	
	public CraftResourceStructure(Town town, TownBlock centerTownBlock) throws CannotConstructException, IOException
	{
		super(town, centerTownBlock);
	}
	
	public CraftResourceStructure(Town town, TownBlock centerTownBlock, boolean isBuilt)
			throws CannotConstructException, IOException
	{
		super(town, centerTownBlock, isBuilt);
	}
//	TODO....
//	{
//		for(Resource resource : getResources())
//		{
//			resourceMap.put(resource.getRelative(getCenterTownBlock().getBlock()));
//		}
//		resourceMap = null;
//	}
	
	public abstract Set<Resource> getResources();
	
	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		placeResources();
	}
	
	private void placeResource(Resource resource)
	{
		resource.placeRelative(getCenterTownBlock().getBlock());
	}
	
	public void placeResources()
	{
		for(Resource resource : getResources())
		{
			placeResource(resource);
		}
	}
	
	@Override
	public Optional<Resource> findResource(Block block)
	{
		for(Resource resource : getResources())
		{
			if(resource.getRelative(this.getCenterTownBlock().getBlock()).equals(block))
			{
				return Optional.of(resource);
			}
		}
		
		return Optional.empty();
	}
	
	@Override
	public void steal(Resource resource)
	{
		if(!getResources().contains(resource))
		{
			return;
		}
		
		int amountStolen = resource.getSubtractOperation().applyAsInt(20);
		Optional.ofNullable(town.getCurrentAttack())
				.map(TownAttack::getAttacker)
				.map(Attacker::getTown)
				.ifPresent(town ->
				{
					resource.getType().getAddOperation().accept(town, amountStolen);
					
					town.findPluginPlayer().ifPresent(pluginPlayer ->
					{
						//TODO: organize in a untranslatable message
						
						Currency currency = resource.getType().getCurrency();
						
						pluginPlayer.getPlayer().sendMessage(Component.text("+" + amountStolen + " ")
								.append(currency.getPluralWord().translate(pluginPlayer, String::toUpperCase))
								.style(currency.getStyle()));
					});
				});
		onSteal();
	}
	
	private void onSteal()
	{
		Optional.ofNullable(town.getCurrentAttack())
				.map(TownAttack::getAttacker)
				.map(Attacker::getPlayer)
				.ifPresent(player -> player.playSound(player, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0F, Pitch.max()));
		
		town.getPlugin()
				.getServer()
				.getScheduler()
				.getMainThreadExecutor(getTown().getPlugin())
				.execute(this::placeResources);
	}
	
	protected int subtractVirtualBalance(IntConsumer setRemainingCapacity,
			IntUnaryOperator townSubtractBalance,
			IntSupplier getVirtualBalance,
			IntSupplier getRemainingCapacity,
			int value)
	{
		value = Math.min(getVirtualBalance.getAsInt(), value);
		setRemainingCapacity.accept(Math.max(0, getRemainingCapacity.getAsInt() - value));
		return townSubtractBalance.applyAsInt(value);
	}
	
	protected void ifUnderAttackUpdateResources()
	{
		if(getTown().isUnderAttack())
		{
			placeResources();
			updateHologram();
		}
	}
}
