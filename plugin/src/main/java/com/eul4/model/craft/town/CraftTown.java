package com.eul4.model.craft.town;

import com.eul4.Main;
import com.eul4.Price;
import com.eul4.StructureType;
import com.eul4.exception.CannotConstructException;
import com.eul4.exception.InsufficientBalanceException;
import com.eul4.exception.StructureLimitException;
import com.eul4.exception.TownHardnessLimitException;
import com.eul4.model.craft.town.structure.CraftDislikeGenerator;
import com.eul4.model.craft.town.structure.CraftLikeGenerator;
import com.eul4.model.craft.town.structure.CraftTownHall;
import com.eul4.model.player.Attacker;
import com.eul4.model.player.PluginPlayer;
import com.eul4.model.player.RaidAnalyzer;
import com.eul4.model.town.Town;
import com.eul4.model.town.TownBlock;
import com.eul4.model.town.TownTile;
import com.eul4.model.town.structure.*;
import com.eul4.wrapper.StructureSet;
import com.eul4.wrapper.TownAttack;
import com.eul4.wrapper.TownBlockMap;
import com.eul4.wrapper.TownTileMap;
import com.fastasyncworldedit.core.FaweAPI;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.entity.BaseEntity;
import com.sk89q.worldedit.entity.Entity;
import com.sk89q.worldedit.extent.clipboard.BlockArrayClipboard;
import com.sk89q.worldedit.extent.clipboard.io.*;
import com.sk89q.worldedit.function.operation.ForwardExtentCopy;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.session.ClipboardHolder;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.List;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.function.Consumer;
import java.util.function.IntConsumer;
import java.util.function.Predicate;
import java.util.logging.Level;

@Getter
@Setter
public class CraftTown implements Town
{
	public static final Map<Block, TownBlock> TOWN_BLOCKS = new HashMap<>();
	
	private final UUID ownerUUID;
	private final Block block;
	
	private final Main plugin;
	
	private TownBlockMap townBlockMap;
	private TownTileMap townTileMap;
	private StructureSet structureSet;
	
	private transient Structure movingStructure;
	private transient ClipboardHolder movingStructureClipboardHolder;
	
	private final transient Consumer<Player> removeMovingStructureItem;
	
	private int likes;
	private int dislikes;
	
	private double hardness;
	
	private transient int likeCapacity;
	private transient int dislikeCapacity;
	
	private TownHall townHall;
	private Armory armory;
	private transient boolean frozen;
	
	@Setter
	private Attacker attacker;
	@Setter
	private RaidAnalyzer analyzer;
	
	private transient TownAttack currentAttack;
	
	@Setter
	private long lastAttackFinishTick;
	
	public CraftTown(UUID ownerUUID, Block block, Main plugin)
	{
		this.ownerUUID = ownerUUID;
		this.block = block;
		this.plugin = plugin;
		this.removeMovingStructureItem = player ->
				player.getInventory().removeItemAnySlot(movingStructure.getItem());
	}
	
	public CraftTown(OfflinePlayer owner, Block block, Main plugin) throws CannotConstructException, IOException
	{
		this(owner.getUniqueId(), block, plugin);
		
		this.townBlockMap = getInitialTownBlocks();
		this.townTileMap = getInitialTownTiles();
		this.structureSet = new StructureSet();
		
		createInitialStructures();
		reloadAllStructureAttributes();
		
		TOWN_BLOCKS.putAll(townBlockMap);
	}
	
	private TownBlockMap getInitialTownBlocks()
	{
		if(townBlockMap != null)
		{
			return townBlockMap;
		}
		
		TownBlockMap townBlockMap = new TownBlockMap();
		
		for(int x = -Town.TOWN_FULL_RADIUS; x <= Town.TOWN_FULL_RADIUS; x++)
		{
			for(int z = -Town.TOWN_FULL_RADIUS; z <= Town.TOWN_FULL_RADIUS; z++)
			{
				Block block = getLocation().getBlock().getRelative(x, 0, z);
				
				townBlockMap.put(block, new CraftTownBlock(this, block, isInInitialAvailableRadius(x, z)));
			}
		}
		
		return townBlockMap;
	}
	
