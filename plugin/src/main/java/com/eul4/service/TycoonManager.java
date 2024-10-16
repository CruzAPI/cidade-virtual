package com.eul4.service;

import com.eul4.Main;
import com.eul4.common.wrapper.Pitch;
import com.eul4.model.town.Town;
import com.eul4.wrapper.Tag;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.Sound;

import java.math.BigDecimal;

import static com.eul4.i18n.PluginRichMessage.BROADCAST_NEW_TYCOON_$PLAYER;

@RequiredArgsConstructor
public class TycoonManager
{
	private final Main plugin;
	
	@Getter
	private Town tycoonTown;
	
	public void updateTycoonAsync()
	{
		plugin.getServer().getScheduler().runTaskAsynchronously(plugin, this::updateTycoon);
	}
	
	private synchronized void updateTycoon()
	{
		Town tycoonTown = null;
		BigDecimal highestBalance = BigDecimal.ZERO;
		
		for(Town town : plugin.getTownManager().getTowns().values())
		{
			final BigDecimal balance = town.getCalculatedCrownBalance();
			
			if(balance.compareTo(highestBalance) >= 0)
			{
				tycoonTown = town;
				highestBalance = balance;
			}
		}
		
		if(this.tycoonTown != (this.tycoonTown = tycoonTown) && this.tycoonTown != null)
		{
			plugin.getMessageableService()
					.broadcastMessage
					(
						BROADCAST_NEW_TYCOON_$PLAYER.withArgs(this.tycoonTown.getOwner()),
						player -> player.playSound
						(
							player.getLocation(),
							Sound.ENTITY_LIGHTNING_BOLT_THUNDER,
							1.0F,
							Pitch.A1
						)
					);
			
			this.tycoonTown.findPluginPlayer().ifPresent(pluginPlayer ->
			{
				if(pluginPlayer.getBestTag() == Tag.TYCOON)
				{
					pluginPlayer.setTag(Tag.TYCOON);
				}
			});
		}
	}
}
