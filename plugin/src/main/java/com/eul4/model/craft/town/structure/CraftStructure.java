package com.eul4.model.craft.town.structure;

import com.eul4.common.wrapper.BlockSerializable;
import com.eul4.exception.CannotConstructException;
import com.eul4.model.town.Town;
import com.eul4.model.town.TownBlock;
import com.eul4.model.town.structure.HologramStructure;
import com.eul4.model.town.structure.Structure;
import com.eul4.service.TownSerializer;
import com.fastasyncworldedit.core.FaweAPI;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormats;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardReader;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.math.Vector3;
import com.sk89q.worldedit.math.transform.AffineTransform;
import com.sk89q.worldedit.session.ClipboardHolder;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

public abstract class CraftStructure implements Structure
{
	@Serial
	private static final long serialVersionUID = 1L;
	
	@Getter
	protected final Town town;
	
	private UUID uuid;
	
	private TownBlock centerTownBlock;
	
	protected int level = 1;
	private int rotation;
	
	private Set<TownBlock> townBlocks = new HashSet<>();
	
	public CraftStructure(Town town)
	{
		this.town = town;
	}
	
	public CraftStructure(Town town, TownBlock centerTownBlock) throws CannotConstructException, IOException
	{
		this(town);
		
		this.uuid = UUID.randomUUID();
		this.centerTownBlock = centerTownBlock;
		
		construct(loadSchematic(), centerTownBlock, 0);
	}
	
	@Override
	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException
	{
		final TownSerializer townSerializer = town.getPlugin().getTownSerializer();
		
		final long version = in.readLong();
		
		if(version == 1L)
		{
			centerTownBlock = Objects.requireNonNull(town.getTownBlock(((BlockSerializable) in.readObject())
					.getBukkitBlock(town.getPlugin().getServer())));
			level = in.readInt();
			rotation = in.readInt();
			townBlocks = townSerializer.readStructureTownBlocks(this, in);
		}
		else
		{
			throw new RuntimeException("CraftStructure serial version not found: " + version);
		}
	}
	
	@Override
	public void writeExternal(ObjectOutput out) throws IOException
	{
		final TownSerializer townSerializer = town.getPlugin().getTownSerializer();
		
		out.writeLong(serialVersionUID);
		
		out.writeObject(new BlockSerializable(centerTownBlock.getBlock()));
		out.writeInt(level);
		out.writeInt(rotation);
		townSerializer.writeStructureTownBlocks(townBlocks, out);
	}
	
	@Override
	public void startMove() throws IOException, CannotConstructException
	{
		town.startMovingStructure(this);
	}
	
	@Override
	public void finishMove(TownBlock centerTownBlock, int rotation) throws CannotConstructException
	{
		town.finishMovingStructure(centerTownBlock, rotation);
	}
	
	@Override
	public void finishMove(TownBlock centerTownBlock) throws CannotConstructException
	{
		finishMove(centerTownBlock, rotation);
	}
	
	@Override
	public void cancelMove() throws CannotConstructException
	{
		town.cancelMovingStructure();
	}
	
	@Override
	public void demolishStructureConstruction(ClipboardHolder clipboardHolder)
	{
		clipboardHolder.setTransform(new AffineTransform().rotateY(rotation));
		Clipboard clipboard = clipboardHolder.getClipboard();
		
		final Vector3 rotatedMin = clipboardHolder.getTransform().apply(clipboard.getMinimumPoint().toVector3());
		final Vector3 rotatedMax = clipboardHolder.getTransform().apply(clipboard.getMaximumPoint().toVector3());
		final Vector3 origin = clipboardHolder.getTransform().apply(clipboard.getOrigin().toVector3());
		
		final int minX = Math.min(rotatedMin.getBlockX(), rotatedMax.getBlockX());
		final int minZ = Math.min(rotatedMin.getBlockZ(), rotatedMax.getBlockZ());
		final int maxX = Math.max(rotatedMin.getBlockX(), rotatedMax.getBlockX());
		final int maxZ = Math.max(rotatedMin.getBlockZ(), rotatedMax.getBlockZ());
		final int originX = origin.getBlockX();
		final int originZ = origin.getBlockZ();
		final int relativeMinX = minX - originX;
		final int relativeMaxX = maxX - originX;
		final int relativeMinZ = minZ - originZ;
		final int relativeMaxZ = maxZ - originZ;
		
		final Block centerBlock = centerTownBlock.getBlock();
		
		for(int x = relativeMinX; x <= relativeMaxX; x++)
		{
			for(int z = relativeMinZ; z <= relativeMaxZ; z++)
			{
				for(int y = 0; y < 20; y++)
				{
					centerBlock.getRelative(x, y, z).setType(y == 0 ? Material.RED_CONCRETE : Material.AIR);
				}
			}
		}
	}
	
