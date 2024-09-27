package com.eul4.task;

import com.eul4.Main;
import com.eul4.common.model.player.CommonPlayer;
import com.eul4.enums.Rarity;
import com.eul4.model.player.PluginPlayer;
import com.eul4.model.town.Town;
import com.eul4.model.town.TownBlock;
import com.eul4.service.BlockData;
import com.eul4.util.OreVeinUtil;
import com.eul4.util.RarityUtil;
import com.eul4.world.SpawnProtectedLevel;
import com.eul4.wrapper.EnchantType;
import com.eul4.wrapper.StackedEnchantment;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.text.Component;
import org.bukkit.FluidCollisionMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.Nullable;

import java.text.DecimalFormat;
import java.util.*;

import static com.eul4.common.util.ComponentUtil.CORRECT_SYMBOL;
import static com.eul4.common.util.ComponentUtil.INCORRECT_SYMBOL;
import static net.kyori.adventure.bossbar.BossBar.Color.GREEN;
import static net.kyori.adventure.bossbar.BossBar.Color.WHITE;
import static net.kyori.adventure.text.Component.empty;
import static net.kyori.adventure.text.Component.text;

@RequiredArgsConstructor
public class RarityBossBarTask extends BukkitRunnable
{
	private final Main plugin;
	private final Map<UUID, RarityBossBar<?>> bossBars = new HashMap<>();
	
	private static final Set<EnchantType> EXCLUDED_ENCHANT_TYPES = Set.of(EnchantType.STABILITY);
	private static final StackedEnchantment ZERO_STABILITY_STACKED_ENCHANTMENT = new StackedEnchantment(EnchantType.STABILITY, 0, true);
	private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("0.##");
	
	@Override
	public void run()
	{
		for(CommonPlayer commonPlayer : plugin.getPlayerManager().getAll())
		{
			PluginPlayer pluginPlayer = (PluginPlayer) commonPlayer;
			Player player = commonPlayer.getPlayer();
			Entity entity = player.getTargetEntity(5);
			
			if(entity != null)
			{
				setAbstractBossBar(player, new EntityBossBarValue(pluginPlayer, entity));
			}
			else
			{
				boolean hasBucketInHand = player.getEquipment().getItemInMainHand().getType() == Material.BUCKET
						|| player.getEquipment().getItemInOffHand().getType() == Material.BUCKET;
				
				FluidCollisionMode fluidCollisionMode = hasBucketInHand
						? FluidCollisionMode.SOURCE_ONLY
						: FluidCollisionMode.NEVER;
				
				Block block = player.getTargetBlockExact(5, fluidCollisionMode);
				
				setAbstractBossBar(player, new BlockBossBarValue(pluginPlayer, block));
			}
		}
	}
	
	private <T> void setAbstractBossBar(Player player, AbstractBossBarValue<T> value)
	{
		if(value == null || !value.isValid())
		{
			removeBossBar(player);
			return;
		}
		
		RarityBossBar<?> rarityBossBar = bossBars.get(player.getUniqueId());
		boolean show = false;
		
		if(rarityBossBar == null)
		{
			rarityBossBar = value.newBossBar(player);
			show = true;
		}
		else if(!rarityBossBar.getObject().equals(value.getValue()))
		{
			removeBossBar(player);
			rarityBossBar = value.newBossBar(player);
			show = true;
		}
		
		rarityBossBar.update();
		
		if(show)
		{
			player.showBossBar(rarityBossBar.getBossBar());
		}
		
		bossBars.put(player.getUniqueId(), rarityBossBar);
	}
	
	private BossBar newEmptyBossBar()
	{
		return BossBar.bossBar(empty(), 0.0F, WHITE, BossBar.Overlay.PROGRESS);
	}
	
	private void removeBossBar(Player player)
	{
		Optional.ofNullable(bossBars.remove(player.getUniqueId()))
				.map(RarityBossBar::getBossBar)
				.ifPresent(player::hideBossBar);
	}
	
