package com.eul4.model.craft.town;

import com.eul4.common.wrapper.BlockSerializable;
import com.eul4.model.town.Town;
import com.eul4.model.town.TownBlock;
import com.eul4.model.town.TownTile;
import com.eul4.model.town.structure.Structure;
import lombok.RequiredArgsConstructor;
import org.bukkit.block.Block;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.Serial;
import java.util.Optional;

@RequiredArgsConstructor
public class CraftTownBlock implements TownBlock
{
	@Serial
	private static final long serialVersionUID = 1L;
	
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
	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException
	{
		final long version = in.readLong();
		
		if(version == 1L)
		{
			block = ((BlockSerializable) in.readObject()).getBukkitBlock(town.getPlugin().getServer());
			available = in.readBoolean();
		}
		else
		{
			throw new RuntimeException("CraftTownBlock serial version not found: " + version);
		}
	}
	
	@Override
	public void writeExternal(ObjectOutput out) throws IOException
	{
		out.writeLong(serialVersionUID);
		
		out.writeObject(new BlockSerializable(block));
		out.writeBoolean(available);
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
}