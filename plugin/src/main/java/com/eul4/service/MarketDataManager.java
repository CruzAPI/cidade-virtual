package com.eul4.service;

import com.eul4.Main;
import com.eul4.economy.Transaction;
import com.eul4.economy.Transfer;
import com.eul4.exception.InvalidCryptoInfoException;
import com.eul4.exception.MaterialNotForSaleException;
import com.eul4.exception.OverCapacityException;
import com.eul4.holder.BigDecimalHolder;
import com.eul4.holder.CapacitatedCrownHolder;
import com.eul4.holder.CrownHolder;
import com.eul4.holder.Holder;
import com.eul4.model.player.PluginPlayer;
import com.eul4.wrapper.*;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.math.BigDecimal;
import java.util.*;

import static org.bukkit.Material.CHEST;
import static org.bukkit.Material.OAK_PLANKS;

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
		
		DerivativeMaterial derivativeOakPlanks = registerDerivative(OAK_PLANKS, rawOakLog.withMultiplier(1.0D / 4.0D));
		DerivativeMaterial derivativeChest = registerDerivative(CHEST, rawOakLog.withMultiplier(2.0D));
//		DerivativeMaterial derivativeChest = registerDerivative(CHEST, derivativeOakPlanks.withMultiplier(8.0D));
		
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
	
	public Transaction createTransaction(PluginPlayer pluginPlayer, ItemStack itemStack)
			throws MaterialNotForSaleException, InvalidCryptoInfoException, OverCapacityException
	{
		List<CryptoInfoTradePreview> cryptoInfoTradePreviews = createTradePreviews(itemStack);
		List<CapacitatedCrownHolder> holders = pluginPlayer.getTown().getCapacitatedCrownHolders();
		
		return plugin.getTransactionManager().createTransaction(cryptoInfoTradePreviews, holders);
	}
	
	private List<CryptoInfoTradePreview> createTradePreviews(ItemStack itemStack)
			throws InvalidCryptoInfoException, MaterialNotForSaleException
	{
		Material material = itemStack.getType();
		
		if(derivates.containsKey(material))
		{
			return derivates.get(material).createTradePreviews(itemStack.getAmount());
		}
		else if(getRawMaterialMap().containsKey(material))
		{
			return Collections.singletonList(getRawMaterialMap().get(material).createTradePreview(itemStack.getAmount()));
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
