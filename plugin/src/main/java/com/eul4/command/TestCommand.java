package com.eul4.command;

import com.eul4.Main;
import com.eul4.model.player.PluginPlayer;
import com.eul4.model.player.TownPlayer;
import com.eul4.model.town.Town;
import com.eul4.type.PluginWorldType;
import lombok.RequiredArgsConstructor;
import net.minecraft.server.level.ServerLevel;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.text.DecimalFormat;
import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
public class TestCommand implements TabExecutor
{
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
		
		if(!player.isOp())
		{
			return true;
		}
		
		PluginPlayer pluginPlayer = (PluginPlayer) plugin.getPlayerManager().get(player);
		
		if(args.length == 0)
		{
			player.teleport(new Location(PluginWorldType.CIDADE_VIRTUAL.getWorld(), 0.0D, 0.0D, 0.0D).toHighestLocation());
		}
		else if((args.length == 1) && args[0].equals("debug"))
		{
			Player target = args.length == 1 ? player : Bukkit.getPlayer(args[1]);
			
			if(target == null)
			{
				player.sendMessage("player not found.");
				return false;
			}
			
			PluginPlayer targetPluginPlayer = (PluginPlayer) plugin.getPlayerManager().get(target);
			
			Town town = targetPluginPlayer.getTown();
			
			if(town == null)
			{
				player.sendMessage(target.getName() + " does not have a town!");
				return false;
			}
			
			Entity npc = town.getArmory().getNPC();
			
			player.sendMessage("Armory NPC: uuid=" + npc.getUniqueId()
					+ " type=" + npc.getType()
					+ " isDead=" + npc.isDead()
					+ " isValid=" + npc.isValid()
					+ " ref=" + Integer.toHexString(npc.hashCode())
			);
		}
		else if((args.length == 1 || args.length == 2) && args[0].equals("info"))
		{
			Player target = args.length == 1 ? player : Bukkit.getPlayer(args[1]);
			
			if(target == null)
			{
				player.sendMessage("player not found.");
				return false;
			}
			
			PluginPlayer targetPluginPlayer = (PluginPlayer) plugin.getPlayerManager().get(target);
			player.sendMessage(target.getName() + ": " + targetPluginPlayer.getClass().getSimpleName());
		}
		else if(args.length == 1)
		{
			int amount = Integer.parseInt(args[0]);
			
			pluginPlayer.getTown().setDislikes(amount);
			pluginPlayer.getTown().setLikes(amount);
		}
		else if(args.length == 2)
		{
			OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayerIfCached(args[0]);
			
			Town town = plugin.getTownManager().getTown(offlinePlayer.getUniqueId());
			
			player.sendMessage("map: " + town.getBoughtTileMapByDepth());
			player.sendMessage("tilesBought: " + town.getTilesBought());
		}
		else if(args.length == 1)
		{
			Location location = player.getLocation();
			player.getLocation().getDirection();
			ServerLevel serverLevel = ((CraftWorld) location.getWorld()).getHandle();
			var nmsArmorStand = new net.minecraft.world.entity.decoration.ArmorStand(serverLevel,
					location.getX(),
					location.getY(),
					location.getZ());
			
			serverLevel.addFreshEntity(nmsArmorStand);
		}
//		else if(args.length == 1)
//		{
//			if(plugin.getPlayerManager().get(player) instanceof TownPlayer townPlayer)
//			{
//				boolean test = Boolean.parseBoolean(args[0]);
//				townPlayer.test(test);
//				player.sendMessage("setting test to: " + townPlayer.test());
//			}
//			else
//			{
//				player.sendMessage("You are not a town player.");
//			}
//		}
		else if(args.length == 2)
		{
			TownPlayer townPlayer = (TownPlayer) plugin.getPlayerManager().get(player);
			double hardness = townPlayer.getTown().getHardness();
			double hardnessLimit = townPlayer.getTown().getHardnessLimit();

			DecimalFormat df = new DecimalFormat("0.0#");

			player.sendMessage(df.format(hardness) + "/" + df.format(hardnessLimit));
		}
		else if(args.length == 2)
		{
			Material type = player.getInventory().getItemInMainHand().getType();
			
			Bukkit.broadcastMessage(type + " hardness: " + type.getHardness()
					+ " br: " + type.getBlastResistance()
					+ " isBlock: " + type.isBlock()
					+ " solid: " + type.isSolid());
		}
		else if(args.length == 3)
		{
			player.sendMessage(plugin.getTownManager().getTowns().size() + " towns");
		}
		
		return false;
	}
}
