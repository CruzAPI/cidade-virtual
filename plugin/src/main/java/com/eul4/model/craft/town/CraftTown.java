package com.eul4.model.craft.town;

import com.eul4.Main;
import com.eul4.model.town.Town;
import com.eul4.model.town.TownBlock;
import com.eul4.model.town.TownTile;
import lombok.RequiredArgsConstructor;
import net.minecraft.server.level.ServerLevel;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.craftbukkit.v1_20_R3.CraftWorld;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

import java.awt.*;
import java.util.List;
import java.util.*;

import static org.bukkit.block.BlockFace.*;

@RequiredArgsConstructor
public class CraftTown implements Town
{
	public static final Set<Entity> HOLOGRAMS = new HashSet<>();
	
	private final Main plugin;
	private final OfflinePlayer owner;
	private final Location location;
	
	private final Map<Block, TownBlock> townBlocks;
	private final Map<Block, TownTile> townTiles;
	private final Map<ArmorStand, TownTile> tileHolograms = new HashMap<>();
	
	public CraftTown(OfflinePlayer owner, Location location, Main plugin)
	{
		this.owner = owner;
		this.location = location;
		this.plugin = plugin;
		
		this.townBlocks = getInitialTownBlocks();
		this.townTiles = getInitialTownTiles();
	}
	
	
	private Map<Block, TownBlock> getInitialTownBlocks()
	{
		if(townBlocks != null)
		{
			return townBlocks;
		}
		
		Map<Block, TownBlock> townBlocks = new HashMap<>();
		
		for(int x = -Town.TOWN_FULL_RADIUS; x <= Town.TOWN_FULL_RADIUS; x++)
		{
			for(int z = -Town.TOWN_FULL_RADIUS; z <= Town.TOWN_FULL_RADIUS; z++)
			{
				Block block = location.getBlock().getRelative(x, 0, z);
				
				townBlocks.put(block, new CraftTownBlock(block, isInInitialAvailableRadius(x, z)));
			}
		}
		
		return townBlocks;
	}
	
	private Map<Block, TownTile> getInitialTownTiles()
	{
		if(townTiles != null)
		{
			return townTiles;
		}
		
		Map<Block, TownTile> townTiles = new HashMap<>();
		
		final int rings = 4;
		final int skipRing = 1;
		final int totalRings = rings + skipRing;
		final int minI = (skipRing * 2 + 1) * (skipRing * 2 + 1);
		final int maxI = (totalRings * 2 + 1) * (totalRings * 2 + 1);
		
		int x = skipRing + 1;
		int z = -skipRing;
		int dx = 1;
		int dz = 0;
		
		for(int i = minI; i < maxI; i++)
		{
			Block block = location.getBlock().getRelative(x * TownTile.DIAMETER, 0, z * TownTile.DIAMETER);
			townTiles.put(block, new CraftTownTile(block, isInTownBorder(i, x, z)));
			
			if(x == z || x < 0 && x == -z || x > 0 && x == 1 - z)
			{
				int temp = dx;
				dx = -dz;
				dz = temp;
			}
			
			x += dx;
			z += dz;
		}
		
		Bukkit.broadcastMessage(townTiles.size() + " tiles size");
		
		return townTiles;
	}
	
	private boolean isInTownBorder(int spiralCount, int x, int z)
	{
		return Math.abs(x) != Math.abs(z) && isInRing(spiralCount, 2);
	}
	
	private boolean isInRing(final int spiralCount, final int ring)
	{
		final int previousRing = ring - 1;
		final int maxI = (ring * 2 + 1) * (ring * 2 + 1);
		final int minI = (previousRing * 2 + 1) * (previousRing * 2 + 1);
		
		return spiralCount >= minI && spiralCount < maxI;
	}
	
	private boolean isInInitialAvailableRadius(int x, int z)
	{
		return Math.abs(x) <= INITIAL_AVAILABLE_RADIUS
				&& Math.abs(z) <= INITIAL_AVAILABLE_RADIUS;
	}
	
	@Override
	public Map<Block, TownBlock> getTownBlocks()
	{
		return townBlocks;
	}
	
