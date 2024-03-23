package com.eul4.command;

import com.eul4.Main;
import com.eul4.StructureType;
import com.eul4.exception.CannotConstructException;
import com.eul4.model.player.TownPlayer;
import com.eul4.model.town.Town;
import com.eul4.model.town.structure.Structure;
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

import java.io.IOException;
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
			if(plugin.getPlayerManager().get(player) instanceof TownPlayer townPlayer)
			{
				StructureType structureType = StructureType.valueOf(args[0]);
				
				try
				{
					Structure structure = structureType.getInstantiation()
							.newInstance(townPlayer.getTown(),
									townPlayer.getTown().getTownBlock(player.getLocation().getBlock()));
				}
				catch(CannotConstructException e)
				{
					player.sendMessage("cannot construct here!");
				}
				catch(IOException e)
				{
					player.sendMessage("failed to load schematic!!!");
				}
			}
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
