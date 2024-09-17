package com.eul4.wrapper;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class CrownInfo
{
	private BigDecimal serverTreasure = BigDecimal.ZERO;
	private BigDecimal jackpot = BigDecimal.ZERO;
	private BigDecimal townHallVault = BigDecimal.ZERO;
	private BigDecimal eul4Insights = BigDecimal.ZERO;
}
