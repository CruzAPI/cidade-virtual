package com.eul4.common.interceptor;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.reflect.StructureModifier;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import com.comphenix.protocol.wrappers.WrappedDataValue;
import com.eul4.common.Common;
import com.eul4.common.hologram.Hologram;
import com.eul4.common.model.player.CommonPlayer;
import lombok.Getter;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.comphenix.protocol.wrappers.WrappedDataWatcher.Registry.getChatComponentSerializer;

@Getter
public class HologramTranslatorAdapter extends PacketAdapter
{
	private final Common plugin;
	private Map<Entity, Hologram.TranslatedHologramLine> holograms = new HashMap<>();
	
	public HologramTranslatorAdapter(Common plugin)
	{
		super(plugin, PacketType.Play.Server.ENTITY_METADATA);
		
		this.plugin = plugin;
	}
	
	@Override
	public void onPacketSending(PacketEvent event)
	{
		final Player player = event.getPlayer();
		final PacketContainer packet = event.getPacket();
		final StructureModifier<Entity> entityModifier = packet.getEntityModifier(player.getWorld());
		final Entity entity = entityModifier.read(0);
		
		if(!holograms.containsKey(entity))
		{
			return;
		}
		
		final CommonPlayer commonPlayer = plugin.getPlayerManager().get(player);
		
		Hologram.TranslatedHologramLine translatedHologramLine = holograms.get(entity);
		
		String translatedMessage = translatedHologramLine.translate(commonPlayer.getLocale());
		
		Optional<?> name = Optional.of(WrappedChatComponent.fromChatMessage(translatedMessage)[0].getHandle());
		
		List< WrappedDataValue> metadata = packet.getDataValueCollectionModifier().read(0);
		
		WrappedDataValue customName = new WrappedDataValue(2, getChatComponentSerializer(true), name);
		
		metadata.add(customName);
		
		packet.getDataValueCollectionModifier().write(0, metadata);
	}
}
