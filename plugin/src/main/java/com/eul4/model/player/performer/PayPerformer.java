package com.eul4.model.player.performer;

import com.eul4.common.exception.CommonException;
import com.eul4.common.exception.CommonRuntimeException;
import com.eul4.common.i18n.MessageArgs;
import com.eul4.common.util.BigDecimalUtil;
import com.eul4.common.util.LoggerUtil;
import com.eul4.economy.Transaction;
import com.eul4.exception.NegativeBalanceException;
import com.eul4.exception.OverCapacityException;
import com.eul4.model.player.PluginPlayer;
import com.eul4.model.town.Town;
import com.eul4.util.SoundUtil;
import com.eul4.wrapper.CryptoInfo;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static com.eul4.i18n.PluginMessage.*;

public interface PayPerformer extends PluginPlayer
{
	default List<String> onPayTabComplete(String[] args)
	{
		List<String> suggestions = Collections.emptyList();
		
		if(args.length == 1)
		{
			for(Player player : getPlugin().getServer().getOnlinePlayers())
			{
				if(player == getPlayer())
				{
					continue;
				}
				
				if(player.getName().toLowerCase().startsWith(args[0].toLowerCase()))
				{
					suggestions = suggestions == Collections.EMPTY_LIST
							? new ArrayList<>()
							: suggestions;
					suggestions.add(player.getName());
				}
			}
			
			suggestions.sort(Comparator.naturalOrder());
		}
		
		return suggestions;
	}
	
	default boolean performPay(String aliases, String[] args)
	{
		if(args.length == 2)
		{
			String targetName = args[0];
			
			try
			{
				OfflinePlayer offlineTarget = getPlugin().getOfflinePlayerIfCachedOrElseThrow(args[0]);
				targetName = offlineTarget.getName();
				
				Town playerTown = getTown();
				Town targetTown = getPlugin().getTownManager().getTown(offlineTarget.getUniqueId());
				
				if(getPlayer() == offlineTarget.getPlayer())
				{
					sendMessage(COMMAND_PAY_YOURSELF);
					return false;
				}
				
				if(playerTown == null)
				{
					sendMessage(COMMAND_BALANCE_TRY_TOWN_COMMAND);
					return false;
				}
				
				if(targetTown == null)
				{
					sendMessage(COMMAND_PAY_INDIGENT_$TARGET, offlineTarget.getName());
					return false;
				}
				
				if(playerTown == targetTown)
				{
					sendMessage(COMMAND_PAY_YOURSELF);
					return false;
				}
				
				if(playerTown.isUnderAttack())
				{
					sendMessage(COMMAND_PAY_YOUR_TOWN_UNDER_ATTACK);
					return false;
				}
				
				if(targetTown.isUnderAttack())
				{
					sendMessage
					(
						COMMAND_PAY_TARGET_TOWN_UNDER_ATTACK_$TARGET,
						offlineTarget.getName()
					);
					return false;
				}
				
				BigDecimal subtrahend = BigDecimalUtil
						.newBigDecimal(args[1].replace(',', '.'), CryptoInfo.MATH_CONTEXT);
				
				if(subtrahend.compareTo(BigDecimalUtil.ONE_CENT) < 0)
				{
					sendMessage(COMMAND_PAY_MIN_VALUE);
					return false;
				}
				
				if(subtrahend.scale() > 2)
				{
					sendMessage(COMMAND_PAY_SCALE_$LIMIT, 2);
					return false;
				}
				
				if(subtrahend.precision() - subtrahend.scale() > 20)
				{
					sendMessage(COMMAND_PAY_INTEGRAL_SCALE_$LIMIT, 20);
					return false;
				}
				
				Transaction<BigDecimal> transaction = getPlugin()
						.getTransactionManager()
						.createTransaction
						(
							playerTown.createTradePreviewSubtract(subtrahend),
							targetTown.getCapacitatedCrownHolders()
						);
				transaction.tryExecute();
				
				MessageArgs paidMessage = COMMAND_PAY_YOU_PAID_$PLAYER_$AMOUNT
						.withArgs(offlineTarget, subtrahend);
				MessageArgs receivedMessage = COMMAND_PAY_YOU_RECEIVED_$PLAYER_$AMOUNT
						.withArgs(getPlayer(), subtrahend);
				
				sendMessage(paidMessage);
				SoundUtil.playPlingPlong(this);
				
				targetTown.findPluginPlayer().ifPresent(pluginPlayer ->
				{
					pluginPlayer.sendMessage(receivedMessage);
					SoundUtil.playPiano(pluginPlayer.getPlayer());
				});
				
				return true;
			}
			catch(CommonRuntimeException | CommonException e)
			{
				sendMessage(e.getMessageArgs());
				return false;
			}
			catch(NegativeBalanceException e)
			{
				sendMessage(COMMAND_PAY_INSUFFICIENT_BALANCE);
				return false;
			}
			catch(OverCapacityException e)
			{
				sendMessage(COMMAND_PAY_TARGET_OVER_CAPACITY_$TARGET, targetName);
				return false;
			}
			catch(Exception e)
			{
				sendMessage(COMMAND_PAY_UNEXPECTED_ERROR);
				LoggerUtil.severe(getPlugin(),
						e,
						"Failed to execute /pay command used by {0}.",
						getPlayer().getName());
				return false;
			}
		}
		else
		{
			sendMessage(COMMAND_PAY_USE_$ALIASES, aliases);
			return false;
		}
	}
}
