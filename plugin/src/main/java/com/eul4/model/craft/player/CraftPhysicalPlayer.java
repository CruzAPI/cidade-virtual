package com.eul4.model.craft.player;

import com.eul4.Main;
import com.eul4.common.model.data.PlayerData;
import com.eul4.common.world.CommonWorld;
import com.eul4.model.player.PhysicalPlayer;
import com.eul4.model.player.PluginPlayer;
import com.eul4.model.player.SpiritualPlayer;
import com.eul4.type.player.PhysicalPlayerType;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Optional;

@Getter
@Setter
public non-sealed abstract class CraftPhysicalPlayer extends CraftPluginPlayer implements PhysicalPlayer
{
	protected boolean isReincarnation;
	
	protected CraftPhysicalPlayer(Player player, Main plugin)
	{
		super(player, plugin);
	}
	
	protected CraftPhysicalPlayer(Player player, PluginPlayer pluginPlayer)
	{
		super(player, pluginPlayer);
		this.isReincarnation = pluginPlayer instanceof SpiritualPlayer;
	}
	
	@Override
	public final PhysicalPlayerType getReincarnationType()
	{
		return getPlayerType();
	}
	
	@Override
	public PluginPlayer load()
	{
		if(isReincarnation)
		{
			PlayerData playerData = commonPlayerData.getPlayerData();
			CommonWorld commonWorld = plugin.getWorldManager().get(playerData.getLocation().getWorld());
			boolean teleport = commonWorld.getAcceptablePlayerTypes().contains(this.getPlayerType());
			plugin.getLogger().info("type=" + getPlayerType() + " teleport=" + teleport + " content=" + Optional.ofNullable(playerData.getContents()[0]).orElse(
					ItemStack.empty()).getType());
			playerData.apply(player, teleport);
		}
		
		reset();
		return this;
	}
}
