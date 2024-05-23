package com.eul4.common.externalizer.writer;

import com.eul4.common.model.data.PlayerData;
import com.eul4.common.type.player.Writers;

import java.io.IOException;

public class PlayerDataWriter extends ObjectWriter<PlayerData>
{
	public PlayerDataWriter(Writers writers)
	{
		super(writers, PlayerData.class);
	}
	
	@Override
	protected void writeObject(PlayerData playerData) throws IOException
	{
		writers.getWriter(PotionEffectCollectionWriter.class).writeReference(playerData.getActivePotionEffects());
		out.writeBoolean(playerData.isAllowFlight());
		out.writeInt(playerData.getArrowsInBody());
		writers.getWriter(InventoryWriter.class).writeReference(playerData.getContents());
		out.writeFloat(playerData.getExhaustion());
		out.writeFloat(playerData.getExp());
		out.writeFloat(playerData.getFallDistance());
		out.writeInt(playerData.getFireTicks());
		out.writeBoolean(playerData.isFlying());
		out.writeFloat(playerData.getFlySpeed());
		out.writeInt(playerData.getFoodLevel());
		out.writeInt(playerData.getFreezeTicks());
		out.writeInt(playerData.getGameMode().ordinal());
		out.writeDouble(playerData.getHealth());
		out.writeDouble(playerData.getHealthScale());
		writers.getWriter(ItemStackWriter.class).writeReference(playerData.getItemOnCursor());
		out.writeInt(playerData.getLevel());
		writers.getWriter(LocationWriter.class).writeReference(playerData.getLocation());
		out.writeInt(playerData.getMaximumAir());
		out.writeInt(playerData.getMaximumNoDamageTicks());
		out.writeInt(playerData.getNoActionTicks());
		out.writeInt(playerData.getNoDamageTicks());
		out.writeInt(playerData.getRemainingAir());
		out.writeFloat(playerData.getSaturation());
		out.writeInt(playerData.getUnsaturatedRegenRate());
		out.writeFloat(playerData.getWalkSpeed());
	}
}
