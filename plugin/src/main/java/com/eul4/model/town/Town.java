package com.eul4.model.town;

import com.eul4.Main;
import com.eul4.Price;
import com.eul4.StructureType;
import com.eul4.economy.Transaction;
import com.eul4.exception.*;
import com.eul4.holder.CapacitatedCrownHolder;
import com.eul4.model.craft.town.CraftTown;
import com.eul4.model.player.spiritual.Attacker;
import com.eul4.model.player.PluginPlayer;
import com.eul4.model.player.spiritual.RaidAnalyzer;
import com.eul4.model.town.structure.*;
import com.eul4.wrapper.*;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.util.BoundingBox;

import java.awt.*;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

public interface Town
{
	Map<UUID, Town> ASSISTANT_UUID_MAP = new HashMap<>();
	
	int TOWN_RADIUS = 49;
	int TOWN_FULL_RADIUS = 55;
	int TOWN_FULL_RADIUS_EXCLUDING_WALLS = TOWN_FULL_RADIUS - 1;
	int TOWN_FULL_DIAMATER = 55 * 2 + 1;
	int INITIAL_AVAILABLE_RADIUS = 13;
	int Y = 49;
	int GRASS_Y = Y;
	int BED_ROCK_Y = 41;
	
	static TownBlock getStaticTownBlock(Block block)
	{
		final Block fixedBlockY = block.getWorld().getBlockAt(block.getX(), Y, block.getZ());
		return CraftTown.TOWN_BLOCKS.get(fixedBlockY);
	}
	
	static Optional<TownBlock> findStaticTownBlock(Block block)
	{
		return Optional.ofNullable(getStaticTownBlock(block));
	}
	
	UUID getTownUniqueId();
	
	TownBlock getTownBlock(Block block);
	Optional<TownBlock> findTownBlock(Block block);
	Location getLocation();
	TownTile getTile(Point point);
	Main getPlugin();
	
	OfflinePlayer getOwner();
	Player getPlayer();
	Optional<Player> findPlayer();
	
	
	void load();
	
	
	int getLikes();
	int getLikeCapacity();
	int getDislikes();
	int getDislikeCapacity();
	
	void setCappedLikes(int likes);
	
	void setCappedDislikes(int dislikes);
	
	void addStructure(Structure structure);
	
	void subtract(Price price);
	int subtractDislikes(int dislikes);
	int subtractLikes(int likes);
	void addLikes(int likes);
	void addDislikes(int dislikes);
	void checkIfAffordable(Price price) throws InsufficientBalanceException;
	
	Price buyNewStructure(StructureType structureType, TownBlock townBlock)
			throws StructureLimitException, StructureNotForSaleException, CannotConstructException, IOException, InsufficientBalanceException;
	
	TownHall getTownHall();
	void reloadAllStructureAttributes();
	
	int countStructures(StructureType structureType);
	int getStructureLimit(StructureType structureType);
	
	void reloadAttributes();
	
	double getHardness();
	double getHardnessLimit();
	void decreaseHardness(double hardness);
	void increaseHardness(double hardness) throws TownHardnessLimitException;
	
	void setHardness(double hardness) throws TownHardnessLimitException;
	boolean isOwner(Player player);
	boolean isOnline();
	
	int getLevel();
	int getBuiltLevel();
	
	boolean isUnderAttack();
	Attacker getAttacker();
	RaidAnalyzer getAnalyzer();
	void setAttacker(Attacker attacker);
	void setAnalyzer(RaidAnalyzer analyzer);
	
	boolean isUnderAnalysis();
	
	boolean canBeAnalyzed();
	
	UUID getOwnerUniqueId();
	
	TownTileMap getTownTileMap();
	TownBlockMap getTownBlockMap();
	StructureMap getStructureMap();
	
