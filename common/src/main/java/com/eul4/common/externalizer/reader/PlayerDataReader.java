package com.eul4.common.externalizer.reader;

import com.eul4.common.exception.InvalidVersionException;
import com.eul4.common.model.data.PlayerData;
import com.eul4.common.type.player.CommonObjectType;
import com.eul4.common.type.player.ObjectType;
import com.eul4.common.type.player.Readers;
import com.eul4.common.wrapper.ParameterizedReadable;
import com.eul4.common.wrapper.PotionEffectCollection;
import com.eul4.common.wrapper.Readable;
import com.eul4.common.wrapper.Reader;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.io.IOException;

public class PlayerDataReader extends ObjectReader<PlayerData>
{
	private final Reader<PlayerData> reader;
	private final ParameterizedReadable<PlayerData, Plugin> parameterizedReadable;
	
	public PlayerDataReader(Readers readers) throws InvalidVersionException
	{
		super(readers, PlayerData.class);
		
		final ObjectType objectType = CommonObjectType.PLAYER_DATA;
		final byte version = readers.getVersions().get(objectType);
		
		switch(version)
		{
		case 0:
			this.reader = Reader.identity();
			this.parameterizedReadable = this::parameterizedReadableVersion0;
			break;
		default:
			throw new InvalidVersionException("Invalid " + objectType + " version: " + version);
		}
	}
	
	private Readable<PlayerData> parameterizedReadableVersion0(Plugin plugin)
	{
		return () ->
		{
			PotionEffectCollection activePotionEffects = readers.getReader(PotionEffectCollectionReader.class).readReference();
			boolean allowFlight = in.readBoolean();
			int arrowsInBody = in.readInt();
			ItemStack[] contents = readers.getReader(InventoryReader.class).readReference();
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
			ItemStack itemOnCursor = readers.getReader(ItemStackReader.class).readReference();
			Location location = readers.getReader(LocationReader.class).readReference(plugin);
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
		};
	}
	
	public PlayerData readReference(Plugin plugin) throws IOException, ClassNotFoundException
	{
		return super.readReference(parameterizedReadable.getReadable(plugin));
	}
	
	@Override
	protected PlayerData readObject(PlayerData playerData) throws IOException, ClassNotFoundException
	{
		return reader.readObject(playerData);
	}
}
