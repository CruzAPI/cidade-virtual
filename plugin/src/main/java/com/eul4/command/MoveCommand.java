package com.eul4.command;

import com.eul4.Main;
import com.eul4.exception.CannotConstructException;
import com.eul4.model.player.TownPlayer;
import com.eul4.model.town.TownBlock;
import com.eul4.model.town.structure.Structure;
import lombok.RequiredArgsConstructor;
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
public class MoveCommand implements TabExecutor
{
	private final Main plugin;
	
	@Override
	public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings)
	{
		return Collections.emptyList();
	}
	
	@Override
	public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args)
	{
		if(!(commandSender instanceof Player player)
				|| !(plugin.getPlayerManager().get(player) instanceof TownPlayer townPlayer))
		{
			return true;
		}
		
		if(args.length == 1 && args[0].equalsIgnoreCase("start"))
		{
			Structure movingStructure = townPlayer.getMovingStructure();
			
			if(movingStructure != null)
			{
				try
				{
					movingStructure.cancelMove();
				}
				catch(CannotConstructException e)
				{
					player.sendMessage("failed to cancel last move.");
				}
			}
			
			TownBlock townBlock = townPlayer.getTown().getTownBlock(player.getLocation().getBlock());
			
			if(townBlock == null)
			{
				player.sendMessage("you are not in your town.");
				return true;
			}
			
			Structure structure = townBlock.getStructure();
			
			if(structure == null)
			{
				player.sendMessage("you are not in a structure.");
				return true;
			}
			
			try
			{
				structure.startMove();
				townPlayer.setMovingStructure(structure);
			}
			catch(CannotConstructException e)
			{
				player.sendMessage("failed to cancel move");
			}
			catch(IOException e)
			{
				player.sendMessage("schem not found");
			}
		}
		else if((args.length == 1 || args.length == 2) && args[0].equalsIgnoreCase("finish"))
		{
			final int rotation;
			
			try
			{
				rotation = args.length == 2 ? Integer.parseInt(args[1]) : 0;
				
				if(rotation % 90 != 0)
				{
					player.sendMessage("rotation must be divisible by 90.");
					return true;
				}
			}
			catch(NumberFormatException e)
			{
				player.sendMessage("rotation must be an int.");
				return true;
			}
			
			Structure movingStructure = townPlayer.getMovingStructure();
			
			if(movingStructure == null)
			{
				player.sendMessage("you are not moving any structure.");
				return true;
			}
			
			TownBlock townBlock = townPlayer.getTown().getTownBlock(player.getLocation().getBlock());
			
			if(townBlock == null)
			{
				player.sendMessage("you must be in a town block to finish the move.");
				return true;
			}
			
			try
			{
				movingStructure.finishMove(townBlock, rotation);
				townPlayer.setMovingStructure(null);
				player.sendMessage("succes??");
			}
			catch(CannotConstructException e)
			{
				player.sendMessage("you cannot construct here.");
			}
		}
		else if(args.length == 1 && args[0].equalsIgnoreCase("cancel"))
		{
			Structure movingStructure = townPlayer.getMovingStructure();
			
			if(movingStructure == null)
			{
				player.sendMessage("you are not moving any structure.");
				return true;
			}
			
			try
			{
				movingStructure.cancelMove();
			}
			catch(CannotConstructException e)
			{
				player.sendMessage("failed to reconstruct.");
			}
			
			townPlayer.setMovingStructure(null);
			player.sendMessage("move cancelled.");
		}
		return false;
	}
}
