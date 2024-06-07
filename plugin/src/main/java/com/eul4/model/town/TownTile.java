package com.eul4.model.town;

import com.eul4.common.hologram.Hologram;
import com.eul4.wrapper.TownTileFields;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

import java.util.List;
import java.util.Optional;

public interface TownTile
{
	int RADIUS = 4;
	int DIAMETER = RADIUS * 2 + 1;
	
	Block getBlock();
	
	void buy();
	TownTile getRelative(BlockFace direction);
	Optional<TownTile> findRelative(BlockFace direction);
	List<TownTile> getNeighboringTiles();
	boolean isInTownBorder();
	void setInTownBorder(boolean value);
	
	boolean isBought();
	Hologram getHologram();
	
	void loadFields(TownTileFields fields);
	
	Town getTown();
	
	void updateHologram();
}
