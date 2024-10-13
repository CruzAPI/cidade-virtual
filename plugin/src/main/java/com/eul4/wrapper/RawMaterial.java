package com.eul4.wrapper;

import com.eul4.exception.InvalidCryptoInfoException;
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
	
	public CryptoInfoTradePreview createTradePreview(BigDecimal multiplier) throws InvalidCryptoInfoException
	{
		return cryptoInfo.createTradePreview(multiplier);
	}
}
