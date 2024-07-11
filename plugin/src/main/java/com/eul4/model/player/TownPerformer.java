package com.eul4.model.player;

import com.eul4.common.model.player.CommonPlayer;
import com.eul4.i18n.PluginMessage;
import com.eul4.model.town.Town;
import com.eul4.type.player.PhysicalPlayerType;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import net.minecraft.data.worldgen.placement.VegetationPlacements;

import java.text.MessageFormat;
import java.time.Duration;
import java.util.concurrent.Future;
import java.util.logging.Level;

import static com.eul4.common.wrapper.Pitch.getPitch;
import static org.bukkit.Sound.ENTITY_EXPERIENCE_ORB_PICKUP;

public sealed interface TownPerformer extends PluginPlayer
		permits TownPerformer.TeleportInside, TownPerformer.TeleportOutside
{
	default void performTown()
	{
		if(getPlugin().getTownManager().isCreating(getUniqueId()))
		{
			return;
		}
		
		if(hasTown())
		{
			execute();
		}
		else
		{
			Future<Town> futureTown = getPlugin().getTownManager().createNewTownAsync(getUniqueId());
			
			getPlugin().getServer().getScheduler().runTaskAsynchronously(getPlugin(), () ->
			{
				try
				{
					Component title;
					Component subtitle;
					Title.Times times;
					
					title = PluginMessage.TITLE_CREATING_TOWN.translate(this);
					subtitle = PluginMessage.SUBTITLE_CREATING_TOWN.translate(this);
					times = Title.Times.times(Duration.ZERO, Duration.ofDays(1L), Duration.ZERO);
					
					Title creatingTownTitle = Title.title(title, subtitle, times);
					
					title = PluginMessage.TITLE_TOWN_WELCOME.translate(this, getPlayer().displayName());
					subtitle = Component.empty();
					times = Title.Times.times(Duration.ofMillis(1000L),
							Duration.ofMillis(1000L),
							Duration.ofMillis(1000L));
					
					Title welcomeTitle = Title.title(title, subtitle, times);
					
					getPlayer().showTitle(creatingTownTitle);
					
					Thread.sleep(1200L);
					futureTown.get();
					
					getPlugin().getServer().getScheduler().callSyncMethod(getPlugin(), () ->
					{
						execute();
						getPlayer().resetTitle();
						getPlayer().showTitle(welcomeTitle);
						
						getPlayer().playSound(getPlayer().getLocation(), ENTITY_EXPERIENCE_ORB_PICKUP, 1.0F, getPitch(0));
						sendMessage(PluginMessage.TOWN_WELCOME, getPlayer().displayName());
						sendMessage(PluginMessage.TOWN_HINT_BUY_STRUCTURE_COMMAND);
						
						return null;
					}).get();
				}
				catch(Exception e)
				{
					getPlugin().getLogger().log(Level.SEVERE, MessageFormat.format(
							"Failed to create a new town for {0}.",
							getPlayer().getName()), e);
					getPlayer().resetTitle();
					sendMessage(PluginMessage.COMMAND_TOWN_FAILED_TO_CREATE_TOWN);
				}
			});
		}
	}
	
	void execute();
	
	non-sealed interface TeleportInside extends TownPerformer
	{
		@Override
		default void execute()
		{
			teleportToTownHall();
		}
	}
	
	non-sealed interface TeleportOutside extends TownPerformer
	{
		@Override
		default void execute()
		{
			teleportToHighestTownHall();
		}
	}
	
	interface Reincarnate extends TeleportInside
	{
		@Override
		default void execute()
		{
			CommonPlayer commonPlayer = getPlugin().getPlayerManager().register(this, PhysicalPlayerType.TOWN_PLAYER);
			
			if(commonPlayer instanceof TownPlayer)
			{
				TeleportInside.super.execute();
			}
		}
	}
}
