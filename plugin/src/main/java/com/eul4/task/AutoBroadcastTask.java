package com.eul4.task;

import com.eul4.Main;
import com.eul4.common.model.player.CommonPlayer;
import com.eul4.i18n.BroadcastRichMessage;
import com.eul4.i18n.PluginMessage;
import com.eul4.model.player.PluginPlayer;
import lombok.RequiredArgsConstructor;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
public class AutoBroadcastTask extends BukkitRunnable
{
	private final Main plugin;
	
	private List<BroadcastRichMessage> broadcasts = new ArrayList<>();
	
	@Override
	public void run()
	{
		if(broadcasts.isEmpty())
		{
			broadcasts = new ArrayList<>(Arrays.asList(BroadcastRichMessage.values()));
			
			if(broadcasts.isEmpty())
			{
				return;
			}
			
			Collections.shuffle(broadcasts);
		}
		
		BroadcastRichMessage broadcast = broadcasts.remove(0);
		
		plugin.getConsole().sendMessage(PluginMessage.AUTO_$BROADCAST, broadcast);
		
		for(CommonPlayer commonPlayer : plugin.getPlayerManager().getAll())
		{
			if(commonPlayer instanceof PluginPlayer pluginPlayer
					&& !pluginPlayer.getPluginPlayerData().getMutedBroadcasts().contains(broadcast))
			{
				pluginPlayer.sendMessage(PluginMessage.AUTO_$BROADCAST, broadcast);
			}
		}
	}
}
