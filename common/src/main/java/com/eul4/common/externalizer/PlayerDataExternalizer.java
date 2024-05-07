package com.eul4.common.externalizer;

import com.eul4.common.Common;
import com.eul4.common.model.data.PlayerData;
import lombok.RequiredArgsConstructor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;

@RequiredArgsConstructor
public class PlayerDataExternalizer
{
	private final Common plugin;
	
	public static final long VERSION = 2L;
	
	public PlayerData read(long version, ObjectInput in) throws IOException, ClassNotFoundException
	{
		if(version == 0L)
		{
			Location location = plugin.getLocationExternalizer().read(in);
			ItemStack[] contents = plugin.getInventoryExternalizer().read(in);
			
			return PlayerData.builder()
					.location(location)
					.contents(contents)
					.build();
		}
		
		if(version == 1L)
		{
			Location location = plugin.getLocationExternalizer().read(in);
			ItemStack[] contents = plugin.getInventoryExternalizer().read(in);
			double health = in.readDouble();
			int foodLevel = in.readInt();
			Collection<PotionEffect> activePotionEffects = readPotionEffects(version, in);
			GameMode gameMode = GameMode.values()[in.readInt()];
			boolean allowFlight = in.readBoolean();
			boolean flying = in.readBoolean();
			int totalExperience = in.readInt();
			
			return PlayerData.builder()
					.location(location)
					.contents(contents)
					.health(health)
					.foodLevel(foodLevel)
					.activePotionEffects(activePotionEffects)
					.gameMode(gameMode)
					.allowFlight(allowFlight)
					.flying(flying)
					.totalExperience(totalExperience)
					.build();
		}
		
		if(version == 2L)
		{
			Collection<PotionEffect> activePotionEffects = readPotionEffects(version, in);
			boolean allowFlight = in.readBoolean();
			int arrowsInBody = in.readInt();
			ItemStack[] contents = plugin.getInventoryExternalizer().read(in);
			float exhaustion = in.readFloat();
			float fallDistance = in.readFloat();
			int fireTicks = in.readInt();
			boolean flying = in.readBoolean();
			float flySpeed = in.readFloat();
			int foodLevel = in.readInt();
			int freezeTicks = in.readInt();
			GameMode gameMode = GameMode.values()[in.readInt()];
			double health = in.readDouble();
			double healthScale = in.readDouble();
			ItemStack itemOnCursor = plugin.getItemStackExternalizer().read(in);
			Location location = plugin.getLocationExternalizer().read(in);
			int maximumAir = in.readInt();
			int maximumNoDamageTicks = in.readInt();
			int noActionTicks = in.readInt();
			int noDamageTicks = in.readInt();
			int remainingAir = in.readInt();
			float saturation = in.readFloat();
			int totalExperience = in.readInt();
			int unsaturatedRegenRate = in.readInt();
			float walkSpeed = in.readFloat();
			
			return PlayerData.builder()
					.activePotionEffects(activePotionEffects)
					.allowFlight(allowFlight)
					.arrowsInBody(arrowsInBody)
					.contents(contents)
					.exhaustion(exhaustion)
					.fallDistance(fallDistance)
					.fireTicks(fireTicks)
					.flying(flying)
					.flySpeed(flySpeed)
					.foodLevel(foodLevel)
					.freezeTicks(freezeTicks)
					.gameMode(gameMode)
					.health(health)
					.healthScale(healthScale)
					.itemOnCursor(itemOnCursor)
					.location(location)
					.maximumAir(maximumAir)
					.maximumNoDamageTicks(maximumNoDamageTicks)
					.noActionTicks(noActionTicks)
					.noDamageTicks(noDamageTicks)
					.remainingAir(remainingAir)
					.saturation(saturation)
					.totalExperience(totalExperience)
					.unsaturatedRegenRate(unsaturatedRegenRate)
					.walkSpeed(walkSpeed)
					.build();
		}
		
		throw new RuntimeException();
	}
	
	public PlayerData read(ObjectInput in) throws IOException, ClassNotFoundException
	{
		return read(in.readLong(), in);
	}
	
	private Collection<PotionEffect> readPotionEffects(long version, ObjectInput in)
			throws IOException, ClassNotFoundException
	{
		if(version == 1L || version == 2L)
		{
			Collection<PotionEffect> potionEffects = new LinkedList<>();
			int size = in.readInt();
			
			for(int i = 0; i < size; i++)
			{
				potionEffects.add(new PotionEffect((Map<String, Object>) in.readObject()));
			}
			
			return potionEffects;
		}
		
		throw new RuntimeException();
	}
	
	private void writePotionEffects(Collection<PotionEffect> potionEffects, ObjectOutput out) throws IOException
	{
		out.writeInt(potionEffects.size());
		
		for(PotionEffect potionEffect : potionEffects)
		{
			out.writeObject(potionEffect.serialize());
		}
	}
	
	public void write(PlayerData playerData, ObjectOutput out) throws IOException
	{
		out.writeLong(VERSION);
		
		writePotionEffects(playerData.getActivePotionEffects(), out);
		out.writeBoolean(playerData.isAllowFlight());
		out.writeInt(playerData.getArrowsInBody());
		plugin.getInventoryExternalizer().write(playerData.getContents(), out);
		out.writeFloat(playerData.getExhaustion());
		out.writeFloat(playerData.getFallDistance());
		out.writeInt(playerData.getFireTicks());
		out.writeBoolean(playerData.isFlying());
		out.writeFloat(playerData.getFlySpeed());
		out.writeInt(playerData.getFoodLevel());
		out.writeInt(playerData.getFreezeTicks());
		out.writeObject(playerData.getGameMode());
		out.writeDouble(playerData.getHealth());
		out.writeDouble(playerData.getHealthScale());
		plugin.getItemStackExternalizer().writeVersioned(playerData.getItemOnCursor(), out);
		plugin.getLocationExternalizer().write(playerData.getLocation(), out);
		out.writeInt(playerData.getMaximumAir());
		out.writeInt(playerData.getMaximumNoDamageTicks());
		out.writeInt(playerData.getNoActionTicks());
		out.writeInt(playerData.getNoDamageTicks());
		out.writeInt(playerData.getRemainingAir());
		out.writeFloat(playerData.getSaturation());
		out.writeInt(playerData.getTotalExperience());
		out.writeInt(playerData.getUnsaturatedRegenRate());
		out.writeFloat(playerData.getWalkSpeed());
	}
}
