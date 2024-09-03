package com.eul4.listener;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketContainer;
import com.destroystokyo.paper.event.block.AnvilDamagedEvent;
import com.eul4.Main;
import com.eul4.common.util.ItemStackUtil;
import com.eul4.common.wrapper.Pitch;
import com.eul4.enums.Rarity;
import com.eul4.util.RarityUtil;
import com.eul4.util.SoundUtil;
import com.eul4.wrapper.EnchantType;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.Component;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.Tag;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.BlockInventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.Repairable;
import org.bukkit.inventory.view.AnvilView;
import org.jetbrains.annotations.NotNull;

import java.util.*;

import static com.eul4.common.util.ItemStackUtil.*;

@RequiredArgsConstructor
public class AnvilRarityListener implements Listener
{
	private final Main plugin;
	
	private final Random random = new Random();
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onAnvilDamaged(AnvilDamagedEvent event)
	{
		AnvilInventory anvilInventory = event.getInventory();
		
		if(!(anvilInventory.getHolder() instanceof BlockInventoryHolder blockInventoryHolder))
		{
			return;
		}
		
		Block anvil = blockInventoryHolder.getBlock();
		Rarity anvilRarity = RarityUtil.getRarity(plugin, anvil);
		
		int nextInt = random.nextInt(anvilRarity.getAnvilDurabilityRandomBound());
		
		if(nextInt != 0)
		{
			anvil.getWorld().playSound(anvil.getLocation(), Sound.BLOCK_ANVIL_USE, 1.0F, Pitch.getPitch(12));
			event.setCancelled(true);
		}
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void calculateBaseCost(PrepareAnvilEvent event)
	{
		AnvilView anvilView = event.getView();
		AnvilInventory anvilInventory = event.getInventory();
		
		ItemStack firstItem = anvilInventory.getFirstItem();
		ItemStack secondItem = anvilInventory.getSecondItem();
		
		if(firstItem == null)
		{
			return;
		}
		
		if(!(anvilInventory.getHolder() instanceof BlockInventoryHolder blockInventoryHolder))
		{
			return;
		}
		
		Rarity anvilRarity = RarityUtil.getRarity(plugin, blockInventoryHolder.getBlock());
		
		ItemMeta firstMeta = firstItem.getItemMeta();
		ItemMeta secondMeta = secondItem == null ? null : secondItem.getItemMeta();
		
		int firstCost = firstMeta instanceof Repairable repairable ? repairable.getRepairCost() : 0;
		int secondCost = secondMeta instanceof Repairable repairable ? repairable.getRepairCost() : 0;
		
		anvilView.setMaximumRepairCost(anvilRarity.getAnvilMaxRepairCost());
		anvilView.setRepairCost(firstCost + secondCost);
		
		event.setResult(firstItem.clone());
	}
	
	@EventHandler(priority = EventPriority.VERY_LOW)
	public void calculateDurability(PrepareAnvilEvent event)
	{
		AnvilView anvilView = event.getView();
		AnvilInventory anvilInventory = event.getInventory();
		
		ItemStack first = anvilInventory.getFirstItem();
		ItemStack second = anvilInventory.getSecondItem();
		ItemStack result = event.getResult();
		
		if(first == null || second == null || result == null)
		{
			return;
		}
		
		if(!(first.getItemMeta() instanceof Damageable firstDamageable
				&& result.getItemMeta() instanceof Damageable resultDamageable))
		{
			event.setResult(null);
			return;
		}
		
		if(!firstDamageable.hasDamage())
		{
			event.setResult(null);
			return;
		}
		
		if(first.getType() == second.getType()
				&& second.getItemMeta() instanceof Damageable secondDamageable)
		{
			Rarity firstRarity = RarityUtil.getRarity(first);
			Rarity secondRarity = RarityUtil.getRarity(second);
			
			if(firstRarity != secondRarity)
			{
				event.setResult(null);
				anvilView.setRepairItemCountCost(0);
				anvilView.setRepairCost(0);
				return;
			}
			
			int firstMaxDamage = ItemStackUtil.getMaxDamage(first);
			int secondMaxDamage = ItemStackUtil.getMaxDamage(second);
			
			int value = (int) (firstDamageable.getDamage()
					- (secondMaxDamage - secondDamageable.getDamage())
					- firstMaxDamage * 0.12D);
			
			ItemStackUtil.setDamage(result, value);
			
			anvilView.setRepairItemCountCost(1);
			anvilView.setRepairCost(anvilView.getRepairCost() + 2);
			return;
		}
		
		Set<Material> types = getRepairingMaterial(first.getType());
		
		if(types.contains(second.getType()))
		{
			Rarity firstRarity = RarityUtil.getRarity(first);
			Rarity secondRarity = RarityUtil.getRarity(second);
			
			double multiplier = firstRarity.getRelativeDurabilityMultiplier(first.getType(), secondRarity);
			
			int limit = ItemStackUtil.getMaxDamage(first);
			double points = Math.ceil(limit / (multiplier * 4.0D));
			
			int maxMaterials = (int) Math.ceil(firstDamageable.getDamage() / points);
			int itemCountCost = Math.min(second.getAmount(), maxMaterials);
			
			resultDamageable.setDamage((int) Math.max(0.0D, resultDamageable.getDamage() - points * itemCountCost));
			result.setItemMeta(resultDamageable);
			
			anvilView.setRepairItemCountCost(itemCountCost);
			anvilView.setRepairCost(anvilView.getRepairCost() + itemCountCost);
			return;
		}
		
		event.setResult(null);
	}
	
	@EventHandler(priority = EventPriority.LOW)
	public void calculateEnchantments(PrepareAnvilEvent event)
	{
		AnvilView anvilView = event.getView();
		AnvilInventory anvilInventory = event.getInventory();
		
		ItemStack first = anvilInventory.getFirstItem();
		ItemStack second = anvilInventory.getSecondItem();
		ItemStack result = event.getResult();
		
		if(first == null || second == null)
		{
			return;
		}
		
		Map<Enchantment, Integer> firstEnchantments = getEnchantments(first);
		Map<Enchantment, Integer> secondEnchantments = getEnchantments(second);
		
		if(secondEnchantments.isEmpty() || !canHoldEnchants(first) || !canHoldEnchants(second))
		{
			return;
		}
		
		boolean isBook = second.getType() == Material.ENCHANTED_BOOK;
		
		if(first.getType() != second.getType() && !isBook)
		{
			return;
		}
		
		Rarity minRarity = RarityUtil.getMinRarity(anvilInventory.getStorageContents());
		
		secondLoop:for(Map.Entry<Enchantment, Integer> secondEntry : secondEnchantments.entrySet())
		{
			Enchantment secondEnchantment = secondEntry.getKey();
			int secondLevel = secondEntry.getValue();
			EnchantType secondEnchantType = EnchantType.fromBukkit(secondEnchantment);
			
			if(!secondEnchantType.canEnchantItem(first))
			{
				continue;
			}
			
			for(Enchantment firstEnchantment : firstEnchantments.keySet())
			{
				EnchantType firstEnchantType = EnchantType.fromBukkit(firstEnchantment);
				
				if(firstEnchantType.conflictsWith(secondEnchantType) && firstEnchantType != secondEnchantType)
				{
					continue secondLoop;
				}
			}
			
			EnchantType enchantType = EnchantType.fromBukkit(secondEnchantment);
			
			int firstLevel = firstEnchantments.getOrDefault(secondEnchantment, 0);
			int finalLevel = Math.min(enchantType.getMaxLevel(minRarity), firstLevel == secondLevel
					? firstLevel + 1
					: Math.max(firstLevel, secondLevel));
			
			int anvilCost = secondEnchantment.getAnvilCost();
			
			anvilCost /= isBook ? 2 : 1;
			anvilCost = Math.max(1, anvilCost);
			anvilCost *= finalLevel;
			
			anvilView.setRepairCost(anvilView.getRepairCost() + anvilCost);
			result = result == null ? first.clone() : result;
			addEnchant(result, secondEnchantment, finalLevel, true);
		}
		
		event.setResult(result);
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void calculateRename(PrepareAnvilEvent event)
	{
		AnvilView anvilView = event.getView();
		AnvilInventory anvilInventory = event.getInventory();
		
		ItemStack first = anvilInventory.getFirstItem();
		ItemStack result = event.getResult();
		String renameText = anvilView.getRenameText();
		renameText = renameText != null && renameText.isEmpty() ? null : renameText;
		
		if(first == null)
		{
			return;
		}
		
		result = result == null ? first.clone() : result;
		
		ItemMeta firstMeta = first.getItemMeta();
		ItemMeta resultMeta = result.getItemMeta();
		
		if(firstMeta == null || resultMeta == null)
		{
			return;
		}
		
		Component renamedComponent = renameText == null ? null : Component.text(renameText);
		
		if(Objects.equals(firstMeta.displayName(), renamedComponent))
		{
			return;
		}
		
		resultMeta.displayName(renamedComponent);
		result.setItemMeta(resultMeta);
		
		event.setResult(result);
		anvilView.setRepairCost(anvilView.getRepairCost() + 1);
	}
	
	@EventHandler(priority = EventPriority.VERY_HIGH)
	public void validadeRarityAndIncrementResultRepairCost(PrepareAnvilEvent event)
	{
		AnvilView anvilView = event.getView();
		AnvilInventory anvilInventory = event.getInventory();
		
		ItemStack first = anvilInventory.getFirstItem();
		ItemStack second = anvilInventory.getSecondItem();
		ItemStack result = event.getResult();
		
		if(first == null || result == null)
		{
			return;
		}
		
		if(first.equals(result) && second == null)
		{
			event.setResult(null);
			anvilView.setRepairCost(0);
			anvilView.setRepairItemCountCost(0);
			return;
		}
		
		if(!isUnitRepairing(first, second))
		{
			if(second != null)
			{
				Rarity firstRarity = RarityUtil.getRarity(first);
				Rarity secondRarity = RarityUtil.getRarity(second);
				
				if(firstRarity != secondRarity)
				{
					event.setResult(null);
					anvilView.setRepairCost(0);
					anvilView.setRepairItemCountCost(0);
					return;
				}
			}
		}
		
		if(result.getItemMeta() instanceof Repairable resultRepairable && !wasJustRenamed(event))
		{
			int firstRepairCost = getRepairCost(first);
			int secondRepairCost = getRepairCost(second);
			
			int repairCost = Math.max(firstRepairCost, secondRepairCost);
			repairCost = repairCost * 2 + 1;
			
			resultRepairable.setRepairCost(repairCost);
			result.setItemMeta(resultRepairable);
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onClickAnvilResult(InventoryClickEvent event)
	{
		if(!(event.getWhoClicked() instanceof Player player)
				|| !(event.getView() instanceof AnvilView anvilView))
		{
			return;
		}
		
		if(event.getRawSlot() != 2)
		{
			return;
		}
		
		if(player.getGameMode() != GameMode.CREATIVE
				&& (anvilView.getRepairCost() > anvilView.getMaximumRepairCost()
				|| player.getLevel() < anvilView.getRepairCost()))
		{
			SoundUtil.playPlong(player);
			event.setCancelled(true);
		}
	}
	
	private boolean wasJustRenamed(PrepareAnvilEvent event)
	{
		AnvilInventory anvilInventory = event.getInventory();
		
		ItemStack first = anvilInventory.getFirstItem();
		ItemStack result = event.getResult();
		
		if(first == null || result == null)
		{
			return false;
		}
		
		ItemMeta firstMeta = first.getItemMeta();
		
		if(firstMeta == null)
		{
			return false;
		}
		
		ItemStack resultClone = result.clone();
		ItemMeta resultCloneMeta = resultClone.getItemMeta();
		
		if(resultCloneMeta == null)
		{
			return false;
		}
		
		resultCloneMeta.displayName(firstMeta.displayName());
		resultClone.setItemMeta(resultCloneMeta);
		
		return resultClone.equals(first);
	}
	
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void updateInventory(PrepareAnvilEvent event)
	{
		AnvilView anvilView = event.getView();
		
		event.getViewers().forEach(human ->
		{
			setInstantBuild((Player) human, anvilView.getRepairCost() <= anvilView.getMaximumRepairCost());
		});
		
		plugin.getServer().getScheduler().getMainThreadExecutor(plugin).execute
		(
			() -> event.getViewers().forEach(human -> ((Player) human).updateInventory())
		);
	}
	
	private boolean isUnitRepairing(ItemStack first, ItemStack second)
	{
		return first != null && second != null && getRepairingMaterial(first.getType()).contains(second.getType());
	}
	
	private Set<Material> getRepairingMaterial(Material tool)
	{
		switch(tool)
		{
		case WOODEN_SWORD:
		case WOODEN_PICKAXE:
		case WOODEN_AXE:
		case WOODEN_SHOVEL:
		case WOODEN_HOE:
		case SHIELD:
			return Tag.PLANKS.getValues();
		case STONE_SWORD:
		case STONE_PICKAXE:
		case STONE_AXE:
		case STONE_SHOVEL:
		case STONE_HOE:
			return Set.of(Material.COBBLESTONE, Material.COBBLED_DEEPSLATE, Material.BLACKSTONE);
		case IRON_HELMET:
		case IRON_CHESTPLATE:
		case IRON_LEGGINGS:
		case IRON_BOOTS:
		case IRON_SWORD:
		case IRON_PICKAXE:
		case IRON_AXE:
		case IRON_SHOVEL:
		case IRON_HOE:
			return Set.of(Material.IRON_INGOT);
		case GOLDEN_HELMET:
		case GOLDEN_CHESTPLATE:
		case GOLDEN_LEGGINGS:
		case GOLDEN_BOOTS:
		case GOLDEN_SWORD:
		case GOLDEN_PICKAXE:
		case GOLDEN_AXE:
		case GOLDEN_SHOVEL:
		case GOLDEN_HOE:
			return Set.of(Material.GOLD_INGOT);
		case DIAMOND_HELMET:
		case DIAMOND_CHESTPLATE:
		case DIAMOND_LEGGINGS:
		case DIAMOND_BOOTS:
		case DIAMOND_SWORD:
		case DIAMOND_PICKAXE:
		case DIAMOND_AXE:
		case DIAMOND_SHOVEL:
		case DIAMOND_HOE:
			return Set.of(Material.DIAMOND);
		case NETHERITE_HELMET:
		case NETHERITE_CHESTPLATE:
		case NETHERITE_LEGGINGS:
		case NETHERITE_BOOTS:
		case NETHERITE_SWORD:
		case NETHERITE_PICKAXE:
		case NETHERITE_AXE:
		case NETHERITE_SHOVEL:
		case NETHERITE_HOE:
			return Set.of(Material.NETHERITE_INGOT);
		case TURTLE_HELMET:
			return Set.of(Material.TURTLE_SCUTE);
		case ELYTRA:
			return Set.of(Material.PHANTOM_MEMBRANE);
		case MACE:
			return Set.of(Material.BREEZE_ROD);
		default:
			return Collections.emptySet();
		}
	}
	
	private static boolean canHoldEnchants(ItemStack item)
	{
		return item.getType().getMaxDurability() != 0 || item.getType() == Material.ENCHANTED_BOOK;
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	private void onInventoryOpen(@NotNull InventoryOpenEvent event)
	{
		if(event.getView() instanceof AnvilView
				&& event.getPlayer() instanceof Player player
				&& player.getGameMode() != GameMode.CREATIVE)
		{
			setInstantBuild(player, true);
		}
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	private void onInventoryClose(@NotNull InventoryCloseEvent event)
	{
		if(event.getInventory() instanceof AnvilInventory
				&& event.getPlayer() instanceof Player player
				&& player.getGameMode() != GameMode.CREATIVE)
		{
			setInstantBuild(player, false);
		}
	}
	
	public void setInstantBuild(@NotNull Player player, boolean instantBuild)
	{
		PacketContainer packet = new PacketContainer(PacketType.Play.Server.ABILITIES);
		packet.getBooleans().write(0, player.isInvulnerable());
		packet.getBooleans().write(1, player.isFlying());
		packet.getBooleans().write(2, player.getAllowFlight());
		packet.getBooleans().write(3, instantBuild);
		packet.getFloat().write(0, player.getFlySpeed() / 2);
		packet.getFloat().write(1, player.getWalkSpeed() / 2);
		
		ProtocolLibrary.getProtocolManager().sendServerPacket(player, packet);
	}
}
