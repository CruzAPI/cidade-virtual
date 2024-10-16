package com.eul4.wrapper;

import com.eul4.exception.InvalidCryptoInfoException;
import com.eul4.exception.NegativeBalanceException;
import com.google.common.base.Preconditions;
import lombok.Getter;
import org.bukkit.Material;

import java.math.BigDecimal;

import static com.eul4.wrapper.CryptoInfo.MATH_CONTEXT;

@Getter
public class RawMaterial extends EconomicMaterial
{
	private final CryptoInfo cryptoInfo;
	
	public RawMaterial(Material material, CryptoInfo cryptoInfo)
	{
		super(material);
		this.cryptoInfo = Preconditions.checkNotNull(cryptoInfo);
	}
	
	public CryptoInfoTradePreview createTradePreview(int amount) throws InvalidCryptoInfoException
	{
		return createTradePreview(BigDecimal.valueOf(amount));
	}
	
	public CryptoInfoTradePreview createTradePreview(BigDecimal circulatingSupplyAugend)
			throws InvalidCryptoInfoException
	{
		return cryptoInfo.createTradePreview(circulatingSupplyAugend);
	}
	
	public ItemStackTradePreview createTradePreview
	(
		BigDecimal holderRemainingCapacity,
		int circulatingSupplyAugend,
		BigDecimal rarityMultiplier
	)
	throws InvalidCryptoInfoException
	{
		return createTradePreview
		(
			holderRemainingCapacity,
			BigDecimal.valueOf(circulatingSupplyAugend),
			rarityMultiplier
		);
	}
	
	public ItemStackTradePreview createTradePreview
	(
		BigDecimal holderRemainingCapacity,
		BigDecimal actualAmount,
		BigDecimal rarityMultiplier
	)
	throws InvalidCryptoInfoException
	{
		BigDecimal minAmount = getMinAmount(holderRemainingCapacity, actualAmount, rarityMultiplier);
		BigDecimal multiplier = minAmount.multiply(rarityMultiplier, MATH_CONTEXT);
		
		return new ItemStackTradePreview
		(
			createTradePreview(multiplier),
			minAmount
		);
	}
	
	private BigDecimal getMinAmount
	(
		BigDecimal holderRemainingCapacity,
		BigDecimal actualAmount,
		BigDecimal rarityMultiplier
	)
	throws InvalidCryptoInfoException
	{
		try
		{
			final BigDecimal maxAmount = cryptoInfo
					.previewCirculatingSupplyDiff(holderRemainingCapacity)
					.divideToIntegralValue(rarityMultiplier, MATH_CONTEXT);
			
			return maxAmount.compareTo(actualAmount) < 0
					? maxAmount
					: actualAmount;
		}
		catch(NegativeBalanceException e)
		{
			return actualAmount;
		}
	}
}
