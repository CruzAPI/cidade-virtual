package com.eul4.hotbar;

import com.eul4.i18n.PluginMessage;
import com.eul4.model.player.spiritual.RaidSpectator;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

@Getter
public class RaidSpectatorHotbar
{
	private final RaidSpectator raidSpectator;
	
	private final ItemStack defend;
	private final ItemStack vanilla;
	
	public RaidSpectatorHotbar(RaidSpectator raidSpectator)
	{
		this.raidSpectator = raidSpectator;
		
		ItemMeta meta;
		
		defend = new ItemStack(Material.SHIELD);
		meta = defend.getItemMeta();
		meta.displayName(PluginMessage.RAID_SPECTATOR_HOTBAR_DEFEND.translate(raidSpectator));
		defend.setItemMeta(meta);
		
		vanilla = new ItemStack(Material.RED_BED);
		meta = vanilla.getItemMeta();
		meta.displayName(PluginMessage.RAID_SPECTATOR_HOTBAR_VANILLA.translate(raidSpectator));
		vanilla.setItemMeta(meta);
	}
	
	public void reset()
	{
		Player player = raidSpectator.getPlayer();
		
		player.getInventory().clear();
		player.getInventory().setItem(0, defend);
		player.getInventory().setItem(8, vanilla);
	}
}
