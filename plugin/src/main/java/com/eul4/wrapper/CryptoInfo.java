package com.eul4.wrapper;

import com.eul4.exception.InvalidCryptoInfoException;
import com.eul4.exception.NegativeBalanceException;
import com.eul4.exception.OperationException;
import com.eul4.holder.CrownHolder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class CryptoInfo implements CrownHolder
{
	public static final MathContext MATH_CONTEXT = new MathContext(8, RoundingMode.HALF_EVEN);
	
	@Setter
	private BigDecimal marketCap = BigDecimal.ZERO;
	
	@Setter
	private BigDecimal circulatingSupply = BigDecimal.ZERO;
	
	public CryptoInfo(double marketCap, double circulatingSupply)
	{
		this.marketCap = BigDecimal.valueOf(marketCap);
		this.circulatingSupply = BigDecimal.valueOf(circulatingSupply);
	}
	
	public BigDecimal calculatePrice() throws InvalidCryptoInfoException
	{
		validate();
		return marketCap.divide(circulatingSupply, MATH_CONTEXT);
	}
	
	public CryptoInfoTradePreview createTradePreview(BigDecimal amount) throws InvalidCryptoInfoException
	{
		return new CryptoInfoTradePreview(this, previewTrade(amount));
	}
	
	public BigDecimal previewTrade(BigDecimal amount) throws InvalidCryptoInfoException
	{
		validate();
		
		BigDecimal circulatingSupply = this.circulatingSupply.add(amount);
		
		if(circulatingSupply.compareTo(BigDecimal.ZERO) <= 0)
		{
			throw new InvalidCryptoInfoException();
		}
		
		BigDecimal ratio = circulatingSupply.divide(this.circulatingSupply, MATH_CONTEXT);
		BigDecimal marketCap = this.marketCap.divide(ratio, MATH_CONTEXT);
		
		return this.marketCap.subtract(marketCap);
	}
	
	public BigDecimal trade(BigDecimal amount) throws InvalidCryptoInfoException
	{
		validate();
		
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
	
	public boolean isValid()
	{
		return marketCap.compareTo(BigDecimal.ZERO) > 0 && circulatingSupply.compareTo(BigDecimal.ZERO) > 0;
	}
	
	private void validate() throws InvalidCryptoInfoException
	{
		if(!isValid())
		{
			throw new InvalidCryptoInfoException();
		}
	}
	
	@Override
	public BigDecimal getBalance()
	{
		return marketCap;
	}
	
	@Override
	public void setBalance(BigDecimal balance) throws OperationException
	{
		updateMarketCap(balance);
	}
	
	@Override
	public void add(BigDecimal amount) throws OperationException
	{
		updateMarketCap(marketCap.add(amount));
	}
	
	@Override
	public void subtract(BigDecimal amount) throws OperationException
	{
		updateMarketCap(marketCap.subtract(amount));
	}
	
	@Override
	public boolean isEmpty()
	{
		return marketCap.compareTo(BigDecimal.ZERO) <= 0;
	}
	
	private void updateMarketCap(BigDecimal newMarketCap) throws OperationException
	{
		if(newMarketCap.compareTo(BigDecimal.ZERO) < 0)
		{
			throw new NegativeBalanceException();
		}
		
		BigDecimal ratio = newMarketCap.divide(marketCap, MATH_CONTEXT);
		marketCap = newMarketCap;
		circulatingSupply = circulatingSupply.divide(ratio, MATH_CONTEXT);
	}
}