	private interface RarityBossBar<T>
	{
		BossBar getBossBar();
		void update();
		T getObject();
		Class<T> getType();
	}
	
	@RequiredArgsConstructor
	@Getter
	private class BlockBossBar implements RarityBossBar<Block>
	{
		private final Player player;
		private final Block block;
		private final BossBar bossBar;
		
		@Override
		public Block getObject()
		{
			return block;
		}
		
		@Override
		public Class<Block> getType()
		{
			return Block.class;
		}
		
		public void update()
		{
			OreVeinUtil.rarifyVein(plugin, block);
			BlockData blockData = plugin.getBlockDataFiler().loadBlockData(block);
			ItemStack tool = player.getInventory().getItemInMainHand();
			Rarity rarity = blockData == null ? Rarity.DEFAULT_RARITY : blockData.getRarity();
			
			StackedEnchantment stabilityStackedEnchantment = findStackedEnchantment(tool, blockData, EnchantType.STABILITY)
					.orElse(ZERO_STABILITY_STACKED_ENCHANTMENT);
			
			int stabilityLevel = stabilityStackedEnchantment.getLevel();
			
			boolean willDrop = blockData == null
					? block.getBlockData().isPreferredTool(tool)
					: blockData.willDrop(tool, block);
			
			float stabilityChange = blockData == null
					? 1.0F
					: blockData.getStabilityFormula().calculateChance(stabilityLevel, rarity, Rarity.COMMON);
			
			boolean isStableWithoutEnchantments = blockData == null
					|| blockData.getStabilityFormula().calculateChance(0, rarity, Rarity.COMMON) == 1.0F;
			
			Component name = text("Drop: ")
					.append(willDrop ? CORRECT_SYMBOL : INCORRECT_SYMBOL);
			
			if((!isStableWithoutEnchantments || stabilityChange < 1.0F) && willDrop)
			{
				float percentage = stabilityChange * 100.0F;
				
				name = name.appendSpace()
						.append(stabilityStackedEnchantment.getComponent())
						.appendSpace()
						.append(text("(" + DECIMAL_FORMAT.format(percentage) + "%)"));
			}
			
			Component enchantments = getEnchantmentsComponentButStability(tool, blockData);
			
			if(enchantments != empty())
			{
				name = name.appendSpace().append(enchantments);
			}
			
			if(blockData == null)
			{
				bossBar.name(name);
				bossBar.progress(1.0F);
				bossBar.color(GREEN);
			}
			else
			{
				bossBar.name(name);
				bossBar.progress(Math.max(0.0F, Math.min(1.0F, blockData.getHealth() / rarity.getMaxHealth())));
				bossBar.color(rarity.getBossBarColor());
			}
		}
		
		private Optional<StackedEnchantment> findStackedEnchantment(ItemStack tool, @Nullable BlockData blockData, EnchantType enchantType)
		{
			return Optional.ofNullable(getStackedEnchantments(tool, blockData).get(enchantType));
		}
		
		private Map<EnchantType, StackedEnchantment> getStackedEnchantments(ItemStack tool, @Nullable BlockData blockData)
		{
			return getStackedEnchantments(tool, blockData, Collections.emptySet());
		}
		
		private Map<EnchantType, StackedEnchantment> getStackedEnchantments(ItemStack tool, @Nullable BlockData blockData, Set<EnchantType> excludedEnchantTypes)
		{
			return blockData == null
					? BlockData.getStaticStackedEnchantments(tool, excludedEnchantTypes)
					: blockData.getStackedEnchantments(tool, excludedEnchantTypes);
		}
		
		private Component getEnchantmentsComponentButStability(ItemStack tool, @Nullable BlockData blockData)
		{
			return getEnchantmentsComponent(tool, blockData, EXCLUDED_ENCHANT_TYPES);
		}
		
