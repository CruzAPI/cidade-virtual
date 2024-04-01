package com.eul4.model.craft.town;

import com.eul4.Main;
import com.eul4.common.wrapper.LocationSerializable;
import com.eul4.exception.CannotConstructException;
import com.eul4.model.craft.town.structure.CraftDislikeFarm;
import com.eul4.model.craft.town.structure.CraftLikeFarm;
import com.eul4.model.craft.town.structure.CraftTownHall;
import com.eul4.model.town.Town;
import com.eul4.model.town.TownBlock;
import com.eul4.model.town.TownTile;
import com.eul4.model.town.structure.HologramStructure;
import com.eul4.model.town.structure.Structure;
import com.eul4.service.TownSerializer;
import com.sk89q.worldedit.session.ClipboardHolder;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.awt.*;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.Serial;
import java.util.*;
import java.util.function.Consumer;

@Getter
public class CraftTown implements Town
{
	@Serial
	private static final long serialVersionUID = 1L;
	
	private UUID ownerUUID;
	private Location location;
	
	private final Main plugin;
	
	private Map<Block, TownBlock> townBlocks;
	
	private Map<Block, TownTile> townTiles;
	
	private Map<UUID, Structure> structures;
	private transient Structure movingStructure;
	private transient ClipboardHolder movingStructureClipboardHolder;
	
	private final transient Consumer<Player> removeMovingStructureItem;
	
	private int likes;
	private int likeLimit;
	private int dislikes;
	private int dislikeLimit;
	
	public CraftTown(Main plugin)
	{
		this.plugin = plugin;
		this.removeMovingStructureItem = player ->
				player.getInventory().removeItemAnySlot(movingStructure.getItem());
	}
	
	public CraftTown(OfflinePlayer owner, Location location, Main plugin) throws CannotConstructException, IOException
	{
		this(plugin);
		
		this.ownerUUID = owner.getUniqueId();
		this.location = location;
		
		this.townBlocks = getInitialTownBlocks();
		this.townTiles = getInitialTownTiles();
		this.structures = getInitialStructures();
		
		this.likeLimit = 100;
		this.dislikeLimit = 100;
	}
	
	@Override
	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException
	{
		final long version = in.readLong();
		
		TownSerializer townSerializer = plugin.getTownSerializer();
		
		if(version == 1L)
		{
			ownerUUID = (UUID) in.readObject();
			location = ((LocationSerializable) in.readObject()).getBukkitLocation(plugin.getServer());
			townBlocks = townSerializer.readTownBlocks(this, in);
			townTiles = townSerializer.readTownTiles(this, in);
			structures = townSerializer.readStructures(this, in);
			movingStructure = townSerializer.readStructureReference(this, in);
			likes = in.readInt();
			likeLimit = in.readInt();
			dislikes = in.readInt();
			dislikeLimit = in.readInt();
		}
		else
		{
			throw new RuntimeException("CraftTown serial version not found: " + version);
		}
		
		load();
	}
	
	@Override
	public void writeExternal(ObjectOutput out) throws IOException
	{
		final TownSerializer townSerializer = plugin.getTownSerializer();
		
		out.writeLong(serialVersionUID);
		
		out.writeObject(ownerUUID);
		out.writeObject(new LocationSerializable(location));
		townSerializer.writeTownBlocks(townBlocks, out);
		townSerializer.writeTownTiles(this, out);
		townSerializer.writeStructures(structures, out);
		townSerializer.writeStructureReference(movingStructure, out);
		out.writeInt(likes);
		out.writeInt(likeLimit);
		out.writeInt(dislikes);
		out.writeInt(dislikeLimit);
		out.flush();
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
				Block block = getLocation().getBlock().getRelative(x, 0, z);
				
				townBlocks.put(block, new CraftTownBlock(this, block, isInInitialAvailableRadius(x, z)));
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
			Block block = getLocation().getBlock().getRelative(x * TownTile.DIAMETER, 0, z * TownTile.DIAMETER);
			townTiles.put(block, new CraftTownTile(this, block, isInTownBorder(i, x, z)));
			
			if(x == z || x < 0 && x == -z || x > 0 && x == 1 - z)
			{
				int temp = dx;
				dx = -dz;
				dz = temp;
			}
			
			x += dx;
			z += dz;
		}
		
