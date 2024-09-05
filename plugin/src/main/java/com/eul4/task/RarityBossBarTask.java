package com.eul4.task;

import com.eul4.Main;
import com.eul4.common.model.player.CommonPlayer;
import com.eul4.enums.Rarity;
import com.eul4.service.BlockData;
import com.eul4.util.RarityUtil;
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

import java.util.*;

import static com.eul4.common.util.ComponentUtil.CORRECT_SYMBOL;
import static com.eul4.common.util.ComponentUtil.INCORRECT_SYMBOL;
import static net.kyori.adventure.bossbar.BossBar.Color.WHITE;
import static net.kyori.adventure.text.Component.empty;
import static net.kyori.adventure.text.Component.text;

@RequiredArgsConstructor
public class RarityBossBarTask extends BukkitRunnable
{
	private final Main plugin;
	private final Map<UUID, RarityBossBar<?>> bossBars = new HashMap<>();
	
	@Override
	public void run()
	{
		for(CommonPlayer commonPlayer : plugin.getPlayerManager().getAll())
		{
			Player player = commonPlayer.getPlayer();
			Entity entity = player.getTargetEntity(5);
			
			if(entity != null)
			{
				if(entity instanceof Player)
				{
					removeBossBar(player);
					return;
				}
				
				setAbstractBossBar(player, new EntityBossBarValue(entity));
			}
			else
			{
				boolean hasBucketInHand = player.getEquipment().getItemInMainHand().getType() == Material.BUCKET
						|| player.getEquipment().getItemInOffHand().getType() == Material.BUCKET;
				
				FluidCollisionMode fluidCollisionMode = hasBucketInHand
						? FluidCollisionMode.SOURCE_ONLY
						: FluidCollisionMode.NEVER;
				
				Block block = player.getTargetBlockExact(5, fluidCollisionMode);
				setAbstractBossBar(player, new BlockBossBarValue(block));
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
			BlockData blockData = plugin.getBlockDataFiler().loadBlockData(block);
			ItemStack tool = player.getInventory().getItemInMainHand();
			
			boolean willDrop = blockData == null
					? block.getBlockData().isPreferredTool(tool)
					: blockData.willDrop(tool, block);
			
			Component enchantments = getEnchantmentsComponent(tool, blockData);
			
			Component name = text("Drop: ")
					.append(willDrop ? CORRECT_SYMBOL : INCORRECT_SYMBOL);
			
			if(enchantments != empty())
			{
				name = name.appendSpace().append(enchantments);
			}
			
			if(blockData == null)
			{
				bossBar.name(name);
				bossBar.progress(1.0F);
				bossBar.color(WHITE);
			}
			else
			{
				Rarity rarity = blockData.getRarity();
				
				bossBar.name(name);
				bossBar.progress(Math.max(0.0F, Math.min(1.0F, blockData.getHealth() / rarity.getMaxHealth())));
				bossBar.color(rarity.getBossBarColor());
			}
		}
		
		private Component getEnchantmentsComponent(ItemStack tool, @Nullable BlockData blockData)
		{
			Set<StackedEnchantment> stackedEnchantments = blockData == null
					? BlockData.getStaticStackedEnchantments(tool)
					: blockData.getStackedEnchantments(tool);
			
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
		protected abstract T getValue();
		protected abstract boolean isValid();
		protected abstract RarityBossBar<T> newBossBar(Player player);
		protected abstract Class<T> getType();
	}
	
	@RequiredArgsConstructor
	@Getter
	private class EntityBossBarValue extends AbstractBossBarValue<Entity>
	{
		private final Entity entity;
		
		@Override
		protected Entity getValue()
		{
			return entity;
		}
		
		@Override
		protected boolean isValid()
		{
			return true;
		}
		
		public EntityBossBar newBossBar(Player player)
		{
			return new EntityBossBar(player, entity, newEmptyBossBar());
		}
		
		@Override
		protected Class<Entity> getType()
		{
			return Entity.class;
		}
	}
	
	@RequiredArgsConstructor
	@Getter
	private class BlockBossBarValue extends AbstractBossBarValue<Block>
	{
		private final Block block;
		
		@Override
		protected Block getValue()
		{
			return block;
		}
		
		@Override
		protected boolean isValid()
		{
			return block != null && !block.isEmpty();
		}
		
		public BlockBossBar newBossBar(Player player)
		{
			return new BlockBossBar(player, block, newEmptyBossBar());
		}
		
		@Override
		protected Class<Block> getType()
		{
			return Block.class;
		}
	}
}