	@Override
	public TownBlock getTownBlock(Block block)
	{
		final Block fixedBlockY = block.getWorld().getBlockAt(block.getX(), Y, block.getZ());
		return townBlocks.get(fixedBlockY);
	}
	
	@Override
	public Optional<TownBlock> findTownBlock(Block block)
	{
		return Optional.ofNullable(getTownBlock(block));
	}
	
	@Override
	public Location getLocation()
	{
		return location.clone();
	}
	
	@Override
	public TownTile getTile(Point point)
	{
		return townTiles.get(location.getBlock()
				.getRelative(point.x * TownTile.DIAMETER, 0, point.y * TownTile.DIAMETER));
	}
	
	@Override
	public Set<ArmorStand> getTileHolograms()
	{
		return tileHolograms.keySet();
	}
	
	@RequiredArgsConstructor
	public class CraftTownTile implements TownTile
	{
		private final Block block;
		
		private boolean isInTownBorder;
		private boolean bought;
		private ArmorStand hologram;
		
		private CraftTownTile(Block block, boolean isInTownBorder)
		{
			this.block = block;
			
			setInTownBorder(isInTownBorder);
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
					Block relative = block.getRelative(x, 0, z);
					
					relative.setType(Material.GRASS_BLOCK);
					findTownBlock(relative).ifPresent(townBlock -> townBlock.setAvailable(true));
				}
			}
		}
		
		
		@Override
		public TownTile getRelative(BlockFace direction)
		{
			return townTiles.get(block.getRelative(direction, TownTile.DIAMETER));
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
			
			Location location = block.getLocation().add(0.5D, 1.0D, 0.5D);
			Location veryAwayLocation = location.clone().add(0.0D, 10_000.0D, 0.0D);
			
			ServerLevel serverLevel = ((CraftWorld) location.getWorld()).getHandle();
			net.minecraft.world.entity.decoration.ArmorStand nmsArmorStand = new net.minecraft.world.entity.decoration.ArmorStand(
					net.minecraft.world.entity.EntityType.ARMOR_STAND, serverLevel);
			hologram = (ArmorStand) nmsArmorStand.getBukkitEntity();
			Bukkit.broadcastMessage("nms hologram id: " + hologram.getEntityId());
			boolean freshEntity = serverLevel.addFreshEntity(nmsArmorStand);
			Bukkit.broadcastMessage("fresh entity " + freshEntity);
			
			hologram.setMarker(true);
			hologram.setInvisible(true);
			hologram.setGravity(false);
			hologram.setCustomNameVisible(true);
			hologram.setCustomName("Clique para comprar este terreno!");
			hologram.teleport(location);
			
			tileHolograms.put(hologram, this);
			HOLOGRAMS.add(hologram);
		}
		
		private void removeHologram()
		{
			Optional.ofNullable(hologram).ifPresent(hologram ->
			{
				tileHolograms.remove(hologram);
				hologram.remove();
			});
		}
	}
	
	public class CraftTownBlock implements TownBlock
	{
		private final Block block;
		private boolean available;
		private boolean hasStructure;
		
		public CraftTownBlock(Block block, boolean available)
		{
			this.block = block;
			this.available = available;
		}
		
		@Override
		public boolean canBuild()
		{
			return available && !hasStructure;
		}
		
		@Override
		public boolean hasStructure()
		{
			return hasStructure;
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
			return CraftTown.this;
		}
		
		@Override
		public TownTile getTile()
		{
			Block centerBlock = CraftTown.this.location.getBlock();
			
			int relativeX = block.getX() - centerBlock.getX();
			int relativeZ = block.getZ() - centerBlock.getZ();
			int tileBlockX = (int) Math.round((double) relativeX / TownTile.DIAMETER) * TownTile.DIAMETER;
			int tileBlockZ = (int) Math.round((double) relativeZ / TownTile.DIAMETER) * TownTile.DIAMETER;
			
			Block tileBlock = centerBlock.getRelative(tileBlockX, 0, tileBlockZ);
			
			return townTiles.get(tileBlock);
		}
	}
}