	//TODO: remove set methods, create an unique method, like in TownTile#loadFields
	void setTownBlockMap(TownBlockMap townBlockMap);
	void setTownTileMap(TownTileMap townTileMap);
	void setStructureMap(StructureMap structureMap);
	void setTownHall(TownHall townHall);
	void setArmory(Armory armory);
	void setLikes(int likes);
	void setDislikes(int dislike);
	void setHardnessField(double hardness);
	void setLastAttackFinishTick(long lastAttackFinishTick);
	void setBoughtTileMapByDepth(BoughtTileMapByDepth boughtTileMapByDepth);
	void setTilesBought(int tilesBought);
	
	Block getBlock();
	
	TownAttack getCurrentAttack();
	
	void setCurrentAttack(TownAttack currentAttack);
	
	TownBlock getUnavailableRandomTownBlock();
	PluginPlayer getPluginPlayer();
	
	boolean isFrozen();
	
	Future<Void> copyAndSaveTownSchematic(ExecutorService executorService);
	
	Future<Void> loadAndPasteTownSchematic(ExecutorService executorService);
	void loadAndPasteTownSchematic();
	
	void updateTileHolograms();
	
	void updateHolograms();
	
	Optional<PluginPlayer> findPluginPlayer();
	void updateLastAttackFinishDate();
	long getLastAttackFinishTick();
	Armory getArmory();
	Optional<Armory> findArmory();
	
	BoundingBox getBoundingBoxExcludingWalls();
	
	BoundingBox getBoundingBox();
	
	Location getRandomSpawnLocation();
	
	BoughtTileMapByDepth getBoughtTileMapByDepth();
	int getTilesBought();
	
	boolean hasReachedMaxLikeCapacity();
	boolean hasReachedMaxDislikeCapacity();
	
	Component getOwnerDisplayName();
	
	Optional<RaidAnalyzer> findAnalyzer();
	void clearAnalisys();
	
	int getLikesIncludingGenerators();
	int getLikeCapacityIncludingGenerators();
	int getDislikesIncludingGenerators();
	int getDislikeCapacityIncludingGenerators();
	
	int getLikesInGenerators();
	int getDislikesInGenerators();
	void setLikesInGenerators(int likesInGenerators);
	void setDislikesInGenerators(int dislikesInGenerators);
	
	void onTileBought(TownTile townTile);
	
	void setDefaultTilesBought();
	
	@Deprecated
	void setDefaultBoughtTileMapByDepth();
	
	void setMovingStructure(Structure structure);
	Structure getMovingStructure();
	
	UUID getUUID();
	
	Structure getStructureByUniqueId(UUID structureUUID);
	
	Villager getAssistant();
	Optional<Villager> findAssistant();
	
	Optional<LikeGenerator> findFirstLikeGenerator();
	Optional<DislikeGenerator> findFirstDislikeGenerator();
	
	World getWorld();
	
	boolean isInside(Location location);
	
	boolean hasFinishedTutorial();
	void setFinishedTutorial(boolean finishedTutorial);
	void setDefaultFinishedTutorial();
	
	void spawnAssistant(Block block) throws CannotConstructException;
	
	EntityItemMoveMap getEntityItemMoveMap();
	void startMoveAssistant();
	
	void scheduleAssistantTargetTaskIfPossible();
	
	Optional<CraftTown.AssistantTargetTask> findAssistantTargetTask();
	CraftTown.AssistantTargetTask getAssistantTargetTask();
	
	boolean hasAssistant();
	
	boolean canScheduleAssistantTargetTask();
	
	void setAssistant(Villager villager);
	
	boolean hasReachedStructureLimit(StructureType structureType);
	
	BigDecimal calculateCrownCapacity();
	BigDecimal calculateRemainingCrownCapacity();
	
	BigDecimal getCalculatedCrownBalance();
	void updateCrownBalance();
	
	List<CapacitatedCrownHolder> getCapacitatedCrownHolders();
	
	List<TradePreview<BigDecimal, CapacitatedCrownHolder>> createTradePreviewSubtract(BigDecimal subtrahend)
			throws NegativeBalanceException;
	
	Transaction<BigDecimal> createTransaction(Town townTo, BigDecimal amount) throws
			OverCapacityException,
			NegativeBalanceException;
}
