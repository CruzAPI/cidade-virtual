package com.eul4.model.town;

import com.eul4.Main;
import com.eul4.Price;
import com.eul4.StructureType;
import com.eul4.exception.*;
import com.eul4.model.craft.town.CraftTown;
import com.eul4.model.town.structure.Structure;
import com.eul4.model.town.structure.TownHall;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.awt.*;
import java.io.Externalizable;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public interface Town extends Externalizable
{
	int TOWN_RADIUS = 49;
	int TOWN_FULL_RADIUS = 55;
	int TOWN_FULL_DIAMATER = 55 * 2 + 1;
	int INITIAL_AVAILABLE_RADIUS = 13;
	int Y = 49;
	int GRASS_Y = Y;
	int BED_ROCK_Y = 41;
	
	static TownBlock getStaticTownBlock(Block block)
	{
		final Block fixedBlockY = block.getWorld().getBlockAt(block.getX(), Y, block.getZ());
		return CraftTown.TOWN_BLOCKS.get(fixedBlockY);
	}
	
	static Optional<TownBlock> findStaticTownBlock(Block block)
	{
		return Optional.ofNullable(getStaticTownBlock(block));
	}
	
	Map<Block, TownBlock> getTownBlocks();
	TownBlock getTownBlock(Block block);
	Optional<TownBlock> findTownBlock(Block block);
	Location getLocation();
	TownTile getTile(Point point);
	Main getPlugin();
	
	void setMovingStructure(Structure structure);
	
	void startMovingStructure(Structure structure) throws IOException, CannotConstructException;
	
	void cancelMovingStructure() throws CannotConstructException;
	
	void finishMovingStructure(TownBlock centerTownBlock, int rotation) throws CannotConstructException;
	
	boolean isMovingStructure();
	Structure getMovingStructure();
	OfflinePlayer getOwner();
	Optional<Player> getPlayer();
	
	Map<Block, TownTile> getTownTiles();
	
	void load();
	
	Map<UUID, Structure> getStructures();
	
	int getLikes();
	int getLikeCapacity();
	int getDislikes();
	int getDislikeCapacity();
	
	void setCappedLikes(int likes);
	
	void setCappedDislikes(int dislikes);
	
	void addStructure(Structure structure);
	
	void subtract(Price price);
	
	void checkIfAffordable(Price price) throws InsufficientBalanceException;
	
	Price buyNewStructure(StructureType<?, ?> structureType, TownBlock townBlock)
			throws StructureLimitException, StructureNotForSaleException, CannotConstructException, IOException, InsufficientBalanceException;
	
	TownHall getTownHall();
	void reloadAllStructureAttributes();
	
	int countStructures(StructureType<?, ?> structureType);
	int getStructureLimit(StructureType<?, ?> structureType);
	
	void reloadAttributes();
	
	double getHardness();
	double getHardnessLimit();
	void decreaseHardness(double hardness);
	void increaseHardness(double hardness) throws TownHardnessLimitException;
	
	void setHardness(double hardness) throws TownHardnessLimitException;
	boolean isOwner(Player player);
}