	private TownTileMap getInitialTownTiles()
	{
		if(townTileMap != null)
		{
			return townTileMap;
		}
		
		TownTileMap townTileMap = new TownTileMap();
		
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
			townTileMap.put(block, new CraftTownTile(this, block, isInTownBorder(i, x, z)));
			
			if(x == z || x < 0 && x == -z || x > 0 && x == 1 - z)
			{
				int temp = dx;
				dx = -dz;
				dz = temp;
			}
			
			x += dx;
			z += dz;
		}
		
		return townTileMap;
	}
	
	public void createInitialStructures() throws CannotConstructException, IOException
	{
		TownBlock centerTownBlock = getTownBlock(block);
		Block centerBlock = centerTownBlock.getBlock();
		
		TownBlock likeFarmTownBlock = getTownBlock(centerBlock.getRelative(-10, 0, -3));
		TownBlock dislikeFarmTownBlock = getTownBlock(centerBlock.getRelative(-10, 0, 3));
		
		townHall = new CraftTownHall(this, centerTownBlock, true);
		new CraftLikeGenerator(this, likeFarmTownBlock, true);
		new CraftDislikeGenerator(this, dislikeFarmTownBlock, true);
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
		
		getPlayer().ifPresent(removeMovingStructureItem);
		
		Structure movingStructure = this.movingStructure;
		ClipboardHolder movingStructureClipboardHolder = this.movingStructureClipboardHolder;
		
		this.movingStructure = null;
		this.movingStructureClipboardHolder = null;
		
		movingStructure.construct(movingStructureClipboardHolder);
	}
	
	@Override
	public void finishMovingStructure(TownBlock centerTownBlock, int rotation) throws CannotConstructException
	{
		if(!isMovingStructure())
		{
			return;
		}
		
		movingStructure.construct(movingStructureClipboardHolder, centerTownBlock, rotation);
		movingStructure.teleportHologramToDefaultLocation();
		
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
		return townBlockMap.get(fixedBlockY);
	}
	
	@Override
	public Optional<TownBlock> findTownBlock(Block block)
	{
		return Optional.ofNullable(getTownBlock(block));
	}
	
	@Override
	public Location getLocation()
	{
		return block.getLocation();
	}
	
	@Override
	public TownTile getTile(Point point)
	{
		return townTileMap.get(getLocation().getBlock()
				.getRelative(point.x * TownTile.DIAMETER, 0, point.y * TownTile.DIAMETER));
	}
	
	@Override
	public void load()
	{
		structureSet.forEach(Structure::load);
		reloadAllStructureAttributes();
		TOWN_BLOCKS.putAll(townBlockMap);
	}
	
	@Override
	public void setCappedLikes(int likes)
	{
		this.likes = Math.min(likeCapacity, likes);
		onLikesChanged();
	}
	
	@Override
	public void setCappedDislikes(int dislikes)
	{
		this.dislikes = Math.min(dislikeCapacity, dislikes);
		onDislikesChanged();
	}
	
	@Override
	public void addStructure(Structure structure)
	{
		structureSet.add(structure);
	}
	
	@Override
	public void subtract(Price price)
	{
		this.likes -= price.getLikes();
		this.dislikes -= price.getDislikes();
	}
	
	private int subtractCount(int currentValue, int subtractedValue, IntConsumer setBalanceOperation)
	{
		if(subtractedValue < 0)
		{
			throw new UnsupportedOperationException("subtracted value can't be negative.");
		}
		
		if(subtractedValue > currentValue)
		{
			setBalanceOperation.accept(0);
			return currentValue;
		}
		
		setBalanceOperation.accept(currentValue - subtractedValue);
		return subtractedValue;
	}
	
	@Override
	public int subtractDislikes(int dislikes)
	{
		return subtractCount(this.dislikes, dislikes, this::setCappedDislikes);
	}
	
	@Override
	public int subtractLikes(int likes)
	{
		return subtractCount(this.likes, likes, this::setCappedLikes);
	}
	
	@Override
	public void addLikes(int likes)
	{
		setCappedLikes(this.likes + likes);
	}
	
	@Override
	public void addDislikes(int dislikes)
	{
		setCappedDislikes(this.dislikes + dislikes);
	}
	
	@Override
	public void checkIfAffordable(Price price) throws InsufficientBalanceException
	{
		int missingLikes = price.getLikes() - likes;
		int missingDislikes = price.getDislikes() - dislikes;
		
		if(missingLikes > 0 || missingDislikes > 0)
		{
			throw new InsufficientBalanceException(missingLikes, missingDislikes);
		}
	}
	
	@Override
	public Price buyNewStructure(StructureType structureType, TownBlock townBlock)
			throws StructureLimitException, CannotConstructException, IOException, InsufficientBalanceException
	{
		final int count = countStructures(structureType);
		final int max = getStructureLimit(structureType);
		
		if(count >= max)
		{
			throw new StructureLimitException(count, max);
		}
		
		Price price = structureType.getRule(plugin).getAttribute(1).getPrice();
		checkIfAffordable(price);
		structureType.getInstantiation().newInstance(this, townBlock);
		subtract(price);
		
		return price;
	}
	
	public void resetAttributes()
	{
		likeCapacity = calculateLikeCapacity();
		dislikeCapacity = calculateDislikeCapacity();
	}
	
	public int calculateLikeCapacity()
	{
		int likeCapacity = townHall.getLikeCapacity();
		
		for(Structure structure : structureSet)
		{
			if(structure instanceof LikeDeposit likeDeposit)
			{
				likeCapacity += likeDeposit.getCapacity();
			}
		}
		
		return likeCapacity;
	}
	
	public int calculateDislikeCapacity()
	{
		int dislikeCapacity = townHall.getDislikeCapacity();
		
		for(Structure structure : structureSet)
		{
			if(structure instanceof DislikeDeposit dislikeDeposit)
			{
				dislikeCapacity += dislikeDeposit.getCapacity();
			}
		}
		
		return dislikeCapacity;
	}
	
	@Override
	public void reloadAllStructureAttributes()
	{
		structureSet.forEach(Structure::reloadAttributes);
		resetAttributes();
	}
	
	@Override
	public void reloadAttributes()
	{
		resetAttributes();
	}
	
	@Override
	public int countStructures(StructureType structureType)
	{
		int count = 0;
		
		for(Structure structure : structureSet)
		{
			if(structure.getStructureType() == structureType)
			{
				count++;
			}
		}
		
		return count;
	}
	
	@Override
	public int getStructureLimit(StructureType structureType)
	{
		return townHall.getStructureLimitMap().getOrDefault(structureType, 0);
	}
	
	@Override
	public double getHardness()
	{
		return hardness;
	}
	
	@Override
	public double getHardnessLimit()
	{
		return 20000.0D;
	}
	
	@Override
	public void increaseHardness(double hardness) throws TownHardnessLimitException
	{
		if(this.hardness + hardness > getHardnessLimit())
		{
			throw new TownHardnessLimitException();
		}
		
		this.hardness += hardness;
	}
	
	@Override
	public void decreaseHardness(double hardness)
	{
		this.hardness = Math.max(0.0D, this.hardness - hardness);
	}
	
	@Override
	public void setHardness(double hardness) throws TownHardnessLimitException
	{
		if(hardness > getHardnessLimit())
		{
			throw new TownHardnessLimitException();
		}
		
		this.hardness = Math.max(0.0D, hardness);
	}
	
	@Override
	public boolean isOwner(Player player)
	{
		return player.getUniqueId().equals(ownerUUID);
	}
	
	@Override
	public boolean isOnline()
	{
		return getOwner().isOnline();
	}
	
	@Override
	public int getLevel()
	{
		return townHall.getLevel();
	}
	
	@Override
	public boolean isUnderAttack()
	{
		return currentAttack != null && !currentAttack.isFinished();
	}
	
	@Override
	public boolean isUnderAnalysis()
	{
		return analyzer != null;
	}
	
	@Override
	public boolean canBeAnalyzed()
	{
		return !isUnderAttack() && !isUnderAnalysis() && !isInCooldown();
	}
	
	private boolean isInCooldown()
	{
		return lastAttackFinishTick > 0L
				&& plugin.getTotalTick() - lastAttackFinishTick < 20L * 20L; //TODO: increase cooldown later
	}
	
	@Override
	public void setHardnessField(double hardness)
	{
		this.hardness = hardness;
	}
	
	@Override
	public void setCurrentAttack(TownAttack currentAttack)
	{
		if(isUnderAttack())
		{
			throw new RuntimeException(); //TODO ...
		}
		
		if(!currentAttack.isStarting())
		{
			throw new RuntimeException(); //TODO ...
		}
		
		this.currentAttack = currentAttack;
	}
	
	@Override
	public TownBlock getUnavailableRandomTownBlock()
	{
		List<TownBlock> unavailableTownBlocks = new ArrayList<>(Town.TOWN_FULL_DIAMATER * Town.TOWN_FULL_DIAMATER);
		
		for(TownBlock townBlock : townBlockMap.values())
		{
			if(!townBlock.isAvailable())
			{
				unavailableTownBlocks.add(townBlock);
			}
		}
		
		return unavailableTownBlocks.isEmpty()
				? null
				: unavailableTownBlocks.get(new Random().nextInt(unavailableTownBlocks.size()));
	}
	
	@Override
	public PluginPlayer getPluginPlayer()
	{
		return getPlayer()
				.map(plugin.getPlayerManager()::get)
				.map(PluginPlayer.class::cast)
				.orElse(null);
	}
	
	@Override
	public Future<Void> copyAndSaveTownSchematic(ExecutorService executorService)
	{
		if(frozen)
		{
			throw new ConcurrentModificationException("Can't copy/save town schematic in a frozen town.");
		}
		
		frozen = true;
		return executorService.submit(this::saveSchematic);
	}
	
	@Override
	public Future<Void> loadAndPasteTownSchematic(ExecutorService executorService)
	{
		if(frozen)
		{
			throw new ConcurrentModificationException("Can't load/paste town schematic in a frozen town.");
		}
		
		frozen = true;
		return executorService.submit(this::loadSchematic);
	}
	
	@Override
	public void loadAndPasteTownSchematic()
	{
		if(frozen)
		{
			throw new ConcurrentModificationException("Can't load/paste town schematic in a frozen town.");
		}
		
		frozen = true;
		plugin.getServer()
				.getScheduler()
				.getMainThreadExecutor(plugin)
				.execute(this::loadSchematicSneaky);
	}
	
	@SneakyThrows
	private void loadSchematicSneaky()
	{
		loadSchematic();
	}
	
	private Void loadSchematic() throws Exception
	{
		frozen = true;
		File file = null;
		
		try
		{
			final Block origin = block;
			final BlockVector3 to = BlockVector3.at(origin.getX(), origin.getY(), origin.getZ());
			final World world = origin.getWorld();
			final var weWorld = FaweAPI.getWorld(world.getName());
			
			file = plugin.getDataFileManager().getTownSchematicFile(this);
			
			if(!file.exists())
			{
				throw new IOException("File " + file.getName() + " not exists.");
			}
			
			ClipboardFormat format = ClipboardFormats.findByFile(file);
			
			if(format == null)
			{
				throw new IOException("File format not supported.");
			}
			
			try(ClipboardReader reader = format.getReader(new FileInputStream(file));
					EditSession editSession = WorldEdit.getInstance().newEditSession(weWorld))
			{
				editSession.setFastMode(true);
				Operation operation = new ClipboardHolder(reader.read())
						.createPaste(editSession)
						.to(to)
						.copyEntities(true)
						.ignoreAirBlocks(false)
						.build();
				
				Operations.complete(operation);
			}
		}
		catch(Exception e)
		{
			plugin.getLogger().log(Level.SEVERE, MessageFormat.format(
					"Failed to paste town schematic! uuid={0}",
					ownerUUID), e);
			throw e;
		}
		finally
		{
			frozen = false;
			
			if(file != null)
			{
				if(!file.delete())
				{
					throw new IOException("Failed to delete file.");
				}
			}
		}
		
		if(Thread.interrupted())
		{
			throw new InterruptedException();
		}
		
		return null;
	}
	
	private Void saveSchematic() throws Exception
	{
		Thread.sleep(3000L);
		frozen = true;
		
		try
		{
			Block center = block;
			World world = center.getWorld();
			var weWorld = FaweAPI.getWorld(world.getName());
			
			BlockVector3 originPos = BlockVector3.at(center.getX(), center.getY(), center.getZ());
			BlockVector3 pos1 = BlockVector3.at(center.getX() - Town.TOWN_FULL_RADIUS, 0, center.getZ() - Town.TOWN_FULL_RADIUS);
			BlockVector3 pos2 = BlockVector3.at(center.getX() + Town.TOWN_FULL_RADIUS, world.getMaxHeight(), center.getZ() + Town.TOWN_FULL_RADIUS);
			
			CuboidRegion region = new CuboidRegion(weWorld, pos1, pos2);
			BlockArrayClipboard clipboard1 = new BlockArrayClipboard(region);
			BlockArrayClipboard clipboard2 = new BlockArrayClipboard(region);
			clipboard1.setOrigin(originPos);
			clipboard2.setOrigin(originPos);
			
			try(EditSession editSession = WorldEdit.getInstance().newEditSession(weWorld))
			{
				ForwardExtentCopy operation = new ForwardExtentCopy(editSession, region, clipboard1, region.getMinimumPoint());
				operation.setCopyingEntities(true);
				Operations.complete(operation);
			}
			
			List<? extends Entity> weEntitiesToCopy = clipboard1.getEntities()
					.stream()
					.filter(Predicate.not(this::hasFaweIgnoreFlag))
					.toList();
			
			try(EditSession editSession = WorldEdit.getInstance().newEditSession(weWorld))
			{
				ForwardExtentCopy operation = new ForwardExtentCopy(editSession, region, clipboard2, region.getMinimumPoint());
				operation.setCopyingEntities(false);
				Operations.complete(operation);
			}

			for(var weEntity : weEntitiesToCopy)
			{
				clipboard2.createEntity(weEntity.getLocation(), weEntity.getState());
			}
			
			try(FileOutputStream fileOutputStream = new FileOutputStream(plugin.getDataFileManager().createTownSchematicFile(this));
					ClipboardWriter writer = BuiltInClipboardFormat.FAST.getWriter(fileOutputStream))
			{
				writer.write(clipboard2);
				plugin.getLogger().info("Town schematic saved! uuid=" + ownerUUID);
			}
			
			//TODO: concurrent bug if save occurs here
			CountDownLatch countDownLatch = new CountDownLatch(1);
			
			plugin.getServer().getScheduler().getMainThreadExecutor(plugin).execute(() ->
			{
				try
				{
					for(var weEntity : clipboard2.getEntities())
					{
						plugin.getEntityRegisterListener()
								.getEntityByUuid(weEntity.getState().getNbtData().getUUID())
								.remove();
					}
				}
				finally
				{
					countDownLatch.countDown();
				}
			});
			
			countDownLatch.await();
		}
		catch(Exception e)
		{
			plugin.getLogger().log(Level.SEVERE, MessageFormat.format(
					"Failed to save town schematic! uuid={0}",
					ownerUUID), e);
			throw e;
		}
		finally
		{
			frozen = false;
		}
		
		if(Thread.interrupted())
		{
			throw new InterruptedException();
		}
		
		return null;
	}
	
	private boolean hasFaweIgnoreFlag(Entity entity)
	{
		return Optional.ofNullable(entity.getState())
				.map(BaseEntity::getNbt)
				.map(nbt -> nbt.getCompound("BukkitValues"))
				.map(nbt -> nbt.getBoolean("plugin:fawe_ignore", false)) //TODO nameSpace?
				.orElse(false);
	}
	
	public void updateTileHolograms()
	{
		townTileMap.values().forEach(TownTile::updateHologram);
	}
	
	public void updateStructureHolograms()
	{
		structureSet.forEach(Structure::updateHologram);
	}
	
	@Override
	public void updateHolograms()
	{
		updateTileHolograms();
		updateStructureHolograms();
	}
	
	public void setLikes(int likes)
	{
		this.likes = likes;
		onLikesChanged();
	}
	
	public void setDislikes(int dislikes)
	{
		this.dislikes = dislikes;
		onDislikesChanged();
	}
	
	private void onLikesChanged()
	{
		structureSet.forEach(Structure::onTownLikeBalanceChange);
	}
	
	private void onDislikesChanged()
	{
		structureSet.forEach(Structure::onTownDislikeBalanceChange);
	}
	
	@Override
	public Optional<PluginPlayer> findPluginPlayer()
	{
		return Optional.ofNullable(getPluginPlayer());
	}
	
	@Override
	public void updateLastAttackFinishDate()
	{
		this.lastAttackFinishTick = plugin.getTotalTick();
	}
	
	@Override
	public Optional<Armory> findArmory()
	{
		return Optional.ofNullable(armory);
	}
}
