package com.eul4.wrapper;

import com.eul4.exception.InvalidCryptoInfoException;
import com.google.common.base.Preconditions;
import lombok.Getter;
import org.bukkit.Material;

import java.math.BigDecimal;
import java.util.ArrayList;
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
	
	public List<CryptoInfoTradePreview> createTradePreviews(int amount) throws InvalidCryptoInfoException
	{
		return createTradePreviews(BigDecimal.valueOf(amount));
	}
	
	public List<CryptoInfoTradePreview> createTradePreviews(BigDecimal multiplier) throws InvalidCryptoInfoException
	{
		return createTradePreviews(new ArrayList<>(), multiplier);
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
	
	private List<CryptoInfoTradePreview> createTradePreviews(List<CryptoInfoTradePreview> cryptoInfoTradePreviews, BigDecimal baseMultiplier)
			throws InvalidCryptoInfoException
	{
		for(EconomicMaterialMultiplier economicMaterialMultiplier : economicMaterialMultipliers)
		{
			EconomicMaterial economicMaterial = economicMaterialMultiplier.economicMaterial;
			BigDecimal multiplier = baseMultiplier.multiply(economicMaterialMultiplier.multiplier, CryptoInfo.MATH_CONTEXT);
			
			if(economicMaterial instanceof DerivativeMaterial derivativeMaterial)
			{
				derivativeMaterial.createTradePreviews(cryptoInfoTradePreviews, multiplier);
			}
			else if(economicMaterial instanceof RawMaterial rawMaterial)
			{
				cryptoInfoTradePreviews.add(rawMaterial.createTradePreview(multiplier));
			}
		}
		
		return cryptoInfoTradePreviews;
	}
}
