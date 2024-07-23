package com.eul4.model.craft.player;

import com.eul4.Main;
import com.eul4.command.TownCommand;
import com.eul4.common.model.player.craft.CraftCommonPlayer;
import com.eul4.i18n.PluginMessage;
import com.eul4.model.player.PluginPlayer;
import com.eul4.model.playerdata.TownPlayerData;
import com.eul4.model.town.Town;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffects;
import org.bukkit.Location;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.util.Optional;

@Getter
@Setter
public abstract sealed class CraftPluginPlayer extends CraftCommonPlayer implements PluginPlayer
	permits CraftSpiritualPlayer, CraftPhysicalPlayer
{
	protected final Main plugin;
	
	protected TownPlayerData townPlayerData;
	
	private transient int lastAttackCooldownTick;
	
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
		return player.teleport(getTown().getTownHall().getSpawnLocation());
	}
	
	@Override
	public boolean teleportToHighestTownHall()
	{
		return player.teleport(getTown().getTownHall().getSpawnLocation().toHighestLocation().add(0.0D, 1.0D, 0.0D));
	}
	
	@Override
	public Optional<Town> findTown()
	{
		return Optional.ofNullable(getTown());
	}
	
	@Override
	public void resetAttackCooldown()
	{
		this.lastAttackCooldownTick = plugin.getServer().getCurrentTick();
	}
	
	@Override
	public int getAttackCooldown()
	{
		return plugin.getServer().getCurrentTick() - lastAttackCooldownTick;
	}
	
	@Override
	public boolean isCritical()
	{
		ServerPlayer serverPlayer = ((CraftPlayer) player).getHandle();
		
		return serverPlayer.fallDistance > 0.0F
				&& !serverPlayer.onGround()
				&& !serverPlayer.onClimbable()
				&& !serverPlayer.isInWater()
				&& !serverPlayer.hasEffect(MobEffects.BLINDNESS)
				&& !serverPlayer.isPassenger()
				&& !serverPlayer.isSprinting();
	}
}
