package com.eul4.common.model.data;

import com.eul4.common.wrapper.PotionEffectCollection;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;

import java.util.Collection;

@Getter
@Builder
@AllArgsConstructor
public class PlayerData
{
	private PotionEffectCollection activePotionEffects;
	private boolean allowFlight;
	private int arrowsInBody;
	private ItemStack[] contents;
	private float exhaustion;
	private float exp;
	private float fallDistance;
	private int fireTicks;
	private boolean flying;
	private float flySpeed;
	private int foodLevel;
	private int freezeTicks;
	private GameMode gameMode;
	private double health;
	private double healthScale;
	private ItemStack itemOnCursor;
	private int level;
	private Location location;
	private int maximumAir;
	private int maximumNoDamageTicks;
	private int noActionTicks;
	private int noDamageTicks;
	private float saturation;
	private int remainingAir;
	private int unsaturatedRegenRate;
	private float walkSpeed;
	
	public PlayerData(Player player)
	{
		activePotionEffects = new PotionEffectCollection(player.getActivePotionEffects());
		allowFlight = player.getAllowFlight();
		arrowsInBody = player.getArrowsInBody();
		contents = player.getInventory().getContents();
		exhaustion = player.getExhaustion();
		exp = player.getExp();
		fallDistance = player.getFallDistance();
		fireTicks = player.getFireTicks();
		flying = player.isFlying();
		flySpeed = player.getFlySpeed();
		foodLevel = player.getFoodLevel();
		freezeTicks = player.getFreezeTicks();
		gameMode = player.getGameMode();
		health = player.getHealth();
		healthScale = player.getHealthScale();
		itemOnCursor = player.getItemOnCursor();
		level = player.getLevel();
		location = player.getLocation();
		maximumAir = player.getMaximumAir();
		maximumNoDamageTicks = player.getMaximumNoDamageTicks();
		noActionTicks = player.getNoActionTicks();
		noDamageTicks = player.getNoDamageTicks();
		remainingAir = player.getRemainingAir();
		saturation = player.getSaturation();
		unsaturatedRegenRate = player.getUnsaturatedRegenRate();
		walkSpeed = player.getWalkSpeed();
	}
	
	public void applyIgnoringTeleport(Player player)
	{
		apply(player, false);
	}
	
	public void apply(Player player)
	{
		apply(player, true);
	}
	
	public void apply(Player player, boolean teleport)
	{
		applyPotionEffects(player, activePotionEffects);
		player.setAllowFlight(allowFlight);
		player.setArrowsInBody(arrowsInBody);
		player.getInventory().setContents(contents);
		player.setExhaustion(exhaustion);
		player.setExp(exp);
		player.setFallDistance(fallDistance);
		player.setFireTicks(fireTicks);
		player.setFlying(flying);
		player.setFlySpeed(flySpeed);
		player.setFoodLevel(foodLevel);
		player.setFreezeTicks(freezeTicks);
		player.setGameMode(gameMode);
		player.setHealth(health);
		player.setHealthScale(healthScale);
		player.setItemOnCursor(itemOnCursor);
		player.setLevel(level);
		if(teleport) player.teleport(location);
		player.setMaximumAir(maximumAir);
		player.setMaximumNoDamageTicks(maximumNoDamageTicks);
		player.setNoActionTicks(noActionTicks);
		player.setNoDamageTicks(noDamageTicks);
		player.setRemainingAir(remainingAir);
		player.setSaturation(saturation);
		player.setUnsaturatedRegenRate(unsaturatedRegenRate);
		player.setWalkSpeed(walkSpeed);
	}
	
	private void applyPotionEffects(Player player, Collection<PotionEffect> potionEffects)
	{
		player.clearActivePotionEffects();
		
		for(PotionEffect potionEffect : potionEffects)
		{
			player.addPotionEffect(potionEffect);
		}
	}
}
