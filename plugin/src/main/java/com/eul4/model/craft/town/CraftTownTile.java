package com.eul4.model.craft.town;

import com.eul4.common.hologram.Hologram;
import com.eul4.i18n.PluginMessage;
import com.eul4.model.town.Town;
import com.eul4.model.town.TownBlock;
import com.eul4.model.town.TownTile;
import com.eul4.wrapper.TownTileFields;
import lombok.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.bukkit.block.BlockFace.*;

@RequiredArgsConstructor
@Getter
@Setter
public class CraftTownTile implements TownTile
{
	@NonNull
	private final Town town;
	@NonNull
	private final Block block;
	
	private boolean isInTownBorder;
	private boolean bought;
	
	private Hologram hologram;
	
	private boolean loaded;
	
	public CraftTownTile(Town town, Block block, boolean isInTownBorder)
	{
		this(town, block);
		setInTownBorder(isInTownBorder);
		loaded = true;
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

		if(town.isFrozen())
		{
			town.getPlayer().ifPresent(player -> player.sendMessage("It is not possible to buy the tile.")); //TODO translate message.
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
				
				town.findTownBlock(relative).ifPresent(townBlock -> townBlock.setAvailable(true));
				town.findTownBlock(relative).ifPresent(TownBlock::reset);
			}
		}
	}
	
	
	@Override
	public TownTile getRelative(BlockFace direction)
	{
		return town.getTownTileMap().get(getBlock().getRelative(direction, TownTile.DIAMETER));
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
	public void loadFields(TownTileFields fields)
	{
		if(loaded)
		{
			return;
		}
		
		hologram = fields.hologram;
		bought = fields.bought;
		isInTownBorder = fields.isInTownBorder;
		
		loaded = true;
	}
	
	@Builder
	public static CraftTownTile createTile(@NonNull Town town, @NonNull Block block)
	{
		return new CraftTownTile(town, block);
	}
	
	@Override
	public void updateHologram()
	{
		if(hologram == null)
		{
			return;
		}
		
		if(!town.isUnderAttack() && isInTownBorder)
		{
			hologram.setSize(1);
			hologram.getLine(0).setMessageAndArgs(PluginMessage.CLICK_TO_BUY_THIS_TILE);
		}
		else
		{
			hologram.remove();
		}
	}
}