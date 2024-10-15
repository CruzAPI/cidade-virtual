package com.eul4.wrapper;

import com.eul4.exception.InvalidCryptoInfoException;
import com.eul4.exception.NegativeBalanceException;
import com.google.common.base.Preconditions;
import lombok.Getter;
import org.bukkit.Material;

import java.math.BigDecimal;

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
		int circulatingSupplyAugend
	)
	throws InvalidCryptoInfoException
	{
		return createTradePreview(holderRemainingCapacity, BigDecimal.valueOf(circulatingSupplyAugend));
	}
	
	public ItemStackTradePreview createTradePreview
	(
		BigDecimal holderRemainingCapacity,
		BigDecimal circulatingSupplyAugend
	)
	throws InvalidCryptoInfoException
	{
		try
		{
			BigDecimal maxCirculatingSupplyAugend = cryptoInfo.previewCirculatingSupplyDiff(holderRemainingCapacity);
			circulatingSupplyAugend = maxCirculatingSupplyAugend.compareTo(circulatingSupplyAugend) < 0
					? maxCirculatingSupplyAugend
					: circulatingSupplyAugend;
		}
		catch(NegativeBalanceException ignored)
		{
		
		}
		
		return new ItemStackTradePreview(createTradePreview(circulatingSupplyAugend), circulatingSupplyAugend);
	}
}
