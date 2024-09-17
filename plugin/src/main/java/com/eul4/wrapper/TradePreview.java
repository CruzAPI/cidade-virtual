package com.eul4.wrapper;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;

@RequiredArgsConstructor
@Getter
public class TradePreview
{
	private final CryptoInfo cryptoInfo;
	private final BigDecimal preview;
}