package com.eul4.listener;

import com.eul4.Main;
import com.eul4.common.i18n.MessageArgs;
import com.eul4.enums.Rarity;
import com.eul4.i18n.PluginRichMessage;
import com.eul4.service.BlockData;
import com.eul4.util.BroadcastUtil;
import com.eul4.util.OreVeinUtil;
import com.eul4.util.SoundUtil;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@RequiredArgsConstructor
public class OreMinedAlertListener implements Listener
{
	private static final Set<Material> ALERTING_MATERIALS;
	private static final String ALERT_PERMISSION = "ore-found.alert";
	
	static
	{
		Set<Material> allertingMaterials = new HashSet<>();
		
		allertingMaterials.addAll(Tag.DIAMOND_ORES.getValues());
		allertingMaterials.addAll(Tag.EMERALD_ORES.getValues());
		allertingMaterials.addAll(Tag.GOLD_ORES.getValues());
		allertingMaterials.add(Material.ANCIENT_DEBRIS);
		
		ALERTING_MATERIALS = Collections.unmodifiableSet(allertingMaterials);
	}
	
	private final Main plugin;
	
	private final Set<Block> ignoredBlocks = new HashSet<>();
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onBlockBreak(BlockBreakEvent event)
	{
		Player player = event.getPlayer();
		Block block = event.getBlock();
		
		if(!ALERTING_MATERIALS.contains(block.getType()) || ignoredBlocks.contains(block))
		{
			return;
		}
		
		BlockData blockData = plugin.getBlockDataFiler().loadBlockData(block);
		
		if(blockData != null && blockData.getOrigin() != BlockData.Origin.CHUNK_GENERATED)
		{
			return;
		}
		
		Rarity blockRarity = blockData == null ? Rarity.DEFAULT_RARITY : blockData.getRarity();
		
		sendAlertBroadcast(player, block.getType(), blockRarity, countVein(block));
	}
	
	private void sendAlertBroadcast(Player miner, Material minedMaterial, Rarity rarity, int count)
	{
		MessageArgs messageArgs = PluginRichMessage.ORE_FOUND_ALERT.withArgs
		(
			miner,
			rarity,
			Component.translatable(minedMaterial.translationKey()),
			count
		);
		
		BroadcastUtil.broadcast(plugin, ALERT_PERMISSION, SoundUtil::playPiano, messageArgs);
	}
	
	private int countVein(Block block)
	{
		int count = 0;
		
		for(Block veinBlock : OreVeinUtil.getVein(block))
		{
			BlockData blockData = plugin.getBlockDataFiler().loadBlockData(veinBlock);
			
			if(ignoredBlocks.add(veinBlock)
				&& (blockData == null || blockData.getOrigin() == BlockData.Origin.CHUNK_GENERATED))
			{
				count++;
			}
		}
		
		return Math.max(1, count);
	}
}
