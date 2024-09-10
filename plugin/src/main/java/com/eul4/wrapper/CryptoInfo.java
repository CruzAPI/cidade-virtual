package com.eul4.wrapper;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

@AllArgsConstructor
@Getter
public class CryptoInfo
{
	public static final MathContext MATH_CONTEXT = new MathContext(8, RoundingMode.HALF_EVEN);
	
	private BigDecimal marketCap;
	private BigDecimal circulatingSupply;
	
	public CryptoInfo(double marketCap, double circulatingSupply)
	{
		this.marketCap = BigDecimal.valueOf(marketCap);
		this.circulatingSupply = BigDecimal.valueOf(circulatingSupply);
	}
	
	public BigDecimal calculatePrice()
	{
		return marketCap.divide(circulatingSupply, MATH_CONTEXT);
	}
	
	public BigDecimal trade(int amount)
	{
		return trade(new BigDecimal(amount));
	}
	
	public BigDecimal trade(BigDecimal amount)
	{
		BigDecimal currentPrice = calculatePrice();
		BigDecimal newPrice = marketCap
				.subtract(currentPrice.multiply(amount))
				.divide(circulatingSupply.add(amount), MATH_CONTEXT);
		BigDecimal avaragePrice = currentPrice
				.add(newPrice)
				.divide(BigDecimal.valueOf(2.0D), MATH_CONTEXT);
		BigDecimal trade = avaragePrice.multiply(amount, MATH_CONTEXT);
		marketCap = marketCap.subtract(trade);
		circulatingSupply = circulatingSupply.add(amount);
		
		return trade;
	}
}
