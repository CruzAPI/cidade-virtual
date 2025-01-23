package com.eul4.wrapper;

import com.eul4.Main;
import com.eul4.holder.UnlimitedCrownHolder;
import com.google.common.base.Preconditions;
import lombok.Getter;
import lombok.ToString;
import org.jetbrains.annotations.NotNull;

@Getter
@ToString
public class CrownInfo
{
	private final @NotNull UnlimitedCrownHolder serverTreasure;
	private final @NotNull UnlimitedCrownHolder jackpot;
	private final @NotNull UnlimitedCrownHolder townHallVault;
	private final @NotNull UnlimitedCrownHolder eul4Insights;
	
	public CrownInfo(@NotNull Main plugin)
	{
		this
		(
			new UnlimitedCrownHolder(plugin),
			new UnlimitedCrownHolder(plugin),
			new UnlimitedCrownHolder(plugin),
			new UnlimitedCrownHolder(plugin)
		);
	}
	
	public CrownInfo
	(
		@NotNull UnlimitedCrownHolder serverTreasure,
		@NotNull UnlimitedCrownHolder jackpot,
		@NotNull UnlimitedCrownHolder townHallVault,
		@NotNull UnlimitedCrownHolder eul4Insights
	)
	{
		this.serverTreasure = Preconditions.checkNotNull(serverTreasure);
		this.jackpot = Preconditions.checkNotNull(jackpot);
		this.townHallVault = Preconditions.checkNotNull(townHallVault);
		this.eul4Insights = Preconditions.checkNotNull(eul4Insights);
	}
}
