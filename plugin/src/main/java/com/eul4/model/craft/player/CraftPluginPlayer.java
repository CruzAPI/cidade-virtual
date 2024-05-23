package com.eul4.model.craft.player;

import com.eul4.Main;
import com.eul4.command.TownCommand;
import com.eul4.common.model.player.craft.CraftCommonPlayer;
import com.eul4.i18n.PluginMessage;
import com.eul4.model.player.PluginPlayer;
import com.eul4.model.playerdata.TownPlayerData;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.entity.Player;

@Getter
@Setter
public abstract sealed class CraftPluginPlayer extends CraftCommonPlayer implements PluginPlayer
	permits CraftSpiritualPlayer, CraftPhysicalPlayer
{
	protected final Main plugin;
	
	protected TownPlayerData townPlayerData;
	
	protected CraftPluginPlayer(Player player, Main plugin)
	{
		super(player, plugin);
		this.plugin = plugin;
		this.townPlayerData = new TownPlayerData();
	}
	
	protected CraftPluginPlayer(Player player, PluginPlayer pluginPlayer)
	{
		super(player, pluginPlayer);
		this.plugin = pluginPlayer.getPlugin();
		this.townPlayerData = pluginPlayer.getTownPlayerData();
	}
	
	public void onStartingTownAttack()
	{
		sendMessage(PluginMessage.TOWN_ATTACK_ALERT, getTown().getCurrentAttack().getAttacker().getPlayer().displayName());
		sendMessage(PluginMessage.TYPE_TOWN_TO_DEFEND, TownCommand.COMMAND_NAME);
	}
	
	@Override
	public void onFinishingTownAttack()
	{
	
	}
	
	@Override
	public final Class<? extends PluginPlayer> getInterfaceType()
	{
		return getPlayerType().getInterfaceType();
	}
	
	@Override
	public boolean teleportToTownHall()
	{
		plugin.getLogger().info("[PluginPlayer] from=" + player.getWorld() + " to=townHall");//TODO
		return player.teleport(getTownHallSpawnLocation());
	}
	
	@Override
	public boolean teleportToHighestTownHall()
	{
		plugin.getLogger().info("[PluginPlayer] from=" + player.getWorld() + " to=highestTownHall");//TODO
		return player.teleport(getTownHallSpawnLocation().toHighestLocation());
	}
	
	private Location getTownHallSpawnLocation()
	{
		return getTown().getTownHall().getCenterTownBlock().getBlock().getLocation().add(0.5D, 1.0D, 0.5D);
	}
}
