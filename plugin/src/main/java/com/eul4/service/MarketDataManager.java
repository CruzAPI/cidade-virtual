package com.eul4.service;

import com.eul4.Main;
import com.eul4.economy.ItemStackTransaction;
import com.eul4.economy.Transaction;
import com.eul4.enums.Rarity;
import com.eul4.exception.InvalidCryptoInfoException;
import com.eul4.exception.MaterialNotForSaleException;
import com.eul4.exception.OverCapacityException;
import com.eul4.holder.CapacitatedCrownHolder;
import com.eul4.model.player.PluginPlayer;
import com.eul4.model.town.Town;
import com.eul4.util.RarityUtil;
import com.eul4.wrapper.*;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.bukkit.Material.*;

public class MarketDataManager
{
	private final Main plugin;
	
	private final Map<Material, DerivativeMaterial> derivates = new HashMap<>();
	
	private boolean derivativeRegistered;
	
	static
	{
//		new RawMaterial()
//		RAW_MATERIALS.put(Material.OAK_LOG, new CryptoInfo(BigDecimal.valueOf(1_000_000.0D), BigDecimal.valueOf(1_000_000.0D)));
//		RAW_MATERIALS.put(Material.SPRUCE_LOG, new CryptoInfo(BigDecimal.valueOf(1_000_000.0D), BigDecimal.valueOf(1_000_000.0D)));
//		RAW_MATERIALS.put(Material.BIRCH_LOG, new CryptoInfo(BigDecimal.valueOf(1_000_000.0D), BigDecimal.valueOf(1_000_000.0D)));
//		RAW_MATERIALS.put(Material.JUNGLE_LOG, new CryptoInfo(BigDecimal.valueOf(1_000_000.0D), BigDecimal.valueOf(1_000_000.0D)));
//		RAW_MATERIALS.put(Material.ACACIA_LOG, new CryptoInfo(BigDecimal.valueOf(1_000_000.0D), BigDecimal.valueOf(1_000_000.0D)));
//		RAW_MATERIALS.put(Material.DARK_OAK_LOG, new CryptoInfo(BigDecimal.valueOf(1_000_000.0D), BigDecimal.valueOf(1_000_000.0D)));
//		RAW_MATERIALS.put(Material.MANGROVE_LOG, new CryptoInfo(BigDecimal.valueOf(1_000_000.0D), BigDecimal.valueOf(1_000_000.0D)));
//		RAW_MATERIALS.put(Material.CRIMSON_STEM, new CryptoInfo(BigDecimal.valueOf(1_000_000.0D), BigDecimal.valueOf(1_000_000.0D)));
//		RAW_MATERIALS.put(Material.WARPED_STEM, new CryptoInfo(BigDecimal.valueOf(1_000_000.0D), BigDecimal.valueOf(1_000_000.0D)));
//
//		RAW_MATERIALS.put(Material.STONE, new CryptoInfo(BigDecimal.valueOf(1_000_000.0D), BigDecimal.valueOf(1_000_000.0D)));
//		RAW_MATERIALS.put(Material.GRANITE, new CryptoInfo(BigDecimal.valueOf(1_000_000.0D), BigDecimal.valueOf(1_000_000.0D)));
//		RAW_MATERIALS.put(Material.ANDESITE, new CryptoInfo(BigDecimal.valueOf(1_000_000.0D), BigDecimal.valueOf(1_000_000.0D)));
//		RAW_MATERIALS.put(Material.DIORITE, new CryptoInfo(BigDecimal.valueOf(1_000_000.0D), BigDecimal.valueOf(1_000_000.0D)));
//		RAW_MATERIALS.put(Material.CLAY_BALL, new CryptoInfo(BigDecimal.valueOf(1_000_000.0D), BigDecimal.valueOf(1_000_000.0D)));
//		RAW_MATERIALS.put(Material.SAND, new CryptoInfo(BigDecimal.valueOf(1_000_000.0D), BigDecimal.valueOf(1_000_000.0D)));
//		RAW_MATERIALS.put(Material.RED_SAND, new CryptoInfo(BigDecimal.valueOf(1_000_000.0D), BigDecimal.valueOf(1_000_000.0D)));
//		RAW_MATERIALS.put(Material.PRISMARINE_SHARD, new CryptoInfo(BigDecimal.valueOf(1_000_000.0D), BigDecimal.valueOf(1_000_000.0D)));
//		RAW_MATERIALS.put(Material.PRISMARINE_CRYSTALS, new CryptoInfo(BigDecimal.valueOf(1_000_000.0D), BigDecimal.valueOf(1_000_000.0D)));
//		RAW_MATERIALS.put(Material.NETHERRACK, new CryptoInfo(BigDecimal.valueOf(1_000_000.0D), BigDecimal.valueOf(1_000_000.0D)));
//		RAW_MATERIALS.put(Material.BASALT, new CryptoInfo(BigDecimal.valueOf(1_000_000.0D), BigDecimal.valueOf(1_000_000.0D)));
//		RAW_MATERIALS.put(Material.BLACKSTONE, new CryptoInfo(BigDecimal.valueOf(1_000_000.0D), BigDecimal.valueOf(1_000_000.0D)));
//		RAW_MATERIALS.put(Material.GILDED_BLACKSTONE, new CryptoInfo(BigDecimal.valueOf(1_000_000.0D), BigDecimal.valueOf(1_000_000.0D)));
//		RAW_MATERIALS.put(Material.END_STONE, new CryptoInfo(BigDecimal.valueOf(1_000_000.0D), BigDecimal.valueOf(1_000_000.0D)));
//		RAW_MATERIALS.put(Material.CHORUS_FRUIT, new CryptoInfo(BigDecimal.valueOf(1_000_000.0D), BigDecimal.valueOf(1_000_000.0D)));
//
//		RAW_MATERIALS.put(Material.COAL, new CryptoInfo(BigDecimal.valueOf(1_000_000.0D), BigDecimal.valueOf(1_000_000.0D)));
//		RAW_MATERIALS.put(Material.IRON_INGOT, new CryptoInfo(BigDecimal.valueOf(1_000_000.0D), BigDecimal.valueOf(1_000_000.0D)));
//		RAW_MATERIALS.put(Material.GOLD_INGOT, new CryptoInfo(BigDecimal.valueOf(1_000_000.0D), BigDecimal.valueOf(1_000_000.0D)));
//		RAW_MATERIALS.put(Material.REDSTONE, new CryptoInfo(BigDecimal.valueOf(1_000_000.0D), BigDecimal.valueOf(1_000_000.0D)));
//		RAW_MATERIALS.put(Material.EMERALD, new CryptoInfo(BigDecimal.valueOf(1_000_000.0D), BigDecimal.valueOf(1_000_000.0D)));
//		RAW_MATERIALS.put(Material.LAPIS_LAZULI, new CryptoInfo(BigDecimal.valueOf(1_000_000.0D), BigDecimal.valueOf(1_000_000.0D)));
//		RAW_MATERIALS.put(Material.DIAMOND, new CryptoInfo(BigDecimal.valueOf(1_000_000.0D), BigDecimal.valueOf(1_000_000.0D)));
//		RAW_MATERIALS.put(Material.ANCIENT_DEBRIS, new CryptoInfo(BigDecimal.valueOf(1_000_000.0D), BigDecimal.valueOf(1_000_000.0D)));
//		RAW_MATERIALS.put(Material.QUARTZ, new CryptoInfo(BigDecimal.valueOf(1_000_000.0D), BigDecimal.valueOf(1_000_000.0D)));
//		RAW_MATERIALS.put(Material.AMETHYST_SHARD, new CryptoInfo(BigDecimal.valueOf(1_000_000.0D), BigDecimal.valueOf(1_000_000.0D)));
//		RAW_MATERIALS.put(Material.COPPER_INGOT, new CryptoInfo(BigDecimal.valueOf(1_000_000.0D), BigDecimal.valueOf(1_000_000.0D)));
//
//		RAW_MATERIALS.put(Material.TERRACOTTA, new CryptoInfo(BigDecimal.valueOf(1_000_000.0D), BigDecimal.valueOf(1_000_000.0D)));
//
//		RAW_MATERIALS.put(Material.DIRT, new CryptoInfo(BigDecimal.valueOf(1_000_000.0D), BigDecimal.valueOf(1_000_000.0D)));
//		RAW_MATERIALS.put(Material.GRASS_BLOCK, new CryptoInfo(BigDecimal.valueOf(1_000_000.0D), BigDecimal.valueOf(1_000_000.0D)));
//		RAW_MATERIALS.put(Material.MYCELIUM, new CryptoInfo(BigDecimal.valueOf(1_000_000.0D), BigDecimal.valueOf(1_000_000.0D)));
//		RAW_MATERIALS.put(Material.ICE, new CryptoInfo(BigDecimal.valueOf(1_000_000.0D), BigDecimal.valueOf(1_000_000.0D)));
//		RAW_MATERIALS.put(Material.CALCITE, new CryptoInfo(BigDecimal.valueOf(1_000_000.0D), BigDecimal.valueOf(1_000_000.0D)));
//
//		DERIVATIVES.put(Material.COBBLESTONE, Set.of(new MaterialMultiplier(Material.STONE, 0.5F)));
//		DERIVATIVES.put(Material.FURNACE, Set.of(new MaterialMultiplier(Material.STONE, 0.5F * 8.0F)));
	}
	
