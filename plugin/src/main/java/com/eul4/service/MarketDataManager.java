package com.eul4.service;

import com.eul4.common.Common;
import com.eul4.wrapper.CryptoInfo;
import com.eul4.wrapper.MaterialMultiplier;
import com.eul4.wrapper.Trade;
import lombok.RequiredArgsConstructor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.math.BigDecimal;
import java.util.*;

@RequiredArgsConstructor
public class MarketDataManager
{
	private final Common plugin;
	
	public static final Map<Material, CryptoInfo> RAW_DATA = new HashMap<>();
	private static final Map<Material, Set<MaterialMultiplier>> DERIVATIVES = new HashMap<>();
	
	static
	{
		RAW_DATA.put(Material.OAK_LOG, new CryptoInfo(BigDecimal.valueOf(1_000_000.0D), BigDecimal.valueOf(1_000_000.0D)));
		RAW_DATA.put(Material.SPRUCE_LOG, new CryptoInfo(BigDecimal.valueOf(1_000_000.0D), BigDecimal.valueOf(1_000_000.0D)));
		RAW_DATA.put(Material.BIRCH_LOG, new CryptoInfo(BigDecimal.valueOf(1_000_000.0D), BigDecimal.valueOf(1_000_000.0D)));
		RAW_DATA.put(Material.JUNGLE_LOG, new CryptoInfo(BigDecimal.valueOf(1_000_000.0D), BigDecimal.valueOf(1_000_000.0D)));
		RAW_DATA.put(Material.ACACIA_LOG, new CryptoInfo(BigDecimal.valueOf(1_000_000.0D), BigDecimal.valueOf(1_000_000.0D)));
		RAW_DATA.put(Material.DARK_OAK_LOG, new CryptoInfo(BigDecimal.valueOf(1_000_000.0D), BigDecimal.valueOf(1_000_000.0D)));
		RAW_DATA.put(Material.MANGROVE_LOG, new CryptoInfo(BigDecimal.valueOf(1_000_000.0D), BigDecimal.valueOf(1_000_000.0D)));
		RAW_DATA.put(Material.CRIMSON_STEM, new CryptoInfo(BigDecimal.valueOf(1_000_000.0D), BigDecimal.valueOf(1_000_000.0D)));
		RAW_DATA.put(Material.WARPED_STEM, new CryptoInfo(BigDecimal.valueOf(1_000_000.0D), BigDecimal.valueOf(1_000_000.0D)));
		
		RAW_DATA.put(Material.STONE, new CryptoInfo(BigDecimal.valueOf(1_000_000.0D), BigDecimal.valueOf(1_000_000.0D)));
		RAW_DATA.put(Material.GRANITE, new CryptoInfo(BigDecimal.valueOf(1_000_000.0D), BigDecimal.valueOf(1_000_000.0D)));
		RAW_DATA.put(Material.ANDESITE, new CryptoInfo(BigDecimal.valueOf(1_000_000.0D), BigDecimal.valueOf(1_000_000.0D)));
		RAW_DATA.put(Material.DIORITE, new CryptoInfo(BigDecimal.valueOf(1_000_000.0D), BigDecimal.valueOf(1_000_000.0D)));
		RAW_DATA.put(Material.CLAY_BALL, new CryptoInfo(BigDecimal.valueOf(1_000_000.0D), BigDecimal.valueOf(1_000_000.0D)));
		RAW_DATA.put(Material.SAND, new CryptoInfo(BigDecimal.valueOf(1_000_000.0D), BigDecimal.valueOf(1_000_000.0D)));
		RAW_DATA.put(Material.RED_SAND, new CryptoInfo(BigDecimal.valueOf(1_000_000.0D), BigDecimal.valueOf(1_000_000.0D)));
		RAW_DATA.put(Material.PRISMARINE_SHARD, new CryptoInfo(BigDecimal.valueOf(1_000_000.0D), BigDecimal.valueOf(1_000_000.0D)));
		RAW_DATA.put(Material.PRISMARINE_CRYSTALS, new CryptoInfo(BigDecimal.valueOf(1_000_000.0D), BigDecimal.valueOf(1_000_000.0D)));
		RAW_DATA.put(Material.NETHERRACK, new CryptoInfo(BigDecimal.valueOf(1_000_000.0D), BigDecimal.valueOf(1_000_000.0D)));
		RAW_DATA.put(Material.BASALT, new CryptoInfo(BigDecimal.valueOf(1_000_000.0D), BigDecimal.valueOf(1_000_000.0D)));
		RAW_DATA.put(Material.BLACKSTONE, new CryptoInfo(BigDecimal.valueOf(1_000_000.0D), BigDecimal.valueOf(1_000_000.0D)));
		RAW_DATA.put(Material.GILDED_BLACKSTONE, new CryptoInfo(BigDecimal.valueOf(1_000_000.0D), BigDecimal.valueOf(1_000_000.0D)));
		RAW_DATA.put(Material.END_STONE, new CryptoInfo(BigDecimal.valueOf(1_000_000.0D), BigDecimal.valueOf(1_000_000.0D)));
		RAW_DATA.put(Material.CHORUS_FRUIT, new CryptoInfo(BigDecimal.valueOf(1_000_000.0D), BigDecimal.valueOf(1_000_000.0D)));
		
		RAW_DATA.put(Material.COAL, new CryptoInfo(BigDecimal.valueOf(1_000_000.0D), BigDecimal.valueOf(1_000_000.0D)));
		RAW_DATA.put(Material.IRON_INGOT, new CryptoInfo(BigDecimal.valueOf(1_000_000.0D), BigDecimal.valueOf(1_000_000.0D)));
		RAW_DATA.put(Material.GOLD_INGOT, new CryptoInfo(BigDecimal.valueOf(1_000_000.0D), BigDecimal.valueOf(1_000_000.0D)));
		RAW_DATA.put(Material.REDSTONE, new CryptoInfo(BigDecimal.valueOf(1_000_000.0D), BigDecimal.valueOf(1_000_000.0D)));
		RAW_DATA.put(Material.EMERALD, new CryptoInfo(BigDecimal.valueOf(1_000_000.0D), BigDecimal.valueOf(1_000_000.0D)));
		RAW_DATA.put(Material.LAPIS_LAZULI, new CryptoInfo(BigDecimal.valueOf(1_000_000.0D), BigDecimal.valueOf(1_000_000.0D)));
		RAW_DATA.put(Material.DIAMOND, new CryptoInfo(BigDecimal.valueOf(1_000_000.0D), BigDecimal.valueOf(1_000_000.0D)));
		RAW_DATA.put(Material.ANCIENT_DEBRIS, new CryptoInfo(BigDecimal.valueOf(1_000_000.0D), BigDecimal.valueOf(1_000_000.0D)));
		RAW_DATA.put(Material.QUARTZ, new CryptoInfo(BigDecimal.valueOf(1_000_000.0D), BigDecimal.valueOf(1_000_000.0D)));
		RAW_DATA.put(Material.AMETHYST_SHARD, new CryptoInfo(BigDecimal.valueOf(1_000_000.0D), BigDecimal.valueOf(1_000_000.0D)));
		RAW_DATA.put(Material.COPPER_INGOT, new CryptoInfo(BigDecimal.valueOf(1_000_000.0D), BigDecimal.valueOf(1_000_000.0D)));
		
		RAW_DATA.put(Material.TERRACOTTA, new CryptoInfo(BigDecimal.valueOf(1_000_000.0D), BigDecimal.valueOf(1_000_000.0D)));
		
		RAW_DATA.put(Material.DIRT, new CryptoInfo(BigDecimal.valueOf(1_000_000.0D), BigDecimal.valueOf(1_000_000.0D)));
		RAW_DATA.put(Material.GRASS_BLOCK, new CryptoInfo(BigDecimal.valueOf(1_000_000.0D), BigDecimal.valueOf(1_000_000.0D)));
		RAW_DATA.put(Material.MYCELIUM, new CryptoInfo(BigDecimal.valueOf(1_000_000.0D), BigDecimal.valueOf(1_000_000.0D)));
		RAW_DATA.put(Material.ICE, new CryptoInfo(BigDecimal.valueOf(1_000_000.0D), BigDecimal.valueOf(1_000_000.0D)));
		RAW_DATA.put(Material.CALCITE, new CryptoInfo(BigDecimal.valueOf(1_000_000.0D), BigDecimal.valueOf(1_000_000.0D)));
		
		DERIVATIVES.put(Material.COBBLESTONE, Set.of(new MaterialMultiplier(Material.STONE, 0.5F)));
		DERIVATIVES.put(Material.FURNACE, Set.of(new MaterialMultiplier(Material.STONE, 0.5F * 8.0F)));
	}
	
