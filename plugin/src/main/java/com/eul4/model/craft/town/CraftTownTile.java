package com.eul4.model.craft.town;

import com.eul4.common.hologram.Hologram;
import com.eul4.common.wrapper.BlockSerializable;
import com.eul4.i18n.PluginMessage;
import com.eul4.model.town.Town;
import com.eul4.model.town.TownTile;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.Serial;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.bukkit.block.BlockFace.*;

@RequiredArgsConstructor
public class CraftTownTile implements TownTile
{
	@Serial
	private static final long serialVersionUID = 1L;
	
	private final Town town;
	private Block block;
	private boolean isInTownBorder;
	private boolean bought;
	
	private Hologram hologram;
	
	public CraftTownTile(Town town, Block block, boolean isInTownBorder)
	{
		this.town = town;
		this.block = block;
		
		setInTownBorder(isInTownBorder);
	}
	
	@Override
	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException
	{
		final long version = in.readLong();
		
		if(version == 1L)
		{
			block = ((BlockSerializable) in.readObject()).getBukkitBlock(town.getPlugin().getServer());
			isInTownBorder = in.readBoolean();
			bought = in.readBoolean();
			hologram = (Hologram) in.readObject();
		}
		else
		{
			throw new RuntimeException("CraftTown serial version not found: " + version);
		}
	}
	
	@Override
	public void writeExternal(ObjectOutput out) throws IOException
	{
		out.writeLong(serialVersionUID);
		
		out.writeObject(new BlockSerializable(block));
		out.writeBoolean(isInTownBorder);
		out.writeBoolean(bought);
		out.writeObject(hologram);
	}
	
	@Override
	public Block getBlock()
	{
		return block;
	}
	
	@Override
	public void buy()
	{
		if(bought)
		{
			Bukkit.broadcastMessage("Tile already bought.");
			return;
		}
		
		if(!isInTownBorder)
		{
			Bukkit.broadcastMessage("Cannot buy this tile yet.");
			return;
		}
		bought = true;
		
		getNeighboringTiles().forEach(neighborTile -> neighborTile.setInTownBorder(true));
		makeBlocksAvailable();
		
		Bukkit.broadcastMessage("tile bought!");
		removeHologram();
	}
	
	private void makeBlocksAvailable()
	{
		for(int x = -RADIUS; x <= RADIUS; x++)
		{
			for(int z = -RADIUS; z <= RADIUS; z++)
			{
				Block relative = getBlock().getRelative(x, 0, z);
				
				relative.setType(Material.GRASS_BLOCK);
				town.findTownBlock(relative).ifPresent(townBlock -> townBlock.setAvailable(true));
			}
		}
	}
	
	
	@Override
	public TownTile getRelative(BlockFace direction)
	{
		return town.getTownTiles().get(getBlock().getRelative(direction, TownTile.DIAMETER));
	}
	
	@Override
	public Optional<TownTile> findRelative(BlockFace direction)
	{
		return Optional.ofNullable(getRelative(direction));
	}
	
	@Override
	public List<TownTile> getNeighboringTiles()
	{
		List<TownTile> neighboringTiles = new ArrayList<>();
		
		for(BlockFace direction : new BlockFace[] { SOUTH, EAST, NORTH, WEST })
		{
			findRelative(direction).ifPresent(neighboringTiles::add);
		}
		
		return neighboringTiles;
	}
	
	@Override
	public boolean isInTownBorder()
	{
		return isInTownBorder;
	}
	
	@Override
	public void setInTownBorder(boolean value)
	{
		if(!isInTownBorder && value)
		{
			spawnHologram();
		}
		
		isInTownBorder = value;
	}
	
	private void spawnHologram()
	{
		if(hologram != null)
		{
			return;
		}
		
		Location location = getBlock().getLocation().add(0.5D, 1.0D, 0.5D);
		
		hologram = new Hologram(town.getPlugin(), location);
		hologram.newLine(PluginMessage.CLICK_TO_BUY_THIS_TILE);
	}
	
	private void removeHologram()
	{
		Optional.ofNullable(hologram).ifPresent(Hologram::remove);
		hologram = null;
	}
	
	@Override
	public void load()
	{
		if(hologram != null)
		{
			hologram.load(town.getPlugin());
		}
	}
}