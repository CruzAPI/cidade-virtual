package com.eul4.command;

import com.eul4.Main;
import com.eul4.Price;
import com.eul4.StructureType;
import com.eul4.common.i18n.CommonMessage;
import com.eul4.common.model.player.CommonPlayer;
import com.eul4.exception.CannotConstructException;
import com.eul4.exception.InsufficientBalanceException;
import com.eul4.exception.StructureLimitException;
import com.eul4.exception.StructureNotForSaleException;
import com.eul4.i18n.PluginMessage;
import com.eul4.model.inventory.craft.CraftStructureShopGui;
import com.eul4.model.player.BuyStructurePerformer;
import com.eul4.model.player.TownPlayer;
import com.eul4.model.town.Town;
import com.eul4.model.town.TownBlock;
import lombok.RequiredArgsConstructor;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
public class BuyStructureCommand implements TabExecutor
{
	public static final String COMMAND_NAME = "buystructure";
	public static final String[] NAME_AND_ALIASES = new String[] { COMMAND_NAME, "loja" };
	
	private final Main plugin;
	
	@Override
	public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender,
			@NotNull Command command,
			@NotNull String alias,
			@NotNull String[] args)
	{
		return Collections.emptyList();
	}
	
	@Override
	public boolean onCommand(@NotNull CommandSender commandSender,
			@NotNull Command command,
			@NotNull String alias,
			@NotNull String[] args)
	{
		if(!(commandSender instanceof Player player))
		{
			return true;
		}
		
		CommonPlayer commonPlayer = plugin.getPlayerManager().get(player);
		
		if(!(commonPlayer instanceof BuyStructurePerformer buyStructurePerformer))
		{
			commonPlayer.sendMessage(CommonMessage.CAN_NOT_PERFORM);
			return false;
		}
		
		return buyStructurePerformer.performBuyStructure();
	}
	
	public boolean executeBuy(TownPlayer townPlayer, StructureType structureType)
	{
		return executeBuy(townPlayer, townPlayer.getPlayer().getLocation().getBlock(), structureType);
	}
	
	public boolean executeBuy(TownPlayer townPlayer, Block block, StructureType structureType)
	{
		Town town = townPlayer.getTown();
		TownBlock townBlock = town.getTownBlock(block);
		
		if(townBlock == null)
		{
			townPlayer.sendMessage(PluginMessage.YOU_CAN_NOT_CONSTRUCT_OUTSIDE_YOUR_TOWN);
			return false;
		}
		
		try
		{
			Price price = town.buyNewStructure(structureType, townBlock);
			
			if(price.getLikes() > 0)
			{
				townPlayer.getPlayer().sendMessage("-" + price.getLikes() + " LIKES");
			}
			
			if(price.getDislikes() > 0)
			{
				townPlayer.getPlayer().sendMessage("-" + price.getDislikes() + " DISLIKES");
			}
			
			return true;
		}
		catch(StructureNotForSaleException e)
		{
			townPlayer.sendMessage(PluginMessage.STRUCTURE_NOT_FOR_SALE);
		}
		catch(CannotConstructException e)
		{
			townPlayer.sendMessage(PluginMessage.STRUCTURE_CAN_NOT_CONSTRUCT_HERE);
		}
		catch(IOException e)
		{
			townPlayer.sendMessage(PluginMessage.STRUCTURE_SCHEMATIC_NOT_FOUND);
		}
		catch(InsufficientBalanceException e)
		{
			if(e.isMissingLikes())
			{
				townPlayer.sendMessage(PluginMessage.MISSING_LIKES, e.getLike());
			}
			
			if(e.isMissingDislikes())
			{
				townPlayer.sendMessage(PluginMessage.MISSING_DISLIKES, e.getDislike());
			}
		}
		catch(StructureLimitException e)
		{
			townPlayer.sendMessage(PluginMessage.STRUCTURE_LIMIT_REACHED, structureType, e.getCount(), e.getLimit());
		}
		
		return false;
	}
}
