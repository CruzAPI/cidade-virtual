package com.eul4.model.craft.player;

import com.eul4.Main;
import com.eul4.common.scoreboard.CommonScoreboard;
import com.eul4.i18n.PluginMessage;
import com.eul4.model.player.PluginPlayer;
import com.eul4.model.player.physical.VanillaPlayer;
import com.eul4.scoreboard.CraftInitialScoreboard;
import com.eul4.scoreboard.CraftTownScoreboard;
import com.eul4.type.PluginWorldType;
import com.eul4.type.player.PhysicalPlayerType;
import com.eul4.world.VanillaLevel;
import com.eul4.wrapper.ChannelingTask;
import lombok.Getter;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

import java.util.Optional;

import static com.eul4.wrapper.ChannelingTask.CancelReason.CHANNELING;

@Getter
public class CraftVanillaPlayer extends CraftPhysicalPlayer implements VanillaPlayer
{
	private final CommonScoreboard scoreboard = hasTown()
			? new CraftTownScoreboard(this)
			: new CraftInitialScoreboard(this);
	
	private transient ChannelingTask channelingTask;
	
	public CraftVanillaPlayer(Player player, Main plugin)
	{
		super(player, plugin);
	}
	
	public CraftVanillaPlayer(Player player, PluginPlayer pluginPlayer)
	{
		super(player, pluginPlayer);
	}
	
	@Override
	public boolean mustSavePlayerData()
	{
		return true;
	}
	
	@Override
	public PhysicalPlayerType getPlayerType()
	{
		return PhysicalPlayerType.VANILLA_PLAYER;
	}
	
	@Override
	public void reset()
	{
		super.reset();
		
		player.setGameMode(GameMode.SURVIVAL);
		
		if(!(getCommonWorld() instanceof VanillaLevel))
		{
			player.teleport(PluginWorldType.NEWBIE_WORLD.getInstance().getSpawnLocation());
		}
		
		scoreboard.registerIfNotRegistered();
	}
	
	@Override
	public PluginPlayer load()
	{
		return super.load();
	}

	@Override
	public PluginPlayer reload()
	{
		return load(); //TODO is it?
	}
	
	@Override
	public void channel(long ticks, Runnable runnable)
	{
		Optional.ofNullable(channelingTask).ifPresent(channelingTask -> channelingTask.cancel(CHANNELING));
		
		sendMessage(PluginMessage.CHANNELER_INITIALIZING_CHANNELING, ticks);
		
		channelingTask = new ChannelingTask(this, ticks, runnable);
		channelingTask.runTaskTimer(plugin, 0L, 1L);
	}
}
