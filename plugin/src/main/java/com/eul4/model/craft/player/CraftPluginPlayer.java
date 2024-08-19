package com.eul4.model.craft.player;

import com.eul4.Main;
import com.eul4.command.TownCommand;
import com.eul4.common.model.player.craft.CraftCommonPlayer;
import com.eul4.i18n.PluginMessage;
import com.eul4.model.player.PluginPlayer;
import com.eul4.model.playerdata.PluginPlayerData;
import com.eul4.model.playerdata.TownPlayerData;
import com.eul4.model.playerdata.TutorialTownPlayerData;
import com.eul4.model.playerdata.VanillaPlayerData;
import com.eul4.model.town.Town;
import com.eul4.wrapper.Tag;
import lombok.Getter;
import lombok.Setter;
import net.kyori.adventure.text.format.Style;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffects;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.util.Optional;
import java.util.TreeSet;

@Getter
@Setter
public abstract sealed class CraftPluginPlayer extends CraftCommonPlayer implements PluginPlayer
	permits CraftSpiritualPlayer, CraftPhysicalPlayer
{
	protected final Main plugin;
	
	protected TownPlayerData townPlayerData;
	protected TutorialTownPlayerData tutorialTownPlayerData;
	protected VanillaPlayerData vanillaPlayerData;
	protected PluginPlayerData pluginPlayerData;
	
	private transient int lastAttackCooldownTick;
	
	protected CraftPluginPlayer(Player player, Main plugin)
	{
		super(player, plugin);
		this.plugin = plugin;
		this.townPlayerData = new TownPlayerData();
		this.tutorialTownPlayerData = new TutorialTownPlayerData();
		this.vanillaPlayerData = new VanillaPlayerData(plugin);
		this.pluginPlayerData = new PluginPlayerData(getBestTag());
	}
	
	protected CraftPluginPlayer(Player player, PluginPlayer pluginPlayer)
	{
		super(player, pluginPlayer);
		this.plugin = pluginPlayer.getPlugin();
		this.townPlayerData = pluginPlayer.getTownPlayerData();
		this.tutorialTownPlayerData = pluginPlayer.getTutorialTownPlayerData();
		this.vanillaPlayerData = pluginPlayer.getVanillaPlayerData();
		this.pluginPlayerData = pluginPlayer.getPluginPlayerData();
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
	
	@Override
	public final Town getTown()
	{
		return plugin.getTownManager().getTown(player.getUniqueId());
	}
	
	@Override
	public final Optional<Town> findTown()
	{
		return Optional.ofNullable(getTown());
	}
	
	@Override
	public final boolean hasTown()
	{
		return getTown() != null;
	}
	
	@Override
	public void onTownCreate()
	{
	
	}
	
	@Override
	public Tag getTag()
	{
		return pluginPlayerData.getTag();
	}
	
	@Override
	public Tag getFreshTag()
	{
		refreshTag();
		return pluginPlayerData.getTag();
	}
	
	@Override
	public TreeSet<Tag> getTags()
	{
		TreeSet<Tag> tags = new TreeSet<>();
		
		for(Tag tag : Tag.values())
		{
			if(tag.hasTag(this))
			{
				tags.add(tag);
			}
		}
		
		return tags;
	}
	
	@Override
	public boolean hasTag()
	{
		return getFreshTag() != null;
	}
	
	@Override
	public Tag getBestTag()
	{
		TreeSet<Tag> tags = getTags();
		return tags.isEmpty() ? null : tags.first();
	}
	
	@Override
	public void refreshTag()
	{
		setTag(isValidTag() ? getTag() : getBestTag());
	}
	
	@Override
	public boolean isValidTag()
	{
		Tag tag = pluginPlayerData.getTag();
		return tag == null || tag.hasTag(this);
	}
	
	@Override
	public void setTag(Tag tag)
	{
		pluginPlayerData.setTag(tag);
		player.displayName(player.displayName().style(tag == null ? Style.empty() : tag.getStyle()));
	}
	
	@Override
	public boolean isTagHidden()
	{
		return pluginPlayerData.isTagHidden();
	}
	
	@Override
	public boolean isTagShown()
	{
		return !isTagHidden();
	}
	
	@Override
	public void hideTag()
	{
		pluginPlayerData.setTagHidden(true);
	}
	
	@Override
	public void showTag()
	{
		pluginPlayerData.setTagHidden(false);
	}
}
