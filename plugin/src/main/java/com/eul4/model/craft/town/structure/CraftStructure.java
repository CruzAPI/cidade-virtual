package com.eul4.model.craft.town.structure;

import com.eul4.common.hologram.Hologram;
import com.eul4.common.model.player.CommonPlayer;
import com.eul4.common.wrapper.Pitch;
import com.eul4.enums.StructureStatus;
import com.eul4.event.StructureConstructEvent;
import com.eul4.exception.*;
import com.eul4.i18n.PluginMessage;
import com.eul4.model.inventory.StructureGui;
import com.eul4.model.town.Town;
import com.eul4.model.town.TownBlock;
import com.eul4.model.town.structure.Structure;
import com.eul4.rule.Rule;
import com.eul4.rule.attribute.GenericAttribute;
import com.eul4.wrapper.TownBlockSet;
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
import lombok.Setter;
import lombok.SneakyThrows;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.FallingBlock;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Setter
@Getter
public abstract class CraftStructure implements Structure
{
	@Getter
	protected final Town town;
	
	private UUID uuid;
	
	private TownBlock centerTownBlock;
	
	protected int level = 0;
	private int rotation;
	
	@Getter
	private TownBlockSet townBlockSet = new TownBlockSet();
	
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
	private final double maxHealth = 30.0D; //TODO generic attribute
	private transient double health = maxHealth;
	
	private transient boolean destroyed;
	private transient final int maxNoDamageTicks = 20;
	private transient int noDamageTicks;
	private transient double lastDamage;
	private transient int lastTickDamaged;
	
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
		this.buildTicks = isBuilt ? 0 : getRule().getAttribute(1).getTotalBuildTicks();
		this.totalBuildTicks = buildTicks;
		
		resetAttributes();
		
		this.hologram = new Hologram(town.getPlugin(),
				centerTownBlock.getBlock().getLocation().add(hologramRelativePosition));
		
		construct(loadSchematic(), centerTownBlock, 0);
		updateHologram();
		
		town.addStructure(this);
		scheduleBuildTaskIfPossible();
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
		
		TownBlockSet townBlockSet = new TownBlockSet();
		
		for(int x = relativeMinX; x <= relativeMaxX; x++)
		{
			for(int z = relativeMinZ; z <= relativeMaxZ; z++)
			{
				TownBlock townBlock = town.getTownBlock(center.getRelative(x, 0, z));
				
				townBlockSet.add(townBlock);
				
				if(townBlock == null || !townBlock.isAvailable()
						|| (townBlock.hasStructure() && townBlock.getStructure() != this))
				{
					throw new CannotConstructException();
				}
			}
		}
		
		this.townBlockSet.removeAll(townBlockSet);
		this.townBlockSet.forEach(TownBlock::reset);
		
		this.townBlockSet = townBlockSet;
		this.townBlockSet.forEach(townBlock -> townBlock.setStructure(this));
		
		town.getPlugin().getServer().getPluginManager().callEvent(new StructureConstructEvent(this));
		
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
		resetAttributes();
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
		onBuildStart();
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
	public void teleportHologramToDefaultLocation()
	{
		hologram.teleport(getLocation().add(getHologramRelativePosition()));
	}
	
	@Override
	public void teleportHologramRelative(Vector3 vector3)
	{
		hologram.teleport(getLocation().add(toBukkitVector(vector3)));
	}
	
	@Override
	public void teleportHologram(Location location)
	{
		hologram.teleport(location);
	}
	
	private Vector toBukkitVector(Vector3 vector3)
	{
		return new Vector(vector3.getX(), vector3.getY(), vector3.getZ());
	}
	
	protected void onBuildStart()
	{
		reloadAttributes();
	}
	