		return townTiles;
	}
	
	public Map<UUID, Structure> getInitialStructures() throws CannotConstructException, IOException
	{
		Map<UUID, Structure> map = new HashMap<>();
		
		TownBlock centerTownBlock = getTownBlock(location.getBlock());
		Block centerBlock = centerTownBlock.getBlock();
		
		TownBlock likeFarmTownBlock = getTownBlock(centerBlock.getRelative(-10, 0, -3));
		TownBlock dislikeFarmTownBlock = getTownBlock(centerBlock.getRelative(-10, 0, 3));
		
		CraftTownHall craftTownHall = new CraftTownHall(this, centerTownBlock);
		CraftLikeFarm craftLikeFarm = new CraftLikeFarm(this, likeFarmTownBlock);
		CraftDislikeFarm craftDislikeFarm = new CraftDislikeFarm(this, dislikeFarmTownBlock);
		
		map.put(craftTownHall.getUUID(), craftTownHall);
		map.put(craftLikeFarm.getUUID(), craftLikeFarm);
		map.put(craftDislikeFarm.getUUID(), craftDislikeFarm);
		
		return map;
	}
	
	@Override
	public OfflinePlayer getOwner()
	{
		return plugin.getServer().getOfflinePlayer(ownerUUID);
	}
	
	@Override
	public Optional<Player> getPlayer()
	{
		return Optional.ofNullable(getOwner().getPlayer());
	}
	
	@Override
	public Structure getMovingStructure()
	{
		return movingStructure;
	}
	
	@Override
	public void setMovingStructure(Structure structure)
	{
		this.movingStructure = structure;
	}
	
	@Override
	public void startMovingStructure(Structure structure) throws IOException, CannotConstructException
	{
		if(isMovingStructure())
		{
			cancelMovingStructure();
		}
		
		movingStructureClipboardHolder = structure.loadSchematic();
		structure.demolishStructureConstruction(movingStructureClipboardHolder);
		movingStructure = structure;
	}
	
	@Override
	public void cancelMovingStructure() throws CannotConstructException
	{
		if(!isMovingStructure())
		{
			return;
		}
		
		Structure movingStructure = this.movingStructure;
		ClipboardHolder movingStructureClipboardHolder = this.movingStructureClipboardHolder;
		
		this.movingStructure = null;
		this.movingStructureClipboardHolder = null;
		
		movingStructure.construct(movingStructureClipboardHolder);
		
		getPlayer().ifPresent(removeMovingStructureItem);
	}
	
	@Override
	public void finishMovingStructure(TownBlock centerTownBlock, int rotation) throws CannotConstructException
	{
		if(!isMovingStructure())
		{
			return;
		}
		
		movingStructure.construct(movingStructureClipboardHolder, centerTownBlock, rotation);
		
		if(movingStructure instanceof HologramStructure hologramStructure)
		{
			hologramStructure.teleportHologram();
		}
		
		getPlayer().ifPresent(removeMovingStructureItem);
		
		this.movingStructure = null;
		this.movingStructureClipboardHolder = null;
	}
	
	@Override
	public boolean isMovingStructure()
	{
		return movingStructure != null;
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
		return location;
	}
	
	@Override
	public TownTile getTile(Point point)
	{
		return townTiles.get(getLocation().getBlock()
				.getRelative(point.x * TownTile.DIAMETER, 0, point.y * TownTile.DIAMETER));
	}
	
	@Override
	public void load()
	{
		structures.values().forEach(Structure::load);
		townTiles.values().forEach(TownTile::load);
	}
	
	@Override
	public void setCappedLikes(int likes)
	{
		this.likes = Math.min(likeLimit, likes);
	}
	
	@Override
	public void setCappedDislikes(int dislikes)
	{
		this.dislikes = Math.min(dislikeLimit, dislikes);
	}
}
