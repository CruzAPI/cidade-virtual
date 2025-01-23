package com.eul4.hotbar;

import com.eul4.i18n.PluginMessage;
import com.eul4.model.player.spiritual.DefenderSpectator;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

@Getter
public class DefenderSpectatorHotbar
{
	private final DefenderSpectator defenderSpectator;
	
	private final ItemStack quit;
	
	public DefenderSpectatorHotbar(DefenderSpectator defenderSpectator)
	{
		this.defenderSpectator = defenderSpectator;
		
		ItemMeta meta;
		
		quit = new ItemStack(Material.RED_BED);
		meta = quit.getItemMeta();
		meta.displayName(PluginMessage.DEFENDER_SPECTATOR_HOTBAR_QUIT.translate(defenderSpectator));
		quit.setItemMeta(meta);
	}
	
	public void reset()
	{
		Player player = defenderSpectator.getPlayer();
		
		player.getInventory().clear();
		player.getInventory().setItem(8, quit);
	}
}
