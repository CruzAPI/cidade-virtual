package com.eul4.listener;

import com.eul4.Main;
import com.eul4.enums.PluginNamespacedKey;
import com.eul4.enums.Rarity;
import com.eul4.util.RarityUtil;
import com.eul4.wrapper.EnchantType;
import lombok.RequiredArgsConstructor;
import net.minecraft.Util;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.EnchantmentTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.util.random.WeightedRandom;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.craftbukkit.util.RandomSourceWrapper;
import org.bukkit.enchantments.EnchantmentOffer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.enchantment.PrepareItemEnchantEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.EnchantingInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;
import java.util.stream.Collectors;

import static net.minecraft.world.item.enchantment.EnchantmentHelper.filterCompatibleEnchantments;

@RequiredArgsConstructor
public class EnchantmentListener implements Listener
{
	private final Main plugin;
	
	private final Random random = new Random();
	private static final Map<UUID, MaterialEnchantmentOffer> MATERIAL_ENCHANTMENT_OFFER_MAP = new HashMap<>();
	
	private boolean isInEnchantingTableIncludingCustomEnchants(Holder<net.minecraft.world.item.enchantment.Enchantment> holder)
	{
		return holder.is(EnchantmentTags.IN_ENCHANTING_TABLE)
				|| holder.getRegisteredName().equals(PluginNamespacedKey.ENCHANTMENT_STABILITY.toString());
	}
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void on(EnchantItemEvent event)
	{
		Player player = event.getEnchanter();
		
		event.setCancelled(true);
		
		int playerLevel = player.getLevel();
		
		ItemStack enchantedItem = enchantItem(event.getItem().clone(), event.getExpLevelCost());
		ItemMeta meta = enchantedItem.getItemMeta();
		
		if
		(
			!(event.getInventory() instanceof EnchantingInventory enchantingInventory)
			|| meta.getEnchants().isEmpty()
			&& meta instanceof EnchantmentStorageMeta enchantmentStorageMeta
			&& !enchantmentStorageMeta.hasStoredEnchants()
		)
		{
			return;
		}
		
		ItemStack secondary = enchantingInventory.getSecondary();
		
		if(secondary != null)
		{
			enchantingInventory.setSecondary(secondary.subtract(event.whichButton() + 1));
		}
		
		Block enchantingTable = event.getEnchantBlock();
		
		enchantingInventory.setItem(enchantedItem);
		player.setLevel(Math.max(0, playerLevel - event.getExpLevelCost()));
		player.getWorld().playSound(enchantingTable.getLocation(), Sound.BLOCK_ENCHANTMENT_TABLE_USE, 1.0F, random.nextFloat() * 0.1F + 0.9F);
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onInventoryCloseRemoveFromMap(InventoryCloseEvent event)
	{
		MATERIAL_ENCHANTMENT_OFFER_MAP.remove(event.getPlayer().getUniqueId());
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void removeFromMapWhenItemIsEmpty(InventoryClickEvent event)
	{
		Inventory inventory = event.getInventory();
		
		if(inventory instanceof EnchantingInventory enchantingInventory)
		{
			plugin.getServer().getScheduler().getMainThreadExecutor(plugin).execute(() ->
			{
				if(enchantingInventory.getItem() == null)
				{
					MATERIAL_ENCHANTMENT_OFFER_MAP.remove(event.getWhoClicked().getUniqueId());
				}
			});
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void recalculateCosts(PrepareItemEnchantEvent event)
	{
		ItemStack item = event.getItem();
		
		Rarity itemRarity = RarityUtil.getRarity(item);
		Player player = event.getEnchanter();
		EnchantmentOffer[] offers = event.getOffers();
		Block enchantingTable = event.getEnchantBlock();
		Rarity enchantingTableRarity = RarityUtil.getRarity(plugin, enchantingTable);
		
		MaterialEnchantmentOffer materialEnchantmentOffer = MATERIAL_ENCHANTMENT_OFFER_MAP.get(player.getUniqueId());
		
		if(materialEnchantmentOffer != null && materialEnchantmentOffer.material == item.getType())
		{
			for(int i = 0; i < offers.length && i < materialEnchantmentOffer.offers.length; i++)
			{
				offers[i] = materialEnchantmentOffer.offers[i];
			}
			
			return;
		}
		
		List<Block> bookshelves = event.getBookshelves();
		
		int bookshelfBonus = getBookshelvesBonus(bookshelves);
		int bonus = Math.min(enchantingTableRarity.getMaxEnchantmentBonus(), bookshelfBonus);
		
		double base = random.nextInt(1, 8 + 1) + Math.floor(bonus / 2.0D) + random.nextInt(0, bonus + 1);
		
		int[] costs = new int[]
		{
			(int) Math.floor(Math.max(1, base / 3)),
			(int) Math.floor((base * 2) / 3 + 1),
			(int) Math.floor(Math.max(base, bonus * 2)),
		};
		
		for(int i = 0; i < offers.length && i < costs.length; i++)
		{
			EnchantmentOffer offer = offers[i];
			
			if(offer != null)
			{
				offer.setCost(costs[i]);
			}
		}
		
		MATERIAL_ENCHANTMENT_OFFER_MAP.put(player.getUniqueId(), new MaterialEnchantmentOffer(item.getType(), offers));
	}
	
	private int getBookshelvesBonus(List<Block> bookshelves)
	{
		int bonus = 0;
		
		for(Block bookshelf : bookshelves)
		{
			if(bookshelf.getType() == Material.BOOKSHELF)
			{
				Rarity bookshelfRarity = RarityUtil.getRarity(plugin, bookshelf);
				bonus += bookshelfRarity.getBookshelfBonus();
			}
		}
		
		return bonus;
	}
	
	@RequiredArgsConstructor
	private static class MaterialEnchantmentOffer
	{
		private final Material material;
		private final EnchantmentOffer[] offers;
	}
	
	private ItemStack enchantItem(ItemStack itemStack, int level)
	{
		final net.minecraft.core.RegistryAccess registryAccess = net.minecraft.server.MinecraftServer.getServer().registryAccess();
		final net.minecraft.core.Registry<net.minecraft.world.item.enchantment.Enchantment> enchantments = registryAccess.registryOrThrow(Registries.ENCHANTMENT);
		
		net.minecraft.world.item.ItemStack nmsStack = CraftItemStack.asNMSCopy(itemStack);
		
		net.minecraft.world.item.ItemStack enchanted = enchantItem
		(
			new RandomSourceWrapper(random),
			nmsStack,
			level,
			enchantments.holders()
					.filter(this::isInEnchantingTableIncludingCustomEnchants)
					.collect(Collectors.toList())
		);
		
		return CraftItemStack.asCraftMirror(enchanted);
	}
	
	private static net.minecraft.world.item.ItemStack enchantItem
	(
		RandomSource random,
		net.minecraft.world.item.ItemStack stack,
		int level,
		List<Holder<net.minecraft.world.item.enchantment.Enchantment>> possibleEnchantments
	)
	{
		List<EnchantmentInstance> list = selectEnchantment(random, stack, level, possibleEnchantments);
		
		if(stack.is(Items.BOOK))
		{
			stack = new net.minecraft.world.item.ItemStack(Items.ENCHANTED_BOOK);
		}
		
		for(EnchantmentInstance enchantmentInstance : list)
		{
			stack.enchant(enchantmentInstance.enchantment, enchantmentInstance.level);
		}
		
		return stack;
	}
	
	private static List<EnchantmentInstance> selectEnchantment
	(
		RandomSource random,
		net.minecraft.world.item.ItemStack stack,
		int level,
		List<Holder<net.minecraft.world.item.enchantment.Enchantment>> possibleEnchantments
	)
	{
		List<EnchantmentInstance> list = new ArrayList<>();
		
		Item item = stack.getItem();
		int i = item.getEnchantmentValue();
		
		if(i <= 0)
		{
			return list;
		}
		else
		{
			level += 1 + random.nextInt(i / 4 + 1) + random.nextInt(i / 4 + 1);
			float f = (random.nextFloat() + random.nextFloat() - 1.0F) * 0.15F;
			level = Mth.clamp(Math.round((float) level + (float) level * f), 1, Integer.MAX_VALUE);
			
			List<EnchantmentInstance> list2 = getAvailableEnchantmentResults2(level, stack, possibleEnchantments);
			
			if(!list2.isEmpty())
			{
				WeightedRandom.getRandomItem(random, list2).ifPresent(list::add);
				
				while(random.nextInt(50) <= level)
				{
					if(!list.isEmpty())
					{
						filterCompatibleEnchantments(list2, Util.lastOf(list));
					}
					
					if(list2.isEmpty())
					{
						break;
					}
					
					WeightedRandom.getRandomItem(random, list2).ifPresent(list::add);
					level /= 2;
				}
			}
			
			return list;
		}
	}
	
//	public static List<EnchantmentInstance> getAvailableEnchantmentResults(int level,
//			net.minecraft.world.item.ItemStack stack,
//			Stream<Holder<net.minecraft.world.item.enchantment.Enchantment>> possibleEnchantments)
//	{
//		Bukkit.broadcastMessage("level: " + level);
//		List<EnchantmentInstance> list = Lists.newArrayList();
//		boolean bl = stack.is(Items.BOOK);
//		possibleEnchantments.filter(enchantment -> enchantment.value().isPrimaryItem(stack) || bl)
//				.forEach(enchantmentx ->
//				{
//					net.minecraft.world.item.enchantment.Enchantment enchantment = enchantmentx.value();
//
//					for(int j = enchantment.getMaxLevel(); j >= enchantment.getMinLevel(); j--)
//					{
//						Bukkit.broadcastMessage
//						(
//							enchantmentx.getRegisteredName() + " j: " + j
//							+ " minCost: " + enchantment.getMinCost(j) + " >= " + level
//							+ " && maxCost: " + enchantment.getMaxCost(j) + " <= " + level
//						);
//						if(level >= enchantment.getMinCost(j) && level <= enchantment.getMaxCost(j))
//						{
//							Bukkit.broadcastMessage("added: " + enchantmentx.getRegisteredName() + " " + j);
//							list.add(new EnchantmentInstance(enchantmentx, j));
//							break;
//						}
//					}
//				});
//		return list;
//	}
//
	public static List<EnchantmentInstance> getAvailableEnchantmentResults2
	(
		int level,
		net.minecraft.world.item.ItemStack nmsStack,
		List<Holder<net.minecraft.world.item.enchantment.Enchantment>> possibleEnchantments
	)
	{
		final List<EnchantmentInstance> enchantmentInstances = new ArrayList<>();
		
		final ItemStack bukkitStack = CraftItemStack.asCraftMirror(nmsStack);
		final Rarity rarity = RarityUtil.getRarity(bukkitStack);
		
		final net.minecraft.core.RegistryAccess registryAccess = net.minecraft.server.MinecraftServer.getServer().registryAccess();
		final net.minecraft.core.Registry<net.minecraft.world.item.enchantment.Enchantment> enchantments = registryAccess.registryOrThrow(Registries.ENCHANTMENT);
		
		for(Holder<net.minecraft.world.item.enchantment.Enchantment> holder : possibleEnchantments)
		{
			net.minecraft.world.item.enchantment.Enchantment enchantment = holder.value();
			
			if(!enchantment.isPrimaryItem(nmsStack) && !nmsStack.is(Items.BOOK))
			{
				continue;
			}
			
			String enchantTypeName = holder.getRegisteredName().replaceAll(".*:", "").toUpperCase();
			
			EnchantType enchantType = EnchantType.valueOf(enchantTypeName);
			
			int maxLevel = enchantType.getMaxLevel(rarity);
			Bukkit.broadcastMessage
			(
				rarity.name() + " "
				+ enchantType.name()
				+ " maxLvl: " + maxLevel
			);
			
			for(int j = maxLevel; j >= 1; j--)
			{
				int minCost = enchantType.getMinCost(j, rarity);
				int maxCost = enchantType.getMaxCost(j, rarity);
				
				Bukkit.broadcastMessage("j: " + j + " min: " + minCost + " max: " + maxCost + " lvl: " + level);
				
				if(level >= minCost && level <= maxCost)
				{
					Bukkit.broadcastMessage("added: " + enchantType.name() + " " + j);
					enchantmentInstances.add(new EnchantmentInstance(holder, j));
					break;
				}
			}
		}
		
		return enchantmentInstances;
	}
}
