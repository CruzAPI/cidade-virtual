package com.eul4.wrapper;

import com.eul4.exception.InvalidCryptoInfoException;
import com.eul4.exception.NegativeBalanceException;
import com.google.common.base.Preconditions;
import lombok.Getter;
import org.bukkit.Material;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Getter
public class DerivativeMaterial extends EconomicMaterial
{
	private final List<EconomicMaterialMultiplier> economicMaterialMultipliers;
	
	public DerivativeMaterial(Material material, List<EconomicMaterialMultiplier> economicMaterialMultipliers)
	{
		super(material);
		
		Preconditions.checkArgument(!economicMaterialMultipliers.isEmpty());
		
		this.economicMaterialMultipliers = economicMaterialMultipliers;
	}
	
	public ItemStackTradePreview createTradePreviews(BigDecimal holderRemainingCapacity, int amount) throws InvalidCryptoInfoException
	{
		BigDecimal actualAmount = BigDecimal.valueOf(amount);
		BigDecimal minAmount;
		
		try
		{
			BigDecimal maxAmount = previewCirculatingSupplyDifferenceByMarketCap(holderRemainingCapacity.negate());
			
			maxAmount = maxAmount.setScale(0, RoundingMode.DOWN);
			minAmount = maxAmount.compareTo(actualAmount) < 0 ? maxAmount : actualAmount;
		}
		catch(NegativeBalanceException e)
		{
			minAmount = actualAmount;
		}
		
		minAmount = minAmount.compareTo(BigDecimal.ONE) < 0
				? BigDecimal.ONE
				: minAmount;
		
		return createTradePreviews(minAmount);
	}
	
	public ItemStackTradePreview createTradePreviews(int amount) throws InvalidCryptoInfoException
	{
		return createTradePreviews(BigDecimal.valueOf(amount));
	}
	
	public ItemStackTradePreview createTradePreviews(BigDecimal multiplier) throws InvalidCryptoInfoException
	{
		return createTradePreviews(new ItemStackTradePreview(multiplier), multiplier);
	}
	
	
	
	public BigDecimal previewCirculatingSupplyDifferenceByMarketCap(BigDecimal marketCapAugend)
			throws InvalidCryptoInfoException, NegativeBalanceException
	{
		BigDecimal price = calculatePrice();
		BigDecimal marketCap = calculateMarketCap();
		BigDecimal circulatingSupply = marketCap.divide(price, CryptoInfo.MATH_CONTEXT);
		
		CryptoInfo fakeCrypto = new CryptoInfo(marketCap, circulatingSupply);
		
		fakeCrypto.add(marketCapAugend);
		
		return fakeCrypto.getCirculatingSupply().subtract(circulatingSupply);
	}
	
	private BigDecimal calculateMarketCap() throws InvalidCryptoInfoException
	{
		return calculateMarketCap(BigDecimal.ZERO);
	}
	
	private BigDecimal calculateMarketCap(BigDecimal totalMarketCap) throws InvalidCryptoInfoException
	{
		for(EconomicMaterialMultiplier economicMaterialMultiplier : economicMaterialMultipliers)
		{
			EconomicMaterial economicMaterial = economicMaterialMultiplier.economicMaterial;
			
			if(economicMaterial instanceof DerivativeMaterial derivativeMaterial)
			{
				derivativeMaterial.calculateMarketCap(totalMarketCap);
			}
			else if(economicMaterial instanceof RawMaterial rawMaterial)
			{
				BigDecimal marketCap = rawMaterial.getCryptoInfo().getMarketCap();
				totalMarketCap = totalMarketCap.add(marketCap);
			}
		}
		
		return totalMarketCap;
	}
	
	public BigDecimal calculatePrice() throws InvalidCryptoInfoException
	{
		return calculatePrice(BigDecimal.ZERO, BigDecimal.ONE);
	}
	
	private BigDecimal calculatePrice(BigDecimal totalPrice, BigDecimal baseMultiplier)
			throws InvalidCryptoInfoException
	{
		for(EconomicMaterialMultiplier economicMaterialMultiplier : economicMaterialMultipliers)
		{
			EconomicMaterial economicMaterial = economicMaterialMultiplier.economicMaterial;
			BigDecimal multiplier = baseMultiplier.multiply(economicMaterialMultiplier.multiplier, CryptoInfo.MATH_CONTEXT);
			
			if(economicMaterial instanceof DerivativeMaterial derivativeMaterial)
			{
				derivativeMaterial.calculatePrice(totalPrice, multiplier);
			}
			else if(economicMaterial instanceof RawMaterial rawMaterial)
			{
				BigDecimal price = rawMaterial.getCryptoInfo().calculatePrice().multiply(multiplier, CryptoInfo.MATH_CONTEXT);
				totalPrice = totalPrice.add(price);
			}
		}
		
		return totalPrice;
	}
	
	private ItemStackTradePreview createTradePreviews
	(
		ItemStackTradePreview itemStackTradePreview,
		BigDecimal baseMultiplier
	)
	throws InvalidCryptoInfoException
	{
		for(EconomicMaterialMultiplier economicMaterialMultiplier : economicMaterialMultipliers)
		{
			EconomicMaterial economicMaterial = economicMaterialMultiplier.economicMaterial;
			BigDecimal multiplier = baseMultiplier.multiply(economicMaterialMultiplier.multiplier, CryptoInfo.MATH_CONTEXT);
			
			if(economicMaterial instanceof DerivativeMaterial derivativeMaterial)
			{
				derivativeMaterial.createTradePreviews(itemStackTradePreview, multiplier);
			}
			else if(economicMaterial instanceof RawMaterial rawMaterial)
			{
				itemStackTradePreview.getPreviews().add(rawMaterial.createTradePreview(multiplier));
			}
		}
		
		return itemStackTradePreview;
	}
}