	public MarketDataManager(final Main plugin)
	{
		this.plugin = plugin;
	}
	
	public void registerDerivatives() throws Exception
	{
		if(derivativeRegistered)
		{
			throw new Exception("Derivatives already registered");
		}
		
		RawMaterialMap rawMaterialMap = plugin.getRawMaterialMapFiler().getRawMaterialMap();
		
		RawMaterial rawOakLog = rawMaterialMap.get(Material.OAK_LOG);
		RawMaterial rawEnderPearl = rawMaterialMap.get(Material.ENDER_PEARL);
		RawMaterial rawBlazePowder = rawMaterialMap.get(Material.BLAZE_POWDER);
		
		rawEnderPearl.getCryptoInfo().setMarketCap(new BigDecimal("1000000"));
		rawEnderPearl.getCryptoInfo().setCirculatingSupply(new BigDecimal("10000"));
		
		rawBlazePowder.getCryptoInfo().setMarketCap(new BigDecimal("5000"));
		rawBlazePowder.getCryptoInfo().setCirculatingSupply(new BigDecimal("5000"));
		
		DerivativeMaterial derivativeOakPlanks = registerDerivative(OAK_PLANKS,
				rawOakLog.withMultiplier(1.0D / 4.0D));
		DerivativeMaterial derivativeChest = registerDerivative(CHEST,
				derivativeOakPlanks.withMultiplier(8.0D));
		
		DerivativeMaterial derivativeEnderEye = registerDerivative(ENDER_EYE,
				rawEnderPearl.withMultiplier(1.0D),
				rawBlazePowder.withMultiplier(1.0D));
		
		derivativeRegistered = true;
	}
	
