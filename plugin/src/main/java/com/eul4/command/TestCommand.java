package com.eul4.command;

import com.eul4.Main;
import com.eul4.Price;
import com.eul4.StructureType;
import com.eul4.common.constant.CommonNamespacedKey;
import com.eul4.exception.CannotConstructException;
import com.eul4.exception.InsufficientBalanceException;
import com.eul4.exception.StructureNotForSaleException;
import com.eul4.i18n.PluginMessage;
import com.eul4.model.player.TownPlayer;
import com.eul4.model.town.Town;
import com.eul4.model.town.structure.Structure;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.Component;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.craftbukkit.v1_20_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_20_R3.inventory.CraftItemStack;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

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
		
		
		
		if(args.length == 0)
		{
			player.teleport(plugin.getCidadeVirtualWorld().getSpawnLocation());
		}
		else if(args.length == 1)
		{
			TownPlayer townPlayer = (TownPlayer) plugin.getPlayerManager().get(player);
			StructureType structureType = StructureType.valueOf(args[0]);
			
			Town town = townPlayer.getTown();
			
			try
			{
				Price price = town.buyNewStructure(structureType, town.getTownBlock(player.getLocation().getBlock()));
				
				if(price.getLikes() > 0)
				{
					player.sendMessage("-" + price.getLikes() + " LIKES");
				}
				
				if(price.getDislikes() > 0)
				{
					player.sendMessage("-" + price.getDislikes() + " DISLIKES");
				}
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
			long count = plugin.getEntityRegisterListener().getPersistentEntities().values().stream()
					.filter(entity -> entity.getWorld() == plugin.getTownWorld())
					.filter(entity -> entity instanceof ArmorStand)
					.count();
			
			player.sendMessage("count: " + count);
			player.sendMessage(plugin.getTownManager().getTowns().size() + " towns");
		}
		
		return false;
	}
}