	@Override
	public void construct(ClipboardHolder clipboardHolder) throws CannotConstructException
	{
		construct(clipboardHolder, centerTownBlock, rotation);
	}
	
	@Override
	public void construct(ClipboardHolder clipboardHolder, TownBlock centerTownBlock, int rotation)
			throws CannotConstructException
	{
		Block center = centerTownBlock.getBlock();
		BlockVector3 to = BlockVector3.at(center.getX(), center.getY() + 1, center.getZ());
		
		clipboardHolder.setTransform(new AffineTransform().rotateY(rotation));
		Clipboard clipboard = clipboardHolder.getClipboard();
		
		final Vector3 rotatedMin = clipboardHolder.getTransform().apply(clipboard.getMinimumPoint().toVector3());
		final Vector3 rotatedMax = clipboardHolder.getTransform().apply(clipboard.getMaximumPoint().toVector3());
		final Vector3 origin = clipboardHolder.getTransform().apply(clipboard.getOrigin().toVector3());
		
		final int minX = Math.min(rotatedMin.getBlockX(), rotatedMax.getBlockX());
		final int minZ = Math.min(rotatedMin.getBlockZ(), rotatedMax.getBlockZ());
		final int maxX = Math.max(rotatedMin.getBlockX(), rotatedMax.getBlockX());
		final int maxZ = Math.max(rotatedMin.getBlockZ(), rotatedMax.getBlockZ());
		final int originX = origin.getBlockX();
		final int originZ = origin.getBlockZ();
		final int relativeMinX = minX - originX;
		final int relativeMaxX = maxX - originX;
		final int relativeMinZ = minZ - originZ;
		final int relativeMaxZ = maxZ - originZ;
		
		Set<TownBlock> townBlocks = new HashSet<>();
		
		for(int x = relativeMinX; x <= relativeMaxX; x++)
		{
			for(int z = relativeMinZ; z <= relativeMaxZ; z++)
			{
				TownBlock townBlock = town.getTownBlock(center.getRelative(x, 0, z));
				
				townBlocks.add(townBlock);
				
				if(townBlock == null || !townBlock.isAvailable()
						|| (townBlock.hasStructure() && townBlock.getStructure() != this))
				{
					throw new CannotConstructException();
				}
			}
		}
		
		this.townBlocks.removeAll(townBlocks);
		this.townBlocks.forEach(townBlock ->
		{
			townBlock.setStructure(null);
			townBlock.getBlock().setType(Material.GRASS_BLOCK);
		});
		
		this.townBlocks = townBlocks;
		this.townBlocks.forEach(townBlock -> townBlock.setStructure(this));
		
		var world = FaweAPI.getWorld(centerTownBlock.getBlock().getWorld().getName());
		
		try(EditSession editSession = WorldEdit.getInstance().getEditSessionFactory().getEditSession(world, -1))
		{
			Operations.complete(clipboardHolder
					.createPaste(editSession)
					.to(to)
					.ignoreAirBlocks(false)
					.build());
		}
		
		this.centerTownBlock = centerTownBlock;
		this.rotation = rotation;
	}
	
	@Override
	public ClipboardHolder loadSchematic() throws IOException
	{
		File schematicFile = getSchematicFile();
		
		try(FileInputStream fileInputStream = new FileInputStream(schematicFile))
		{
			ClipboardFormat format = ClipboardFormats.findByFile(schematicFile);
			ClipboardReader reader = format.getReader(fileInputStream);
			return new ClipboardHolder(reader.read());
		}
	}
	
	@Override
	public int getLevel()
	{
		return level;
	}
	
	@Override
	public File getSchematicFile()
	{
		return new File(town.getPlugin().getSchematicsFolder(), getName() + "_" + level + ".schem");
	}
	
	@Override
	public String getName()
	{
		return getStructureType().getName();
	}
	
	@Override
	public ItemStack getItem()
	{
		ItemStack item = new ItemStack(Material.STONE);
		ItemMeta meta = item.getItemMeta();
		meta.displayName(Component.text("ttext"));
		meta.addEnchant(Enchantment.DURABILITY, 1, true);
		meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		item.setItemMeta(meta);
		
		return item;
	}
	
	@Override
	public Location getLocation()
	{
		return centerTownBlock.getBlock().getLocation();
	}
	
	@Override
	public UUID getUUID()
	{
		return uuid;
	}
	
	@Override
	public void load()
	{
		if(this instanceof HologramStructure hologramStructure)
		{
			hologramStructure.getHologram().load(town.getPlugin());
		}
	}
}
