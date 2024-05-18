package com.eul4.model.craft.player;

import com.eul4.Main;
import com.eul4.common.model.player.craft.CraftCommonPlayer;
import com.eul4.model.player.PluginPlayer;
import com.eul4.model.playerdata.TownPlayerData;
import com.eul4.type.player.PluginPlayerType;
import lombok.Getter;
import lombok.Setter;
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
	
	@Override
	public void reset()
	{
		super.reset();
		
		if(!oldInstance.mustSavePlayerData() && this.mustSavePlayerData())
		{
		
		}
	}
	
	@Override
	public PluginPlayer load()
	{
		commonPlayerData.getPlayerData().apply(player);
		return this;
	}
	
	@Override
	public final Class<? extends PluginPlayer> getInterfaceType()
	{
		return getPlayerType().getInterfaceType();
	}
}
