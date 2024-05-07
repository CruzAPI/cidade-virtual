package com.eul4.common.externalizer.writer;

import com.eul4.common.model.data.PlayerData;

import java.io.IOException;
import java.io.ObjectOutput;

public class PlayerDataWriter extends ObjectWriter<PlayerData>
{
	private final PotionEffectCollectionWriter potionEffectCollectionWriter;
	private final InventoryWriter inventoryWriter;
	private final ItemStackWriter itemStackWriter;
	private final LocationWriter locationWriter;
	
	public PlayerDataWriter(ObjectOutput out)
	{
		super(out);
		
		this.potionEffectCollectionWriter = new PotionEffectCollectionWriter(out);
		this.inventoryWriter = new InventoryWriter(out);
		this.itemStackWriter = inventoryWriter.getItemStackWriter();
		this.locationWriter = new LocationWriter(out);
	}
	
	@Override
	protected void writeObject(PlayerData playerData) throws IOException
	{
		potionEffectCollectionWriter.writeReference(playerData.getActivePotionEffects());
		out.writeBoolean(playerData.isAllowFlight());
		out.writeInt(playerData.getArrowsInBody());
		inventoryWriter.writeReference(playerData.getContents());
		out.writeFloat(playerData.getExhaustion());
		out.writeFloat(playerData.getFallDistance());
		out.writeInt(playerData.getFireTicks());
		out.writeBoolean(playerData.isFlying());
		out.writeFloat(playerData.getFlySpeed());
		out.writeInt(playerData.getFoodLevel());
		out.writeInt(playerData.getFreezeTicks());
		out.writeInt(playerData.getGameMode().ordinal());
		out.writeDouble(playerData.getHealth());
		out.writeDouble(playerData.getHealthScale());
		itemStackWriter.writeReference(playerData.getItemOnCursor());
		locationWriter.writeReference(playerData.getLocation());
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
