package com.eul4.listener;

import com.destroystokyo.paper.event.block.BlockDestroyEvent;
import com.eul4.Main;
import com.eul4.common.util.ItemStackUtil;
import com.eul4.common.util.MathUtil;
import com.eul4.enums.Rarity;
import com.eul4.event.block.BlockBreakNaturallyEvent;
import com.eul4.i18n.PluginMessage;
import com.eul4.model.player.PluginPlayer;
import com.eul4.service.BlockData;
import com.eul4.util.RarityUtil;
import com.eul4.util.SoundUtil;
import com.eul4.wrapper.EnchantType;
import com.eul4.wrapper.StabilityFormula;
import io.papermc.paper.event.block.BlockBreakBlockEvent;
import io.papermc.paper.event.player.PlayerFlowerPotManipulateEvent;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.Component;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.data.type.Cocoa;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.util.Vector;

import java.util.*;

import static org.bukkit.block.BlockFace.*;

@RequiredArgsConstructor
public class BlockRarityListener implements Listener
{
	private final Main plugin;
	
	private final Random random = new Random();
	
	private static final Set<Material> CHECK_DOWNSIDE;
	private static final Set<Material> CHECK_UPSIDE;
	
	static
	{
		Set<Material> checkDownside = new HashSet<>();
		Set<Material> checkUpside = new HashSet<>();
		
		checkDownside.addAll(Tag.SAPLINGS.getValues());
		checkDownside.addAll(Tag.FLOWERS.getValues());
		checkDownside.add(Material.SUGAR_CANE);
		checkDownside.add(Material.CACTUS);
		checkDownside.add(Material.NETHER_WART);
		checkDownside.add(Material.SWEET_BERRIES);
		checkDownside.add(Material.BAMBOO);
		
		checkDownside.add(Material.MELON_SEEDS);
		checkDownside.add(Material.PUMPKIN_SEEDS);
		checkDownside.add(Material.WHEAT_SEEDS);
		checkDownside.add(Material.BEETROOT_SEEDS);
		checkDownside.add(Material.POTATO);
		checkDownside.add(Material.CARROT);
		checkDownside.remove(Material.CHORUS_FLOWER);
		
		checkUpside.add(Material.GLOW_BERRIES);
		checkUpside.add(Material.CAVE_VINES);
		checkUpside.add(Material.CAVE_VINES_PLANT);
		
		CHECK_DOWNSIDE = Collections.unmodifiableSet(checkDownside);
		CHECK_UPSIDE = Collections.unmodifiableSet(checkUpside);
	}
	
	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void cocoaPlacement(BlockPlaceEvent event)
	{
		Block block = event.getBlock();
		ItemStack item = event.getItemInHand();
		
		if(!(block.getBlockData() instanceof Cocoa cocoa))
		{
			return;
		}
		
		Block relative = block.getRelative(cocoa.getFacing());
		
		Rarity itemRarity = RarityUtil.getRarity(item);
		Rarity relativeRarity = RarityUtil.getRarity(plugin, relative);
		
		Player player = event.getPlayer();
		PluginPlayer pluginPlayer = (PluginPlayer) plugin.getPlayerManager().get(player);
		
		if(relativeRarity.compareTo(itemRarity) < 0)
		{
			event.setCancelled(true);
			pluginPlayer.sendMessage
			(
				itemRarity.getPlacementIncompatibilityMessage(),
				Component.translatable(item.getType().translationKey()),
				Component.translatable(relative.getType().translationKey())
			);
		}
	}
	
	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void chorusFlowerPlacement(BlockPlaceEvent event)
	{
		Block block = event.getBlock();
		ItemStack item = event.getItemInHand();
		
		if(block.getType() != Material.CHORUS_FLOWER)
		{
			return;
		}
		
		boolean hasAdjacentChorusPlant = false;
		Rarity adjacentChorusPlantMaxRarity = Rarity.MIN_RARITY;
		
		for(BlockFace face : new BlockFace[] { UP, EAST, WEST, NORTH, SOUTH, DOWN })
		{
			Block relative = block.getRelative(face);
			
			if(relative.getType() == Material.CHORUS_PLANT)
			{
				hasAdjacentChorusPlant = true;
				Rarity adjacentChorusPlantRarity = RarityUtil.getRarity(plugin, relative);
				
				if(adjacentChorusPlantRarity.compareTo(adjacentChorusPlantMaxRarity) > 0)
				{
					adjacentChorusPlantMaxRarity = adjacentChorusPlantRarity;
				}
			}
		}
		
		Block relativeDown = block.getRelative(DOWN);
		Rarity relativeDownRarity = RarityUtil.getRarity(plugin, relativeDown);
		Rarity itemRarity = RarityUtil.getRarity(item);
		
		Player player = event.getPlayer();
		PluginPlayer pluginPlayer = (PluginPlayer) plugin.getPlayerManager().get(player);
		
		if((!hasAdjacentChorusPlant || hasAdjacentChorusPlant && adjacentChorusPlantMaxRarity.compareTo(itemRarity) < 0)
				&& (relativeDown.getType() != Material.END_STONE || relativeDown.getType() == Material.END_STONE && relativeDownRarity.compareTo(itemRarity) < 0))
		{
			event.setCancelled(true);
			pluginPlayer.sendMessage
			(
				itemRarity.getPlacementIncompatibilityMessage(),
				Component.translatable(item.getType().translationKey()),
				Component.translatable(hasAdjacentChorusPlant ? Material.CHORUS_PLANT.translationKey() : relativeDown.getType().translationKey())
			);
		}
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void downsidePhysics(BlockPhysicsEvent event)
	{
		Block block = event.getBlock();
		
		if(!CHECK_DOWNSIDE.contains(block.getType()))
		{
			return;
		}
		
		plugin.execute(() ->
		{
			Block relativeDown = block.getRelative(DOWN);
			
			Rarity blockRarity = RarityUtil.getRarity(plugin, block);
			Rarity relativeDownRarity = RarityUtil.getRarity(plugin, relativeDown);
			
			if(relativeDownRarity.compareTo(blockRarity) < 0)
			{
				block.breakNaturally();
			}
		});
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void upsidePhysics(BlockPhysicsEvent event)
	{
		Block block = event.getBlock();
		
		if(!CHECK_UPSIDE.contains(block.getType()))
		{
			return;
		}
		
		plugin.execute(() ->
		{
			Block relativeUp = block.getRelative(UP);
			
			Rarity blockRarity = RarityUtil.getRarity(plugin, block);
			Rarity relativeUpRarity = RarityUtil.getRarity(plugin, relativeUp);
			
			if(relativeUpRarity.compareTo(blockRarity) < 0)
			{
				block.breakNaturally();
			}
		});
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void cocoaPhysics(BlockPhysicsEvent event)
	{
		Block block = event.getBlock();
		
		if(!(block.getBlockData() instanceof Cocoa cocoa))
		{
			return;
		}
		
		plugin.execute(() ->
		{
			Block adjacentJungleLog = block.getRelative(cocoa.getFacing());
			
			Rarity blockRarity = RarityUtil.getRarity(plugin, block);
			Rarity adjacentRarity = RarityUtil.getRarity(plugin, adjacentJungleLog);
			
			if(adjacentRarity.compareTo(blockRarity) < 0)
			{
				block.breakNaturally();
			}
		});
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void chorusFlowerPhysics(BlockPhysicsEvent event)
	{
		Block block = event.getBlock();
		
		if(block.getType() != Material.CHORUS_FLOWER)
		{
			return;
		}
		
		plugin.execute(() ->
		{
			Rarity blockRarity = RarityUtil.getRarity(plugin, block);
			
			for(BlockFace face : new BlockFace[] { UP, EAST, WEST, NORTH, SOUTH, DOWN })
			{
				Block relative = block.getRelative(face);
				
				if(relative.getType() == Material.CHORUS_PLANT)
				{
					Rarity adjacentChorusPlantRarity = RarityUtil.getRarity(plugin, relative);
					
					if(adjacentChorusPlantRarity.compareTo(blockRarity) >= 0)
					{
						return;
					}
				}
			}
			
			Block relativeDown = block.getRelative(DOWN);
			Rarity relativeDownRarity = RarityUtil.getRarity(plugin, relativeDown);
			
			if(relativeDown.getType() != Material.END_STONE || relativeDownRarity.compareTo(blockRarity) < 0)
			{
				block.breakNaturally();
			}
		});
	}
	
	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void checkDownsideRarity(BlockPlaceEvent event)
	{
		ItemStack item = event.getItemInHand();
		
		if(!CHECK_DOWNSIDE.contains(item.getType()))
		{
			return;
		}
		
		Block block = event.getBlock();
		Block downsideBlock = block.getRelative(BlockFace.DOWN);
		
		Rarity itemRarity = RarityUtil.getRarity(item);
		Rarity downsideBlockRarity = RarityUtil.getRarity(plugin, downsideBlock);
		
		Player player = event.getPlayer();
		PluginPlayer pluginPlayer = (PluginPlayer) plugin.getPlayerManager().get(player);
		
		if(itemRarity.compareTo(downsideBlockRarity) > 0)
		{
			pluginPlayer.sendMessage
			(
				itemRarity.getPlacementIncompatibilityMessage(),
				Component.translatable(item.getType().translationKey()),
				Component.translatable(downsideBlock.getType().translationKey())
			);
			SoundUtil.playPlong(player);
			event.setCancelled(true);
		}
	}
	
	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void checkUpsideRarity(BlockPlaceEvent event)
	{
		ItemStack item = event.getItemInHand();
		
		if(!CHECK_UPSIDE.contains(item.getType()))
		{
			return;
		}
		
		Block block = event.getBlock();
		Block upsideBlock = block.getRelative(BlockFace.UP);
		
		Rarity itemRarity = RarityUtil.getRarity(item);
		Rarity upsideBlockRarity = RarityUtil.getRarity(plugin, upsideBlock);
		
		Player player = event.getPlayer();
		PluginPlayer pluginPlayer = (PluginPlayer) plugin.getPlayerManager().get(player);
		
		if(itemRarity.compareTo(upsideBlockRarity) > 0)
		{
			pluginPlayer.sendMessage
			(
				itemRarity.getPlacementIncompatibilityMessage(),
				Component.translatable(item.getType().translationKey()),
				Component.translatable(upsideBlock.getType().translationKey())
			);
			SoundUtil.playPlong(player);
			event.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onPlayerScrape(BlockPlaceEvent event)
	{
		Player player = event.getPlayer();
		ItemStack tool = player.getEquipment().getItem(event.getHand());
		
		if(!ItemStackUtil.isTool(tool)
				|| !(tool.getItemMeta() instanceof Damageable damageable))
		{
			return;
		}
		
		Block block = event.getBlock();
		BlockData blockData = plugin.getBlockDataFiler().loadBlockDataOrDefault(block);
		int toolRemainingDurability = ItemStackUtil.getRemainingDurability(tool);
		
		if(blockData.isScraped())
		{
			blockData.resetScrapeHealth();
		}
		
		while(!blockData.isScraped() && !tool.isEmpty())
		{
			byte damage = MathUtil.clampToByte(Math.max(0, Math.min(toolRemainingDurability, blockData.getScrapeHealth())));
			
			if(damage == 0)
			{
				break;
			}
			
			player.damageItemStack(event.getHand(), damage);
			blockData.scrape(damage);
		}
		
		if(!blockData.isScraped())
		{
			event.setCancelled(true);
		}
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onBlockPlace(BlockPlaceEvent event)
	{
		Player player = event.getPlayer();
		ItemStack item = player.getEquipment().getItem(event.getHand());
		
		if(ItemStackUtil.isTool(item) && item.getItemMeta() instanceof Damageable)
		{
			return;
		}
		
		Block block = event.getBlock();
		Rarity rarity = RarityUtil.getRarity(event.getItemInHand());
		StabilityFormula stabilityFormula = StabilityFormula.PLACEMENT_STABILITY_FORMULA_BY_MATERIAL.getOrDefault(block.getType(), StabilityFormula.PLACED);
		
		plugin.getBlockDataFiler().setBlockData(block, BlockData.builder()
				.rarity(rarity)
				.origin(BlockData.Origin.PLACED)
				.stabilityFormula(stabilityFormula)
				.build());
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onMultiBlockPlace(BlockMultiPlaceEvent event)
	{
		Rarity rarity = RarityUtil.getRarity(event.getItemInHand());
		
		for(BlockState blockState : event.getReplacedBlockStates())
		{
			Block block = blockState.getBlock();
			plugin.getBlockDataFiler().setBlockData(block, BlockData.builder()
					.rarity(rarity)
					.origin(BlockData.Origin.PLACED)
					.build());
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onBlockBreak(BlockBreakEvent event)
	{
		Player player = event.getPlayer();
		Block block = event.getBlock();
		
		if(player.getGameMode() != GameMode.SURVIVAL)
		{
			plugin.execute(() -> plugin.getBlockDataFiler().removeBlockData(block));
			return;
		}
		
		BlockData blockData = plugin.getBlockDataFiler().loadBlockDataOrDefault(block);
		
		ItemStack tool = player.getInventory().getItemInMainHand();
		int damage = (int) Math.ceil(blockData.damage(tool, block));
		
		event.setCancelled(true);
		
		damage *= Tag.ITEMS_SWORDS.isTagged(tool.getType()) ? 2 : 1;
		player.damageItemStack(EquipmentSlot.HAND, damage);
		
		plugin.getOreMinedAlertListener().broadcastAlertIfNeeded(event);
		
		if(blockData.isDead())
		{
			block.breakNaturally(blockData.getFakeTool(tool), player, false, true);
		}
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onBlockDropItem(BlockDropItemEvent event)
	{
		Block block = event.getBlock();
		Rarity rarity = RarityUtil.getRarity(plugin, block);
		plugin.execute(() -> plugin.getBlockDataFiler().removeBlockData(block));
		
		for(Item drop : event.getItems())
		{
			drop.setItemStack(RarityUtil.setRarity(drop.getItemStack(), rarity));
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onBlockDestroy(BlockDestroyEvent event)
	{
		event.setCancelled(true);
		event.getBlock().breakNaturally();
	}
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onBlockBreakNaturally(BlockBreakNaturallyEvent event)
	{
		Block block = event.getBlock();
		Rarity rarity = RarityUtil.getRarity(plugin, block);
		plugin.execute(() -> plugin.getBlockDataFiler().removeBlockData(block));
		
		BlockData blockData = plugin.getBlockDataFiler().loadBlockData(block);
		
		StabilityFormula stabilityFormula = blockData == null ? StabilityFormula.STABLE : blockData.getStabilityFormula();
		int stabilityLevel = Optional.ofNullable(event.getTool())
				.map(ItemStack::getItemMeta)
				.map(meta -> meta.getEnchantLevel(EnchantType.STABILITY.getEnchantment()))
				.orElse(0);
		
		float stabilityChance = stabilityFormula.calculateChance(stabilityLevel, rarity, Rarity.COMMON);
		boolean keepRarity = rarity == Rarity.MIN_RARITY || Math.random() < stabilityChance;
		
		if(keepRarity)
		{
			for(ItemStack drop : event.getItems())
			{
				RarityUtil.setRarity(drop, rarity);
			}
		}
		else
		{
			for(ItemStack drop : event.getItems())
			{
				drop.setAmount(drop.getAmount() * 10);
				RarityUtil.setRarity(drop, rarity.lower());
			}
			
			Particle.WHITE_SMOKE.builder()
					.location(block.getLocation().toCenterLocation())
					.count(30)
					.allPlayers()
					.offset(0.2D, 0.2D, 0.2D)
					.extra(0.0D)
					.spawn();
		}
		
		event.setExpToDrop(event.getExpToDrop() * rarity.getScalarMultiplier(10));
	}
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onBlockExplode(BlockExplodeEvent event)
	{
		Block block = event.getBlock();
		Rarity rarity = RarityUtil.getRarity(plugin, block);
		event.blockList().clear();
		createFakeExplosion(rarity, block.getLocation().toCenterLocation(), event.getRadius(), event.getRadius());
	}
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onTNTPrime(TNTPrimeEvent event)
	{
		event.setCancelled(true);
		primeTNT(event.getBlock(), event.getCause());
	}
	
	private void primeTNT(Block block, TNTPrimeEvent.PrimeCause cause)
	{
		Rarity rarity = RarityUtil.getRarity(plugin, block);
		block.setType(Material.AIR);
		block.getWorld().spawnEntity
		(
			block.getLocation().toCenterLocation(),
			EntityType.TNT,
			CreatureSpawnEvent.SpawnReason.CUSTOM,
			entity -> setupTNTPrimed((TNTPrimed) entity, rarity, cause)
		);
	}
	
	private void setupTNTPrimed(TNTPrimed tntPrimed, Rarity rarity, TNTPrimeEvent.PrimeCause cause)
	{
		RarityUtil.setRarity(tntPrimed, rarity);
		tntPrimed.setFuseTicks(cause == TNTPrimeEvent.PrimeCause.EXPLOSION
				? random.nextInt(10, 31)
				: 80);
	}
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onEntityExplode(EntityExplodeEvent event)
	{
		Entity entity = event.getEntity();
		Rarity rarity = RarityUtil.getRarity(entity);
		event.blockList().clear();
		createFakeExplosion(rarity, entity.getLocation(), event.getRadius(), event.getRadius() * 2.0F);
	}
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onBlockBreakBlock(BlockBreakBlockEvent event)
	{
		Block block = event.getBlock();
		Rarity rarity = RarityUtil.getRarity(plugin, block);
		plugin.getBlockDataFiler().removeBlockData(block);
		
		for(ItemStack itemStack : event.getDrops())
		{
			RarityUtil.setRarity(itemStack, rarity);
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onBlockBreakBlock(LeavesDecayEvent event)
	{
		event.setCancelled(true);
		event.getBlock().breakNaturally();
	}
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onPlacingFlowerPot(PlayerFlowerPotManipulateEvent event)
	{
		if(!event.isPlacing())
		{
			return;
		}
		
		Rarity blockRarity = RarityUtil.getRarity(plugin, event.getFlowerpot());
		Rarity itemRarity = RarityUtil.getRarity(event.getItem());
		
		if(blockRarity != itemRarity)
		{
			event.setCancelled(true);
			
			Player player = event.getPlayer();
			PluginPlayer pluginPlayer = (PluginPlayer) plugin.getPlayerManager().get(player);
			pluginPlayer.sendMessage(PluginMessage.INCOMPATIBLE_RARITY);
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onTakingFlowerPot(PlayerFlowerPotManipulateEvent event)
	{
		if(event.isPlacing())
		{
			return;
		}
		
		event.setCancelled(true);
		
		Player player = event.getPlayer();
		
		if(!player.getInventory().getItemInMainHand().isEmpty())
		{
			return;
		}
		
		Rarity blockRarity = RarityUtil.getRarity(plugin, event.getFlowerpot());
		
		ItemStack item = event.getItem();
		RarityUtil.setRarity(item, blockRarity);
		
		player.getInventory().setItemInMainHand(item);
	}
	
	public void createFakeExplosion(final Rarity rarity, final Location location, final float radius, final float power)
	{
		final int r = (int) radius;
		final int rays = 200;
		
		final Map<Block, Float> damagedBlocks = new HashMap<>();
		
		for(int i = 0; i < rays; i++)
		{
			final Vector direction = getRandomDirection();
			
			for(int j = 0; j <= r; j++)
			{
				final Location rayLocation = location.clone().add(direction.clone().multiply(j));
				final Block block = rayLocation.getBlock();
				
				if(block.getType() == Material.TNT)
				{
					primeTNT(block, TNTPrimeEvent.PrimeCause.EXPLOSION);
					return;
				}
				
				if(block.isEmpty())
				{
					continue;
				}
				
				final float rayIntensity = (float) (power - power * location.distance(rayLocation) / radius);
				
				final BlockData blockData = plugin.getBlockDataFiler().loadBlockDataOrDefault(block);
				final float damage = calculateExplosionDamage(rarity, block, rayIntensity);
				
				final float lastDamage = damagedBlocks.getOrDefault(block, 0.0F);
				final float greaterDamage = damagedBlocks.compute(block, (k, v) ->
				{
					if(v == null)
					{
						return damage;
					}
					
					if(damage > v)
					{
						return damage;
					}
					
					return v;
				});
				
				final float computedDamage = greaterDamage - lastDamage;
				
				blockData.damageExplosion(computedDamage, block);
				
				if(blockData.getHealth() > 0.0F)
				{
					break;
				}
				else
				{
					block.breakNaturally();
				}
			}
		}
	}
	
	private Vector getRandomDirection()
	{
		double x = Math.random() * 2.0 - 1.0;
		double y = Math.random() * 2.0 - 1.0;
		double z = Math.random() * 2.0 - 1.0;
		
		Vector direction = new Vector(x, y, z);
		direction.normalize();
		
		return direction;
	}
	
	private float calculateExplosionDamage(Rarity rarity, Block block, float rayIntensity)
	{
		float blastResistance = block.getType().getBlastResistance();
		float damage = rayIntensity * rarity.getExplosionMultiplierDamage() / (blastResistance / 10.0F + 1.0F);
//		Bukkit.broadcastMessage("rayIntensity: " + rayIntensity + " damage: " + damage + " br: " + blastResistance);
		return Math.max(damage, 0.0F);
	}
	
//	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
//	public void onBlockPlace(BlockPlaceEvent event)
//	{
//		Rarity rarity = RarityUtil.getRarity(event.getItemInHand());
//		plugin.getBlockDataFiler().setBlockData(event.getBlock(), new BlockData(rarity));
//	}
//
//	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
//	public void on(StructureGrowEvent event)
//	{
//		changeMultipleHardnessToNewState(event, event.getBlocks());
//	}
//
//	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
//	public void on(LeavesDecayEvent event)
//	{
//		changeHardnessToNewState(event, event.getBlock().getState(), Material.AIR.createBlockData());
//	}
//
//	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
//	public void on(BlockSpreadEvent event)
//	{
//		changeHardnessToNewState(event, event.getNewState());
//	}
//
//	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
//	public void on(BlockFormEvent event)
//	{
//		changeHardnessToNewState(event, event.getNewState());
//	}
//
//	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
//	public void onLiquidFlow(BlockFromToEvent event)
//	{
//		Block block = event.getBlock();
//
//		if(block.getType() != Material.LAVA
//				&& block.getType() != Material.WATER
//				&& !(block.getBlockData() instanceof Waterlogged waterlogged
//				&& waterlogged.isWaterlogged()))
//		{
//			return;
//		}
//
//		changeHardnessToNewState(event, event.getToBlock().getState(), event.getBlock().getBlockData());
//	}
//
//	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
//	public void onDragonEggTeleport(BlockFromToEvent event)
//	{
//		Block block = event.getBlock();
//
//		if(block.getType() != Material.DRAGON_EGG)
//		{
//			return;
//		}
//
//		Block toBlock = event.getToBlock();
//
//		BlockState fromState = block.getState();
//		BlockState toState = toBlock.getState();
//
//		fromState.setType(toBlock.getType());
//		fromState.setBlockData(toBlock.getBlockData());
//
//		toState.setType(block.getType());
//		toState.setBlockData(block.getBlockData());
//
//		changeMultipleHardnessToNewState(event, List.of(fromState, toState));
//	}
//
//	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
//	public void on(FluidLevelChangeEvent event)
//	{
//		changeHardnessToNewState(event, event.getBlock().getState(), event.getNewData());
//	}
//
//	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
//	public void on(PlayerBucketEmptyEvent event)
//	{
//		final org.bukkit.block.data.BlockData newData;
//
//		if(event.getBlock().getBlockData() instanceof Waterlogged waterlogged)
//		{
//			newData = waterlogged.clone();
//			((Waterlogged) newData).setWaterlogged(true);
//		}
//		else
//		{
//			newData = Material.WATER.createBlockData();
//		}
//
//		changeHardnessToNewState(event, event.getBlock().getState(), newData);
//
//		if(plugin.getPlayerManager().get(event.getPlayer()) instanceof TownPlayer townPlayer
//				&& event.isCancelled())
//		{
//			townPlayer.sendMessage(PluginMessage.THIS_BLOCK_WILL_EXCEED_HARDNESS_LIMIT);
//		}
//	}
//
//	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
//	public void on(PlayerBucketFillEvent event)
//	{
//		changeHardnessToNewState(event, event.getBlock().getState(), Material.AIR.createBlockData());
//	}
//
//	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
//	public void on(StructureConstructEvent event)
//	{
//		Structure structure = event.getStructure();
//
//		for(TownBlock townBlock : structure.getTownBlockSet())
//		{
//			Block block = townBlock.getBlock();
//
//			for(block = block.getWorld().getBlockAt(block.getX(), 0, block.getZ());
//					block.getY() < block.getWorld().getMaxHeight(); block = block.getRelative(UP))
//			{
//				BlockData blockData = plugin.getBlockDataFiler().loadBlockData(block);
//
//				if(blockData == null || !blockData.hasHardness())
//				{
//					continue;
//				}
//
//				townBlock.getTown().decreaseHardness(getHardnessPlusWaterlogged(block));
//				blockData.hasHardness(false);
//			}
//		}
//	}
//
//	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
//	public void on(BlockDestroyEvent event)
//	{
//		changeHardnessToNewState(event, event.getBlock().getState(), event.getNewState());
//	}
//
//	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
//	public void on(BlockGrowEvent event)
//	{
//		changeHardnessToNewState(event, event.getNewState());
//	}
//
//	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
//	public void on(MoistureChangeEvent event)
//	{
//		changeHardnessToNewState(event, event.getNewState());
//	}
//
//	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
//	public void on(BlockFadeEvent event)
//	{
//		changeHardnessToNewState(event, event.getNewState());
//	}
//
//	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
//	public void on(BlockMultiPlaceEvent event)
//	{
//		changeHardnessToNewState(event,
//				event.getReplacedBlockStates().get(1),
//				event.getBlock().getBlockData());
//	}
//
//	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
//	public void on(BlockPistonExtendEvent event)
//	{
//		final Block pistonBase = event.getBlock();
//
//		final List<Block> blocks = event.getBlocks();
//		final List<BlockState> newBlockStates = new ArrayList<>();
//
//		final Block pistonHead = pistonBase.getRelative(event.getDirection());
//
//		if(blocks.isEmpty())
//		{
//			changeHardnessToNewState(event,
//					pistonBase.getRelative(event.getDirection()).getState(),
//					Material.PISTON_HEAD.createBlockData());
//
//			return;
//		}
//
//		for(final Block block : event.getBlocks())
//		{
//			final Block previous = block.getRelative(event.getDirection().getOppositeFace());
//			final Block next = block.getRelative(event.getDirection());
//			final BlockState newBlockState = block.getState();
//
//			if(pistonBase.equals(previous))
//			{
//				newBlockState.setType(Material.PISTON_HEAD);
//				newBlockState.setBlockData(Material.PISTON_HEAD.createBlockData());
//			}
//			else if(blocks.contains(previous))
//			{
//				newBlockState.setType(previous.getType());
//				newBlockState.setBlockData(previous.getBlockData());
//			}
//			else
//			{
//				newBlockState.setType(Material.AIR);
//				newBlockState.setBlockData(Material.AIR.createBlockData());
//			}
//
//			newBlockStates.add(newBlockState);
//
//			if(!blocks.contains(next) && block.getPistonMoveReaction() != PistonMoveReaction.BREAK)
//			{
//				final BlockState nextNewBlockState = next.getState();
//
//				nextNewBlockState.setType(block.getType());
//				nextNewBlockState.setBlockData(block.getBlockData());
//
//				newBlockStates.add(nextNewBlockState);
//			}
//		}
//
//		changeMultipleHardnessToNewState(event, newBlockStates);
//	}
//
//	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
//	public void on(BlockPistonRetractEvent event)
//	{
//		final Block pistonBase = event.getBlock();
//		final Block pistonHead = pistonBase.getRelative(event.getDirection());
//		final List<Block> blocks = event.getBlocks();
//		final List<BlockState> newBlockStates = new ArrayList<>();
//
//		if(blocks.isEmpty())
//		{
//			BlockState oldState = pistonHead.getState();
//
//			oldState.setType(Material.PISTON_HEAD);
//			oldState.setBlockData(Material.PISTON_HEAD.createBlockData());
//
//			changeHardnessToNewState(event,
//					oldState,
//					Material.AIR.createBlockData());
//
//			return;
//		}
//
//		for(final Block block : event.getBlocks())
//		{
//			final Block nearest = block.getRelative(event.getDirection().getOppositeFace());
//			final Block furthest = block.getRelative(event.getDirection());
//			final BlockState newBlockState = block.getState();
//
//			if(blocks.contains(furthest))
//			{
//				newBlockState.setType(furthest.getType());
//				newBlockState.setBlockData(furthest.getBlockData());
//			}
//			else
//			{
//				newBlockState.setType(Material.AIR);
//				newBlockState.setBlockData(Material.AIR.createBlockData());
//			}
//
//			newBlockStates.add(newBlockState);
//
//			if(!blocks.contains(nearest))
//			{
//				final BlockState nearestNewBlockState = nearest.getState();
//
//				nearestNewBlockState.setType(block.getType());
//				nearestNewBlockState.setBlockData(block.getBlockData());
//
//				newBlockStates.add(nearestNewBlockState);
//			}
//		}
//
//		changeMultipleHardnessToNewState(event, newBlockStates);
//	}
//
//	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
//	public void onDispenseBucket(BlockDispenseEvent event)
//	{
//		Block block = event.getBlock();
//		Dispenser dispenser = (Dispenser) block.getBlockData();
//
//		ItemStack item = event.getItem();
//
//		BlockFace facing = dispenser.getFacing();
//		Block relative = block.getRelative(facing);
//
//		if(item.getType() == Material.BUCKET && canFillBucket(relative))
//		{
//			changeHardnessToNewState(event, relative.getState(), getBlockDataUnfilled(relative.getBlockData()));
//		}
//	}
//
//	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
//	public void onDispenseLava(BlockDispenseEvent event)
//	{
//		Block block = event.getBlock();
//		ItemStack item = event.getItem();
//
//		Dispenser dispenser = (Dispenser) block.getBlockData();
//
//		BlockFace facing = dispenser.getFacing();
//		Block relative = block.getRelative(facing);
//
//		if(item.getType() == Material.LAVA_BUCKET)
//		{
//			if(relative.getType().isSolid())
//			{
//				return;
//			}
//
//			BlockState airState = newState(relative, Material.AIR);
//
//			BlockState oldState = isFilled(relative) ? relative.getState() : airState;
//
//			changeHardnessToNewState(event, oldState, Material.LAVA.createBlockData());
//		}
//	}
//
//	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
//	public void onDispenseWater(BlockDispenseEvent event)
//	{
//		Block block = event.getBlock();
//		ItemStack item = event.getItem();
//
//		Dispenser dispenser = (Dispenser) block.getBlockData();
//
//		BlockFace facing = dispenser.getFacing();
//		Block relative = block.getRelative(facing);
//
//		if(canBeFilledByWater(relative) && isWaterBucket(item.getType()))
//		{
//			BlockState airState = newState(relative, Material.AIR);
//
//			BlockState oldState = isFilled(relative) || relative.getBlockData() instanceof Waterlogged
//					? relative.getState() : airState;
//
//			var newData = getDataFilledWithWater(relative.getBlockData());
//
//			changeHardnessToNewState(event, oldState, newData);
//		}
//	}
//
//	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
//	public void onDispensePowderSnow(BlockDispenseEvent event)
//	{
//		Block block = event.getBlock();
//		ItemStack item = event.getItem();
//
//		Dispenser dispenser = (Dispenser) block.getBlockData();
//
//		BlockFace facing = dispenser.getFacing();
//		Block relative = block.getRelative(facing);
//
//		if(item.getType() == Material.POWDER_SNOW_BUCKET && relative.getType() == Material.AIR)
//		{
//			changeHardnessToNewState(event, relative.getState(), Material.POWDER_SNOW.createBlockData());
//		}
//	}
//
//	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
//	public void on(EntityExplodeEvent event)
//	{
//		List<BlockState> newBlockStates = new ArrayList<>();
//
//		for(Block block : event.blockList())
//		{
//			newBlockStates.add(newState(block, Material.AIR));
//		}
//
//		changeMultipleHardnessToNewState(event, newBlockStates);
//	}
//
//	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
//	public void on(BlockExplodeEvent event)
//	{
//		List<BlockState> newBlockStates = new ArrayList<>();
//
//		for(Block block : event.blockList())
//		{
//			newBlockStates.add(newState(block, Material.AIR));
//		}
//
//		changeMultipleHardnessToNewState(event, newBlockStates);
//	}
//
//	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
//	public void on(EntityChangeBlockEvent event)
//	{
//		changeHardnessToNewState(event, event.getBlock().getState(), event.getBlockData());
//	}
//
//	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
//	public void on(SpongeAbsorbEvent event)
//	{
//		changeMultipleHardnessToNewState(event, event.getBlocks());
//	}
//
//	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
//	public void on(BlockBurnEvent event)
//	{
//		changeHardnessToNewState(event, newState(event.getBlock(), Material.AIR));
//	}
//
//	private void changeHardnessToNewState(Cancellable cancellable, BlockState newState)
//	{
//		changeHardnessToNewState(cancellable, newState.getBlock().getState(), newState.getBlockData());
//	}
//
//	private void changeHardnessToNewState(Cancellable cancellable,
//			BlockState oldState,
//			org.bukkit.block.data.BlockData newData)
//	{
//		plugin.getLogger().finer("event=" + cancellable.getClass().getSimpleName());
//		plugin.getLogger().finer("pos=" + oldState.getLocation().toVector().toBlockVector());
//		plugin.getLogger().finer("oldState=" + oldState.getBlockData());
//		plugin.getLogger().finer("newData=" + newData);
//
//		final Block block = oldState.getBlock();
//		final TownBlock townBlock = Town.getStaticTownBlock(block);
//
//		if(townBlock == null)
//		{
//			return;
//		}
//
//		final BlockData blockData = plugin.getBlockDataFiler().loadBlockDataOrDefault(block);
//
//
//		final Town town = townBlock.getTown();
//		double hardness = town.getHardness();
//		final double oldHardness = hardness;
//
//		if(blockData.hasHardness())
//		{
//			hardness -= getHardnessPlusWaterlogged(oldState);
//		}
//
//		hardness += getHardnessPlusWaterlogged(newData);
//
//		plugin.getLogger().finer(String.format("oldHardness=%.2f", oldHardness));
//		plugin.getLogger().finer(String.format("newHardness=%.2f", hardness));
//
//		try
//		{
//			townBlock.getTown().setHardness(hardness);
//			blockData.hasHardness(true);
//		}
//		catch(TownHardnessLimitException e)
//		{
//			cancellable.setCancelled(true);
//		}
//	}
//
//	private void changeMultipleHardnessToNewState(Cancellable cancellable, List<BlockState> newBlockStates)
//	{
//		Map<Town, Double> townMap = new HashMap<>();
//
//		for(BlockState blockState : newBlockStates)
//		{
//			TownBlock townBlock = Town.getStaticTownBlock(blockState.getBlock());
//
//			if(townBlock == null)
//			{
//				continue;
//			}
//
//			Town town = townBlock.getTown();
//
//			townMap.putIfAbsent(town, town.getHardness());
//			townMap.computeIfPresent(town, (townKey, hardness) -> hardness - getHardnessPlusWaterlogged(blockState.getBlock()));
//			townMap.computeIfPresent(town, (townKey, hardness) -> hardness + getHardnessPlusWaterlogged(blockState));
//		}
//
//		if(townMap.isEmpty())
//		{
//			return;
//		}
//
//		if(townMap.size() != 1)
//		{
//			cancellable.setCancelled(true);
//			return;
//		}
//
//		Map.Entry<Town, Double> entry = townMap.entrySet().iterator().next();
//		Town town = entry.getKey();
//		double hardness = entry.getValue();
//
//		plugin.getLogger().finer("multiple-blocks-event=" + cancellable.getClass().getSimpleName());
//		plugin.getLogger().finer("size=" + newBlockStates.size());
//		plugin.getLogger().finer(String.format("oldHardness=%.2f", town.getHardness()));
//		plugin.getLogger().finer(String.format("newHardness=%.2f", hardness));
//
//		try
//		{
//			town.setHardness(hardness);
//
//			for(BlockState blockState : newBlockStates)
//			{
//				plugin.getBlockDataFiler().loadBlockDataOrDefault(blockState.getBlock()).hasHardness(true);
//			}
//		}
//		catch(TownHardnessLimitException e)
//		{
//			cancellable.setCancelled(true);
//		}
//	}
}