	private DerivativeMaterial registerDerivative(Material material, EconomicMaterialMultiplier... economicMaterialMultipliers)
	{
		return registerDerivative(new DerivativeMaterial(material, List.of(economicMaterialMultipliers)));
	}
	
	private DerivativeMaterial registerDerivative(DerivativeMaterial derivativeMaterial)
	{
		derivates.put(derivativeMaterial.getMaterial(), derivativeMaterial);
		return derivativeMaterial;
	}
	
	public BigDecimal calculatePrice(Material material) throws MaterialNotForSaleException, InvalidCryptoInfoException
	{
		if(derivates.containsKey(material))
		{
			return derivates.get(material).calculatePrice();
		}
		
		if(getRawMaterialMap().containsKey(material))
		{
			return getRawMaterialMap().get(material).getCryptoInfo().calculatePrice();
		}
		
		throw new MaterialNotForSaleException();
	}
	
	public ItemStackTransaction createItemStackTransaction(PluginPlayer pluginPlayer, int slot)
			throws OverCapacityException, InvalidCryptoInfoException, MaterialNotForSaleException
	{
		Town town = pluginPlayer.getTown();
		
		final Inventory inventory = pluginPlayer.getPlayer().getInventory();
		final ItemStack itemStack = Optional.ofNullable(inventory.getItem(slot)).orElse(ItemStack.empty());
		
		ItemStackTradePreview itemStackTradePreview = createTradePreviews(
				town.calculateRemainingCrownCapacity(),
				itemStack);
		List<CapacitatedCrownHolder> holders = pluginPlayer.getTown().getCapacitatedCrownHolders();
		
		int amountToConsume = itemStackTradePreview.getAmountToConsume();
		
		Transaction<?> transaction = plugin.getTransactionManager()
				.createTransaction(itemStackTradePreview.getPreviews(), holders);
		
		return new ItemStackTransaction(transaction, inventory, slot, amountToConsume);
	}
	
	private ItemStackTradePreview createTradePreviews
	(
		BigDecimal holderRemainingCapacity,
		ItemStack itemStack
	)
	throws InvalidCryptoInfoException, MaterialNotForSaleException
	{
		Material material = itemStack.getType();
		Rarity rarity = RarityUtil.getRarity(itemStack);
		BigDecimal rarityMultiplier = BigDecimal.valueOf(rarity.getScalarMultiplier(10));
		
		if(derivates.containsKey(material))
		{
			return derivates.get(material)
					.createTradePreviews
					(
						holderRemainingCapacity,
						itemStack.getAmount(),
						rarityMultiplier
					);
		}
		else if(getRawMaterialMap().containsKey(material))
		{
			return getRawMaterialMap()
					.get(material)
					.createTradePreview
					(
						holderRemainingCapacity,
						itemStack.getAmount(),
						rarityMultiplier
					);
		}
		else
		{
			throw new MaterialNotForSaleException();
		}
	}
	
	public RawMaterialMap getRawMaterialMap()
	{
		return plugin.getRawMaterialMapFiler().getRawMaterialMap();
	}
}
