package com.eul4.common.externalizer.reader;

import com.eul4.common.Common;
import com.eul4.common.exception.InvalidVersionException;
import com.eul4.common.model.data.PlayerData;
import com.eul4.common.wrapper.CommonVersions;
import com.eul4.common.wrapper.Reader;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

import java.io.IOException;
import java.io.ObjectInput;
import java.util.Collection;

public class PlayerDataReader extends ObjectReader<PlayerData>
{
	private final Reader<PlayerData> reader;
	
	private final PotionEffectCollectionReader potionEffectCollectionReader;
	private final LocationReader locationReader;
	private final InventoryReader inventoryReader;
	private final ItemStackReader itemStackReader;
	
	public PlayerDataReader(ObjectInput in, CommonVersions commonVersions, Common plugin) throws InvalidVersionException
	{
		super(in, commonVersions);
		
		this.potionEffectCollectionReader = new PotionEffectCollectionReader(in, commonVersions);
		this.locationReader = new LocationReader(in, commonVersions, plugin);
		this.inventoryReader = new InventoryReader(in, commonVersions);
		this.itemStackReader = inventoryReader.getItemStackReader();
		
		if(commonVersions.getPlayerDataVersion() == 0)
		{
			this.reader = this::readerVersion0;
		}
		else
		{
			throw new InvalidVersionException("Invalid PlayerData version: " + commonVersions.getPlayerDataVersion());
		}
	}
	
	private PlayerData readerVersion0() throws IOException, ClassNotFoundException
	{
		Collection<PotionEffect> activePotionEffects = potionEffectCollectionReader.readReference();
		boolean allowFlight = in.readBoolean();
		int arrowsInBody = in.readInt();
		ItemStack[] contents = inventoryReader.readReference();
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
		ItemStack itemOnCursor = itemStackReader.readReference();
		Location location = locationReader.readReference();
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
	
	@Override
	protected PlayerData readObject() throws IOException, ClassNotFoundException
	{
		return reader.readObject();
	}
}
