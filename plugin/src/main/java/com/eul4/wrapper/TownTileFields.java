package com.eul4.wrapper;

import com.eul4.common.hologram.Hologram;
import lombok.Builder;

@Builder
public class TownTileFields
{
	public final boolean isInTownBorder;
	public final boolean bought;
	public final Hologram hologram;
	public final int depth;
}
