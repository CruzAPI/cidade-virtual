package com.eul4.model.town.structure;

import com.eul4.common.i18n.MessageArgs;
import com.eul4.common.util.LoggerUtil;
import com.eul4.economy.Transaction;
import com.eul4.exception.OverCapacityException;
import com.eul4.i18n.PluginMessage;
import com.eul4.model.player.spiritual.Attacker;
import com.eul4.util.SoundUtil;
import com.eul4.wrapper.TownAttack;
import com.eul4.wrapper.TransactionalResource;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

import java.util.Optional;
import java.util.Set;

import static com.eul4.common.wrapper.Pitch.max;
import static com.eul4.i18n.PluginMessage.OTHER_STOLE_FROM_YOU_$AMOUNT_$CURRENCY_$PLAYER;
import static com.eul4.i18n.PluginMessage.YOU_STOLE_FROM_OTHER_$AMOUNT_$CURRENCY_$PLAYER;
import static org.bukkit.Sound.ENTITY_EXPERIENCE_ORB_PICKUP;

public interface TransactionalResourceStructure extends Structure
{
	<N extends Number & Comparable<N>> Set<TransactionalResource<N>> getTransactionalResources();
	
	default Optional<TransactionalResource<?>> findTransactionResource(Block block)
	{
		for(TransactionalResource<?> transactionalResource : getTransactionalResources())
		{
			if(transactionalResource.getRelative(this.getCenterTownBlock().getBlock()).equals(block))
			{
				return Optional.of(transactionalResource);
			}
		}
		
		return Optional.empty();
	}
	
	default <N extends Number & Comparable<N>> boolean steal
	(
		TransactionalResource<N> transactionalResource,
		ItemStack tool
	)
	{
		if(!getTransactionalResources().contains(transactionalResource))
		{
			return false;
		}
		
		final N amount = transactionalResource.getAmountToSteal(tool);
		
		TownAttack townAttack = getTown().getCurrentAttack();
		Attacker attacker = townAttack == null ? null : townAttack.getAttacker();
		
		if(attacker == null)
		{
			return false;
		}
		
		try
		{
			Transaction<N> transaction = transactionalResource
					.getTransactionCreator()
					.createTransaction(amount);
			
			N total = transaction.calculateTotal(transactionalResource.getCalculator());
			transaction.tryExecute();
			
			attacker.sendMessage
			(
				YOU_STOLE_FROM_OTHER_$AMOUNT_$CURRENCY_$PLAYER,
				total,
				transactionalResource.getType().getCurrency(),
				getTown().getOwner()
			);
			
			attacker.getPlayer().playSound
			(
				attacker.getPlayer(),
				ENTITY_EXPERIENCE_ORB_PICKUP,
				1.0F,
				max()
			);
			
			MessageArgs otherStoleYouMessage = OTHER_STOLE_FROM_YOU_$AMOUNT_$CURRENCY_$PLAYER
					.withArgs
					(
						total,
						transactionalResource.getType().getCurrency(),
						attacker.getPlayer()
					);
			
			getTown()
					.findPluginPlayer()
					.ifPresent(pluginPlayer ->
					{
						pluginPlayer.sendMessage(otherStoleYouMessage);
						SoundUtil.playPiano(pluginPlayer.getPlayer());
					});
			
			getTown().getPlugin().execute(this::placeTransactionalResources);
			return true;
		}
		catch(OverCapacityException e)
		{
			attacker.sendMessage(transactionalResource.getType().getOverCapacityMessage());
			return false;
		}
		catch(Exception e)
		{
			attacker.sendMessage(PluginMessage.UNEXPECTED_ERROR_WHILE_STEALING);
			LoggerUtil.severe
			(
				getTown().getPlugin(),
				e,
				"Error while {0} tried to steal {1} resource ({2})",
				attacker.getPlayer().getName(),
				getTown().getOwner().getName(),
				transactionalResource.getType());
			return false;
		}
	}
	
	private Optional<TransactionalResource> findTransactionalResource(Block block)
	{
		for(TransactionalResource resource : getTransactionalResources())
		{
			if(resource.getRelative(this.getCenterTownBlock().getBlock()).equals(block))
			{
				return Optional.of(resource);
			}
		}
		
		return Optional.empty();
	}
	
	default void placeTransactionalResources()
	{
		for(TransactionalResource transactionalResource : getTransactionalResources())
		{
			placeTransactionalResource(transactionalResource);
		}
	}
	
	private void placeTransactionalResource(TransactionalResource<?> transactionalResource)
	{
		transactionalResource.placeRelative(getCenterTownBlock().getBlock());
	}
}