		private Component getEnchantmentsComponent(ItemStack tool, @Nullable BlockData blockData)
		{
			return getEnchantmentsComponent(tool, blockData, Collections.emptySet());
		}
		
		private Component getEnchantmentsComponent(ItemStack tool, @Nullable BlockData blockData, Set<EnchantType> excludedEnchantTypes)
		{
			Collection<StackedEnchantment> stackedEnchantments = getStackedEnchantments(tool, blockData, excludedEnchantTypes).values();
			
			if(stackedEnchantments.isEmpty())
			{
				return empty();
			}
			
			Component enchantmentsComponent = Component.empty();
			
			Iterator<StackedEnchantment> iterator = stackedEnchantments.iterator();
			
			while(iterator.hasNext())
			{
				enchantmentsComponent = enchantmentsComponent.append(iterator.next().getComponent());
				
				if(iterator.hasNext())
				{
					enchantmentsComponent = enchantmentsComponent.appendSpace();
				}
			}
			
			return enchantmentsComponent;
		}
	}
	
	@RequiredArgsConstructor
	@Getter
	private class EntityBossBar implements RarityBossBar<Entity>
	{
		private final Player player;
		private final Entity entity;
		private final BossBar bossBar;
		
		@Override
		public Entity getObject()
		{
			return entity;
		}
		
		@Override
		public Class<Entity> getType()
		{
			return Entity.class;
		}
		
		public void update()
		{
			Rarity rarity = RarityUtil.getRarity(entity);
			
			float health = entity instanceof LivingEntity livingEntity
					? (float) (livingEntity.getHealth() / livingEntity.getMaxHealth())
					: 1.0F;
			
			bossBar.name(empty());
			bossBar.progress(Math.max(0.0F, Math.min(1.0F, health)));
			bossBar.color(rarity.getBossBarColor());
		}
	}
	
	@RequiredArgsConstructor
	@Getter
	private abstract class AbstractBossBarValue<T>
	{
		protected final PluginPlayer pluginPlayer;
		protected final T value;
		
		protected abstract boolean isValid();
		protected abstract RarityBossBar<T> newBossBar(Player player);
		protected abstract Class<T> getType();
	}
	
	@Getter
	private class EntityBossBarValue extends AbstractBossBarValue<Entity>
	{
		public EntityBossBarValue(PluginPlayer pluginPlayer, Entity entity)
		{
			super(pluginPlayer, entity);
		}
		
		@Override
		protected boolean isValid()
		{
			return !(value instanceof Player) && !value.isInvulnerable() && !value.isInvisible();
		}
		
		public EntityBossBar newBossBar(Player player)
		{
			return new EntityBossBar(player, value, newEmptyBossBar());
		}
		
		@Override
		protected Class<Entity> getType()
		{
			return Entity.class;
		}
	}
	
	@Getter
	private class BlockBossBarValue extends AbstractBossBarValue<Block>
	{
		public BlockBossBarValue(PluginPlayer pluginPlayer, Block block)
		{
			super(pluginPlayer, block);
		}
		
		@Override
		protected boolean isValid()
		{
			if(value == null || value.isEmpty())
			{
				return false;
			}
			
			TownBlock townBlock = Town.getStaticTownBlock(value);
			
			if(townBlock != null)
			{
				return townBlock.getTown().getOwnerUUID().equals(pluginPlayer.getUniqueId())
						&& townBlock.canBuild();
			}
			
			if(plugin.getWorldManager().get(value.getWorld()) instanceof SpawnProtectedLevel spawnProtectedLevel)
			{
				return !spawnProtectedLevel.isSpawn(value);
			}
			
			return true;
		}
		
		public BlockBossBar newBossBar(Player player)
		{
			return new BlockBossBar(player, value, newEmptyBossBar());
		}
		
		@Override
		protected Class<Block> getType()
		{
			return Block.class;
		}
	}
}