	protected void onBuildFinish()
	{
		updateHologram();
		reloadAttributesAndReloadTownAttributes();
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
		if(status == StructureStatus.UNREADY && !town.isUnderAttack())
		{
			teleportHologramToDefaultLocation();
			hologram.setSize(2);
			hologram.getLine(0).setMessageAndArgs(PluginMessage.STRUCTURE_READY_IN, buildTicks);
			hologram.getLine(1).setCustomName(getProgressBarComponent());
		}
		else if(status == StructureStatus.READY && !town.isUnderAttack())
		{
			teleportHologramToDefaultLocation();
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
		int buildTicks = getRule().getAttribute(nextLevel).getTotalBuildTicks();
		
		construct(loadSchematic(getSchematicFile(nextLevel, StructureStatus.UNREADY)));
		
		this.status = StructureStatus.UNREADY;
		this.buildTicks = buildTicks;
		this.totalBuildTicks = buildTicks;
		this.level = nextLevel;
		
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
	public void resetAttributes()
	{
		hologramRelativePosition = getRule().getAttribute(level).getHologramVector();
	}
	
	@Override
	public void reloadAttributes()
	{
		resetAttributes();
		updateHologram();
		teleportHologramToDefaultLocation();
	}
	
	@Override
	public void reloadAttributesAndReloadTownAttributes()
	{
		reloadAttributes();
		town.reloadAttributes();
	}
	
	@Override
	public boolean hasUpgradeUnlocked()
	{
		GenericAttribute nextAttribute = getRule().getAttribute(level + 1);
		
		return nextAttribute != null && town.getTownHall().getLevelStatus() >= nextAttribute.getRequiresTownHallLevel();
	}
	
	@Override
	public final int getHealthPercentage()
	{
		final int percentage = (int) (100 * health / maxHealth);
		return Math.max(0, Math.min(100, percentage));
	}
	
	@Override
	public void damage(double originalDamage, Block hitBlock)
	{
		if(!town.isUnderAttack() || destroyed)
		{
			return;
		}
		
		double damage = originalDamage;
		
		if(isInNoDamageTicks())
		{
			if(damage > lastDamage)
			{
				damage -= lastDamage;
			}
			else
			{
				return;
			}
		}
		else
		{
			playHurtEffect(hitBlock);
			lastTickDamaged = town.getPlugin().getServer().getCurrentTick();
		}
		
		lastDamage = originalDamage;
		
		if(health - damage <= 0.0D)
		{
			destroy();
		}
		else
		{
			health -= damage;
		}
		
		updateHologram();
	}
	
	private void playHurtEffect(Block hitBlock)
	{
		if(!town.isUnderAttack())
		{
			return;
		}
		
		Location centerLocation = hitBlock.getLocation().toCenterLocation();
		World world = centerLocation.getWorld();
		
		world.spawnParticle(Particle.EXPLOSION_LARGE, centerLocation, 1);
		world.playSound(centerLocation, Sound.ENTITY_GENERIC_HURT, 1.0F, Pitch.getPitch(5));
	}
	
	private void playDestroyEffect()
	{
		Location centerLocation = centerTownBlock.getBlock().getLocation().toCenterLocation();
		World world = centerLocation.getWorld();
		
		world.playSound(centerLocation, Sound.ENTITY_GENERIC_EXPLODE, 1.0F, Pitch.getPitch(7));
		world.spawnParticle(Particle.EXPLOSION_HUGE, centerLocation, 1);
		
		List<BlockState> removedBlockStates = removeAllSurfaceBlocks();
		
		explodeBlocks(removedBlockStates);
	}
	
	private List<BlockState> removeAllSurfaceBlocks()
	{
		List<BlockState> blockStates = new ArrayList<>(townBlockSet.size() * 8);
		
		for(TownBlock townBlock : townBlockSet)
		{
			for(Block block = townBlock.getBlock().getRelative(BlockFace.UP);
					block.getY() < block.getWorld().getMaxHeight();
					block = block.getRelative(BlockFace.UP))
			{
				if(!block.isEmpty())
				{
					blockStates.add(block.getState());
					block.setType(Material.AIR);
				}
			}
		}
		
		return blockStates;
	}
	
	private void explodeBlocks(List<BlockState> blockStates)
	{
		blockStates.forEach(this::explodeBlock);
	}
	
	private void explodeBlock(BlockState blockState)
	{
		if(blockState.getType() == Material.AIR)
		{
			return;
		}
		
		Location centerLocation = blockState.getLocation().toCenterLocation();
		
		blockState.getWorld().spawnEntity(centerLocation,
				EntityType.FALLING_BLOCK,
				CreatureSpawnEvent.SpawnReason.EXPLOSION,
				entity -> onSpawnFallingBlock((FallingBlock) entity, blockState));
	}
	
	private void onSpawnFallingBlock(FallingBlock fallingBlock, BlockState blockState)
	{
		fallingBlock.setBlockData(blockState.getBlockData());
		fallingBlock.setCancelDrop(true);
		
		Vector vector = fallingBlock.getLocation().toVector().subtract(centerTownBlock
				.getBlock()
				.getLocation()
				.toCenterLocation()
				.toVector()).normalize();
		
		vector.setX(vector.getX() * 0.5D);
		vector.setY(Math.abs(vector.getY()) * (1.0D + Math.random()));
		vector.setZ(vector.getZ() * 0.5D);
		
		fallingBlock.setVelocity(vector);
	}
	
	private boolean destroy()
	{
		if(destroyed)
		{
			return false;
		}
		
		destroyed = true;
		health = 0.0D;
		
		onDestroy();
		return true;
	}
	
	protected void onDestroy()
	{
		playDestroyEffect();
	}
	
	private boolean isInNoDamageTicks()
	{
		return getNoDamageTicks() > maxNoDamageTicks / 2;
	}
	
	private int getNoDamageTicks()
	{
		final int currentTick = town.getPlugin().getServer().getCurrentTick();
		return Math.max(0, Math.min(maxNoDamageTicks, maxNoDamageTicks - (currentTick - lastTickDamaged)));
	}

	@Override
	public boolean isDestroyed()
	{
		return destroyed;
	}

	public void onStartAttack()
	{
		destroyed = false;
		health = maxHealth;
	}
	
	@Override
	public void onTownLikeBalanceChange()
	{
	
	}
	
	@Override
	public void onTownDislikeBalanceChange()
	{
	
	}
}
