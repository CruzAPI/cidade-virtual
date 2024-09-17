package com.eul4.holder;

import com.eul4.exception.OperationException;

import java.math.BigDecimal;

public interface CrownHolder extends Holder<BigDecimal>
{
	@Override BigDecimal getBalance();
	@Override void setBalance(BigDecimal balance) throws OperationException;
	@Override void add(BigDecimal amount) throws OperationException;
	@Override void subtract(BigDecimal amount) throws OperationException;
}
