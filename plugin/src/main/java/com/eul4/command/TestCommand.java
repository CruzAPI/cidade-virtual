package com.eul4.command;

import com.eul4.Main;
import com.eul4.common.i18n.CommonMessage;
import com.eul4.common.i18n.Messageable;
import com.eul4.item.ContaintmentPickaxe;
import com.eul4.model.player.PluginPlayer;
import com.eul4.model.player.physical.TownPlayer;
import com.eul4.model.town.Town;
import com.eul4.type.PluginWorldType;
import lombok.RequiredArgsConstructor;
import net.minecraft.server.level.ServerLevel;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ItemType;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RequiredArgsConstructor
public class TestCommand implements TabExecutor
{
	public static final String COMMAND_NAME = "test";
	public static final String[] NAME_AND_ALIASES = new String[] { COMMAND_NAME };
	
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
		Messageable messageable = plugin.getMessageableService().getMessageable(commandSender);
		
		if(messageable == null)
		{
			return false;
		}
		
		if(!plugin.getPermissionService().hasPermission(commandSender, "command.test"))
		{
			messageable.sendMessage(CommonMessage.YOU_DO_NOT_HAVE_PERMISSION);
			return true;
		}
		
		if(!(commandSender instanceof Player player))
		{
			return true;
		}
		
		PluginPlayer pluginPlayer = (PluginPlayer) plugin.getPlayerManager().get(player);
		
		if(args.length == 0)
		{
			player.teleport(new Location(PluginWorldType.CIDADE_VIRTUAL.getWorld(), 0.0D, 0.0D, 0.0D).toHighestLocation());
		}
		else if((args.length == 2) && args[0].equals("containtment"))
		{
			float chance = Float.parseFloat(args[1]);
			ContaintmentPickaxe containtmentPickaxe = new ContaintmentPickaxe(chance);
			player.getInventory().addItem(containtmentPickaxe.getItemStack());
		}
		else if((args.length == 1) && args[0].equals("reloadchunks"))
		{
			File blockDataDir = new File(plugin.getDataFolder() + "/block_data");
			
			List<ChunkInfo> chunkInfos = new ArrayList<>();
			
			for(File worldDir : blockDataDir.listFiles())
			{
				String worldName = worldDir.getName();
				World world = plugin.getServer().getWorld(worldName);
				
				var files = worldDir.listFiles();
				plugin.getLogger().warning(worldName + " chunks in files: " + files.length);
				
				for(File chunkFile : files)
				{
					String regex = "c\\.(-?\\d+)\\.(-?\\d+)\\.dat";
					
					Pattern pattern = Pattern.compile(regex);
					Matcher matcher = pattern.matcher(chunkFile.getName());
					
					if(matcher.matches())
					{
						int x = Integer.parseInt(matcher.group(1));
						int z = Integer.parseInt(matcher.group(2));
						
						chunkInfos.add(new ChunkInfo(world, x, z));
					}
					else
					{
						plugin.getLogger().severe("Invalid file name: " + chunkFile.getName());
					}
				}
			}
			
			
			Iterator<ChunkInfo> iterator = chunkInfos.iterator();
			int size = chunkInfos.size();
			
			AtomicInteger count = new AtomicInteger(0);
			
			new BukkitRunnable()
			{
				@Override
				public void run()
				{
					for(int i = 0; i < 20 && iterator.hasNext(); i++)
					{
						iterator.next().getAsync().whenComplete((chunk, cause) ->
						{
							plugin.getBlockDataFiler().loadBlockData(chunk.getBlock(0, 0, 0));
							plugin.getLogger().info("Chunks: " + count.incrementAndGet() + "/" + size);
						});
						
						iterator.remove();
					}
				}
			}.runTaskTimer(plugin, 0L, 20L);
		}
		else if((args.length == 2) && args[0].equals("test"))
		{
			int amount = Integer.parseInt(args[1]);
		}
		else if((args.length == 1) && args[0].equals("1"))
		{
			ItemStack item = player.getInventory().getItemInMainHand();
			net.minecraft.world.item.ItemStack nmsStack = CraftItemStack.asNMSCopy(item);
			int enchantibility = nmsStack.getItem().getEnchantmentValue();
		}
		else if((args.length == 1) && args[0].equals("2"))
		{
			ItemStack item = ItemStack.of(Material.IRON_SWORD);
			ItemMeta meta = item.getItemMeta();
			
			if(meta instanceof Damageable damageable)
			{
				damageable.setDamage(10);
			}
			
			meta.setAttributeModifiers(ItemType.IRON_SWORD.getDefaultAttributeModifiers());
			
			item.setItemMeta(meta);
			player.getInventory().addItem(item);
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
			OfflinePlayer offlinePlayer = plugin.getOfflinePlayerIfCached(args[0]);
			
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
		}
		else if(args.length == 3)
		{
			player.sendMessage(plugin.getTownManager().getTowns().size() + " towns");
		}
		
		return false;
	}
	
	@RequiredArgsConstructor
	private static class ChunkInfo
	{
		private final World world;
		private final int x;
		private final int z;
		
		public CompletableFuture<Chunk> getAsync()
		{
			return world.getChunkAtAsync(x, z);
		}
	}
}
