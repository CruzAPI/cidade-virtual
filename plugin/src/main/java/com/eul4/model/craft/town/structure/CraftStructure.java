package com.eul4.model.craft.town.structure;

import com.eul4.common.hologram.Hologram;
import com.eul4.common.model.player.CommonPlayer;
import com.eul4.common.wrapper.BlockSerializable;
import com.eul4.enums.StructureStatus;
import com.eul4.exception.*;
import com.eul4.i18n.PluginMessage;
import com.eul4.model.inventory.StructureGui;
import com.eul4.model.town.Town;
import com.eul4.model.town.TownBlock;
import com.eul4.model.town.structure.Structure;
import com.eul4.rule.GenericAttribute;
import com.eul4.rule.Rule;
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
import joptsimple.internal.Strings;
import lombok.Getter;
import lombok.SneakyThrows;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

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
	
	protected int level = 0;
	private int rotation;
	
	private Set<TownBlock> townBlocks = new HashSet<>();
	
	@Getter
	protected StructureStatus status;
	
	@Getter
	private int buildTicks;
	@Getter
	private int totalBuildTicks;
	
	@Getter
	protected Hologram hologram;
	
	private BukkitRunnable buildTask;
	
	private transient Vector hologramRelativePosition;
	
	public CraftStructure(Town town)
	{
		this.town = town;
	}
	
	public CraftStructure(Town town, TownBlock centerTownBlock) throws CannotConstructException, IOException
	{
		this(town, centerTownBlock, false);
	}
	
	public CraftStructure(Town town, TownBlock centerTownBlock, boolean isBuilt) throws CannotConstructException, IOException
	{
		this(town);
		
		this.uuid = UUID.randomUUID();
		this.centerTownBlock = centerTownBlock;
		this.level = 1;
		this.status = isBuilt ? StructureStatus.BUILT : StructureStatus.UNREADY;
		this.buildTicks = isBuilt ? 0 : 30 * 20;
		this.totalBuildTicks = buildTicks;
		
		reloadAttributes();
		
		this.hologram = new Hologram(town.getPlugin(),
				centerTownBlock.getBlock().getLocation().add(hologramRelativePosition));
		
		construct(loadSchematic(), centerTownBlock, 0);
		updateHologram();
		
		town.addStructure(this);
		scheduleBuildTaskIfPossible();
	}
	
	@Override
	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException
	{
		final TownSerializer townSerializer = town.getPlugin().getTownSerializer();
		
		final long version = in.readLong();
		
		if(version == 1L)
		{
			uuid = (UUID) in.readObject();
			centerTownBlock = Objects.requireNonNull(town.getTownBlock(((BlockSerializable) in.readObject())
					.getBukkitBlock(town.getPlugin().getServer())));
			level = in.readInt();
			rotation = in.readInt();
			townBlocks = townSerializer.readStructureTownBlocks(this, in);
			status = StructureStatus.values()[in.readInt()];
			buildTicks = in.readInt();
			totalBuildTicks = in.readInt();
			hologram = (Hologram) in.readObject();
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
		
		out.writeObject(uuid);
		out.writeObject(new BlockSerializable(centerTownBlock.getBlock()));
		out.writeInt(level);
		out.writeInt(rotation);
		townSerializer.writeStructureTownBlocks(townBlocks, out);
		out.writeInt(status.ordinal());
		out.writeInt(buildTicks);
		out.writeInt(totalBuildTicks);
		out.writeObject(hologram);
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
		this.townBlocks.forEach(TownBlock::reset);
		
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
		return loadSchematic(getSchematicFile());
	}
	
	public ClipboardHolder loadSchematic(File schematicFile) throws IOException
	{
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
		return getSchematicFile(level, status);
	}
	
	public File getSchematicFile(int level, StructureStatus status)
	{
		return new File(town.getPlugin().getSchematicsFolder(), getName().toLowerCase() + "_" + level + "_" + status + ".schem");
	}
	
	@Override
	public String getName()
	{
		return getStructureType().name();
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
		hologram.load(town.getPlugin());
		reloadAttributes();
		scheduleBuildTaskIfPossible();
	}
	
	@Override
	public StructureGui newGui(CommonPlayer commonPlayer)
	{
		return getStructureType().getNewStructureGui().apply(commonPlayer, this);
	}
	
	private void scheduleBuildTaskIfPossible()
	{
		if(status != StructureStatus.UNREADY || town.getPlugin().isQueued(buildTask))
		{
			return;
		}
		
		buildTask = new BukkitRunnable()
		{
			@SneakyThrows
			@Override
			public void run()
			{
				if(buildTicks <= 0)
				{
					status = StructureStatus.READY;
					updateHologram();
					cancel();
					construct(loadSchematic());
					return;
				}
				
				updateHologram();
				buildTicks--;
			}
		};
		
		buildTask.runTaskTimer(town.getPlugin(), 0L, 1L);
	}
	
	@Override
	public void finishBuild() throws StructureAlreadyBuiltException,
			CannotBuildYetException,
			IOException,
			CannotConstructException
	{
		if(status == StructureStatus.BUILT)
		{
			throw new StructureAlreadyBuiltException();
		}
		
		if(buildTicks > 0)
		{
			throw new CannotBuildYetException();
		}
		
		try(ClipboardHolder clipboardHolder = loadSchematic(getSchematicFile(level, StructureStatus.BUILT)))
		{
			construct(clipboardHolder);
			status = StructureStatus.BUILT;
			onBuildFinish();
		}
	}
	
	@Override
	public boolean canFinishBuild()
	{
		return status == StructureStatus.READY;
	}
	
	@Override
	public void teleportHologram()
	{
		hologram.teleport(getLocation().add(getHologramRelativePosition()));
	}
	
	protected void onBuildStart()
	{
		reloadAttributes();
	}
	
	protected void onBuildFinish()
	{
		updateHologram();
		reloadAttributes();
	}
	
	public double getBuildProgressPercentage()
	{
		final double value = totalBuildTicks == 0 || status != StructureStatus.UNREADY
				? 100.0D
				: 100.0D - 100.0D * buildTicks / totalBuildTicks;
		
		return Math.max(0.0D, Math.min(100.0D, value));
	}
	
	public void updateHologram()
	{
		if(status == StructureStatus.UNREADY)
		{
			hologram.setSize(2);
			hologram.getLine(0).setMessageAndArgs(PluginMessage.STRUCTURE_READY_IN, buildTicks);
			hologram.getLine(1).setCustomName(getProgressBarComponent());
		}
		else if(status == StructureStatus.READY)
		{
			hologram.setSize(1);
			hologram.getLine(0).setMessageAndArgs(PluginMessage.CLICK_TO_FINISH_BUILD);
		}
		else
		{
			hologram.remove();
		}
	}
	
	@Override
	public final Component getProgressBarComponent()
	{
		int donePercentage = (int) Math.round(getBuildProgressPercentage());
		int remainingPercentage = 100 - donePercentage;
		
		Component done = Component.text(Strings.repeat('\u258F', donePercentage)).color(NamedTextColor.GREEN);
		Component remaining = Component.text(Strings.repeat('\u258F', remainingPercentage)).color(NamedTextColor.DARK_GRAY);
		
		return done.append(remaining).decorate(TextDecoration.BOLD).decoration(TextDecoration.ITALIC, false);
	}
	
	public void upgrade() throws StructureIllegalStatusException, IOException,
			CannotConstructException, UpgradeLockedException, UpgradeNotFoundException
	{
		checkUpgrade();
		
		if(status != StructureStatus.BUILT)
		{
			throw new StructureIllegalStatusException();
		}
		
		int nextLevel = level + 1;
		
		construct(loadSchematic(getSchematicFile(nextLevel, StructureStatus.UNREADY)));
		
		status = StructureStatus.UNREADY;
		buildTicks = 30 * 20;
		totalBuildTicks = buildTicks;
		level = nextLevel;
		
		scheduleBuildTaskIfPossible();
	}
	
	public void checkUpgrade() throws UpgradeLockedException, UpgradeNotFoundException
	{
		final var rule = getRule();
		final int nextLevel = level + 1;
		
		if(!rule.hasAttribute(nextLevel))
		{
			throw new UpgradeNotFoundException();
		}
		
		int townHallLevel = town.getTownHall().getLevelStatus();
		int levelRequired = rule.getAttribute(nextLevel).getRequiresTownHallLevel();
		
		if(townHallLevel < levelRequired)
		{
			throw new UpgradeLockedException(levelRequired);
		}
	}
	
	@Override
	public Vector getHologramRelativePosition()
	{
		return hologramRelativePosition;
	}
	
	@Override
	public int getLevelStatus()
	{
		return status == StructureStatus.BUILT ? level : level - 1;
	}
	
	public abstract Rule<? extends GenericAttribute> getRule();
	
	@Override
	public void reloadAttributes()
	{
		hologramRelativePosition = getRule().getAttribute(level).getHologramVector();
	}
	
	@Override
	public void reloadAttributesAndUpdateHologram()
	{
		reloadAttributes();
		updateHologram();
	}
	
	@Override
	public boolean hasUpgradeUnlocked()
	{
		GenericAttribute nextAttribute = getRule().getAttribute(level + 1);
		
		return nextAttribute != null && town.getTownHall().getLevelStatus() >= nextAttribute.getRequiresTownHallLevel();
	}
}
