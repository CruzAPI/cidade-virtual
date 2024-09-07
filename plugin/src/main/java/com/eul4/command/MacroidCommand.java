package com.eul4.command;

import com.eul4.Main;
import com.eul4.common.i18n.CommonMessage;
import com.eul4.common.model.player.CommonPlayer;
import com.eul4.enums.StructureStatus;
import com.eul4.wrapper.Macroid;
import lombok.RequiredArgsConstructor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
public class MacroidCommand implements TabExecutor
{
	public static final String COMMAND_NAME = "macroid";
	public static final String[] NAME_AND_ALIASES = new String[] { COMMAND_NAME };
	
	private final Main plugin;
	
	@Override
	public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings)
	{
		return Collections.emptyList();
	}
	
	@Override
	public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args)
	{
		if(!(commandSender instanceof Player player))
		{
			return false;
		}
		
		final CommonPlayer commonPlayer = plugin.getPlayerManager().get(player);
		
		if(!player.isOp())
		{
			commonPlayer.sendMessage(CommonMessage.YOU_DO_NOT_HAVE_PERMISSION);
			return false;
		}
		
		if(args.length == 2 && args[0].equalsIgnoreCase("setstatus"))
		{
			ItemStack item = player.getInventory().getItemInMainHand();
			UUID macroidUUID = plugin.getMacroidService().getMacroidWandUUID(item);
			
			if(macroidUUID == null)
			{
				player.sendMessage("You must be holding a macroid wand.");
				return false;
			}
			
			Macroid macroid = plugin.getMacroidService().getMacroid(macroidUUID);
			
			if(macroid == null)
			{
				player.sendMessage("Macroid not found.");
				return  false;
			}
			
			StructureStatus status = StructureStatus.valueOf(args[1].toUpperCase());
			
			macroid.setStructureStatus(status);
			player.sendMessage("Macroid status changed to " + status);
		}
		else if(args.length == 2)
		{
			Macroid macroid = new Macroid(player, args[0], StructureStatus.valueOf(args[1].toUpperCase()));
			plugin.getMacroidService().newMacroidWand(macroid);
			player.sendMessage("Macroid wand: " + macroid.getStructureName() + " " + macroid.getStructureStatus());
		}
		else
		{
			player.sendMessage("Try: /macroid <structure_name> <structure_status>");
			player.sendMessage("Example: /macroid like_deposit unready");
		}
		
		return true;
	}
}
