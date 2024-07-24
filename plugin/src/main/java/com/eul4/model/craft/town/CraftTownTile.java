package com.eul4.model.craft.town;

import com.eul4.Price;
import com.eul4.common.hologram.Hologram;
import com.eul4.enums.Currency;
import com.eul4.i18n.PluginMessage;
import com.eul4.model.town.Town;
import com.eul4.model.town.TownBlock;
import com.eul4.model.town.TownTile;
import com.eul4.wrapper.TownTileFields;
import lombok.*;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.eul4.i18n.PluginMessage.BOLD_DECORATED_VALUE_CURRENCY;
import static com.eul4.i18n.PluginMessage.CLICK_TO_BUY_THIS_TILE;
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
	private int depth;
	
	public CraftTownTile(Town town, Block block, boolean isInTownBorder, int depth)
	{
		this(town, block);
		this.depth = depth;
		setInTownBorder(isInTownBorder);
		loaded = true;
	}
	
	@Override
	public Block getBlock()
	{
		return block;
	}
	
	@Override
	public boolean buy()
	{
		if(bought || !isInTownBorder || town.isFrozen())
		{
			return false;
		}

		bought = true;
		getNeighboringTiles().forEach(neighborTile -> neighborTile.setInTownBorder(true));
		makeBlocksAvailable();
		
		town.findPluginPlayer().ifPresent(pluginPlayer -> pluginPlayer.sendMessage(PluginMessage.TILE_BOUGHT));
		
		removeHologram();
		town.onTileBought(this);
		return true;
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
		boolean spawnHologram = !isInTownBorder && value;
		isInTownBorder = value;
		
		if(spawnHologram)
		{
			spawnHologram();
		}
	}
	
	private void spawnHologram()
	{
		if(hologram != null)
		{
			return;
		}
		
		Location location = getBlock().getLocation().add(0.5D, 1.0D, 0.5D);
		
		hologram = new Hologram(town.getPlugin(), location);
		updateHologram();
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
		depth = fields.depth;
		
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
			Price price = calculatePrice();
			
			hologram.setSize(3);
			hologram.getLine(2).setMessageAndArgs(CLICK_TO_BUY_THIS_TILE);
			hologram.getLine(1).setMessageAndArgs(BOLD_DECORATED_VALUE_CURRENCY, Currency.LIKE, price.getLikes());
			hologram.getLine(0).setMessageAndArgs(BOLD_DECORATED_VALUE_CURRENCY, Currency.DISLIKE, price.getDislikes());
		}
		else
		{
			hologram.remove();
		}
	}
	
	@Override
	public Price calculatePrice()
	{
		final int tilesBought = town.getBoughtTileMapByDepth().getTilesBoughtInDepth(depth);
		
		final int basePrice = getTileBasePriceByDepth(depth);
		final int incrementPrice = getIncrementTilePriceChartByDepth(depth);
		
		return new Price(basePrice + incrementPrice * tilesBought);
	}
	
	private static int getIncrementTilePriceChartByDepth(int depth)
	{
		return switch(depth)
		{
			case 1 -> 500;
			case 2 -> 2_000;
			case 3 -> 10_000;
			default -> 30_000;
		};
	}
	
	private static int getTileBasePriceByDepth(int depth)
	{
		return switch(depth)
		{
			case 1 -> 200;
			case 2 -> 10_000;
			case 3 -> 150_000;
			default -> 900_000;
		};
	}
}