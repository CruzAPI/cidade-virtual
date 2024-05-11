package com.eul4.model.craft.town;

import com.eul4.model.town.Town;
import com.eul4.model.town.TownBlock;
import com.eul4.model.town.TownTile;
import com.eul4.model.town.structure.Structure;
import lombok.RequiredArgsConstructor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

import java.util.Optional;

@RequiredArgsConstructor
public class CraftTownBlock implements TownBlock
{
	private final Town town;
	private Block block;
	private boolean available;
	
	private transient Structure structure;
	
	public CraftTownBlock(Town town, Block block, boolean available)
	{
		this(town);
		this.block = block;
		this.available = available;
	}
	
	@Override
	public boolean canBuild()
	{
		return available && !hasStructure();
	}
	
	@Override
	public boolean hasStructure()
	{
		return structure != null;
	}
	
	@Override
	public void setAvailable(boolean value)
	{
		this.available = value;
	}
	
	@Override
	public boolean isAvailable()
	{
		return available;
	}
	
	@Override
	public Block getBlock()
	{
		return block;
	}
	
	@Override
	public Town getTown()
	{
		return town;
	}
	
	@Override
	public TownTile getTile()
	{
		Block centerBlock = town.getTownBlock(town.getLocation().getBlock()).getBlock();
		
		int relativeX = getBlock().getX() - centerBlock.getX();
		int relativeZ = getBlock().getZ() - centerBlock.getZ();
		int tileBlockX = (int) Math.round((double) relativeX / TownTile.DIAMETER) * TownTile.DIAMETER;
		int tileBlockZ = (int) Math.round((double) relativeZ / TownTile.DIAMETER) * TownTile.DIAMETER;
		
		Block tileBlock = centerBlock.getRelative(tileBlockX, 0, tileBlockZ);
		
		return town.getTownTiles().get(tileBlock);
	}
	
	@Override
	public Structure getStructure()
	{
		return structure;
	}
	
	@Override
	public void setStructure(Structure structure)
	{
		this.structure = structure;
	}
	
	@Override
	public Optional<Structure> findStructure()
	{
		return Optional.ofNullable(structure);
	}
	
	@Override
	public void reset()
	{
		structure = null;
		
		for(Block block = this.block.getWorld().getBlockAt(this.block.getX(), 0, this.block.getZ());
				block.getY() < block.getWorld().getMaxHeight();
				block = block.getRelative(BlockFace.UP))
		{
			if(block.getY() < Town.BED_ROCK_Y)
			{
				block.setType(Material.AIR);
			}
			else if(block.getY() == Town.BED_ROCK_Y)
			{
				block.setType(Material.BEDROCK);
			}
			else if(block.getY() < Town.GRASS_Y)
			{
				block.setType(Material.DIRT);
			}
			else if(block.getY() == Town.GRASS_Y)
			{
				block.setType(Material.GRASS_BLOCK);
			}
			else
			{
				block.setType(Material.AIR);
			}
		}
	}
	
	@Override
	public boolean hasProtection()
	{
		return !canBuild();
	}
}