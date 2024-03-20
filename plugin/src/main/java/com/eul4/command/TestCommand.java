package com.eul4.command;

import com.eul4.Main;
import lombok.RequiredArgsConstructor;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.decoration.ArmorStand;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.craftbukkit.v1_20_R3.CraftWorld;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.CreatureSpawnEvent;
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
			player.sendMessage(plugin.getSpawnEntityInterceptor().entities.size() + " entities.size()");
		}
//		Location loc = player.getLocation();
//		ServerLevel serverLevel = ((CraftWorld) player.getWorld()).getHandle();
//
//		ArmorStand armorStand = new ArmorStand(serverLevel,
//				loc.getX() + 2,
//				loc.getY(),
//				loc.getZ());
//
//		serverLevel.addFreshEntity(armorStand, CreatureSpawnEvent.SpawnReason.COMMAND);
		
		return false;
	}
}