	public Set<MaterialMultiplier> getRawDatas(Material material)
	{
		if(RAW_DATA.containsKey(material))
		{
			return Collections.singleton(new MaterialMultiplier(material));
		}
		
		return DERIVATIVES.getOrDefault(material, Collections.emptySet());
	}
	
	public BigDecimal calculatePrice(Material material)
	{
		BigDecimal price = BigDecimal.ZERO;
		
		for(MaterialMultiplier rawData : getRawDatas(material))
		{
			CryptoInfo cryptoInfo = getCryptoInfo(rawData.getMaterial());
			
			if(cryptoInfo == null)
			{
				return null;
			}
			
			price = price.add(cryptoInfo.calculatePrice().multiply(rawData.getMultiplier()));
		}
		
		if(price.compareTo(BigDecimal.ZERO) <= 0)
		{
			return null;
		}
		
		return price;
	}
	
	public Set<Trade> getTrades(ItemStack itemStack)
	{
		final Material material = itemStack.getType();
		final BigDecimal stackAmount = new BigDecimal(itemStack.getAmount());
		
		Set<Trade> trades = new HashSet<>();
		
		for(MaterialMultiplier rawData : getRawDatas(material))
		{
			CryptoInfo cryptoInfo = getCryptoInfo(rawData.getMaterial());
			
			if(cryptoInfo == null)
			{
				return Collections.emptySet();
			}
			
			BigDecimal amount = rawData.getMultiplier().multiply(stackAmount, CryptoInfo.MATH_CONTEXT);
			
			trades.add(new Trade(cryptoInfo, amount));
		}
		
		return trades;
	}
	
	public CryptoInfo getCryptoInfo(Material material)
	{
		return RAW_DATA.get(material);
	}
}
