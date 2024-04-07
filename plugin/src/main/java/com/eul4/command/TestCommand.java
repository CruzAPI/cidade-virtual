package com.eul4.command;

import com.eul4.Main;
import com.eul4.common.constant.CommonNamespacedKey;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
		
		
		
		if(args.length == 0)
		{
			player.teleport(plugin.getCidadeVirtualWorld().getSpawnLocation());
		}
		else if(args.length == 1)
		{
			ItemStack test = new ItemStack(Material.REDSTONE);
			ItemMeta meta = test.getItemMeta();
			var container = meta.getPersistentDataContainer();
			container.set(CommonNamespacedKey.CANCEL_SPAWN, PersistentDataType.BOOLEAN, true);
			test.setItemMeta(meta);
			
			player.getInventory().addItem(test);
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
