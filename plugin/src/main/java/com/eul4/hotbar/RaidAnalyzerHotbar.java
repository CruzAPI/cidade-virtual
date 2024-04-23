package com.eul4.hotbar;

import com.eul4.model.player.RaidAnalyzer;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

@Getter
public class RaidAnalyzerHotbar
{
	private final RaidAnalyzer raidAnalyzer;
	
	private ItemStack attack;
	private ItemStack reroll;
	private ItemStack cancel;
	
	public RaidAnalyzerHotbar(RaidAnalyzer raidAnalyzer)
	{
		this.raidAnalyzer = raidAnalyzer;
		
		ItemMeta meta;
		
		attack = new ItemStack(Material.IRON_SWORD);
		meta = attack.getItemMeta();
		meta.setDisplayName("attack?");
		attack.setItemMeta(meta);
		
		reroll = new ItemStack(Material.ARROW);
		meta = reroll.getItemMeta();
		meta.setDisplayName("reroll?");
		reroll.setItemMeta(meta);
		
		cancel = new ItemStack(Material.RED_BED);
		meta = cancel.getItemMeta();
		meta.setDisplayName("cancel?");
		cancel.setItemMeta(meta);
	}
	
	public void reset()
	{
		Player player = raidAnalyzer.getPlayer();
		
		player.getInventory().clear();
		player.getInventory().setItem(0, attack);
		player.getInventory().setItem(1, reroll);
		player.getInventory().setItem(8, cancel);
	}
}
