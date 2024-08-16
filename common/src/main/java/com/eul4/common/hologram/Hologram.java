package com.eul4.common.hologram;

import com.eul4.common.Common;
import com.eul4.common.i18n.Message;
import com.eul4.common.i18n.ResourceBundleHandler;
import com.eul4.common.util.ThreadUtil;
import lombok.Getter;
import lombok.Setter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.minecraft.server.level.ServerLevel;
import org.bukkit.Location;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.entity.ArmorStand;

import java.util.*;
import java.util.function.Consumer;

import static com.eul4.common.constant.CommonNamespacedKey.FAWE_IGNORE;
import static org.bukkit.persistence.PersistentDataType.BOOLEAN;

@Getter
public class Hologram
{
	private final ArrayList<TranslatedHologramLine> hologramLines = new ArrayList<>();
	
	private final Common plugin;
	private Location location;
	
	public Hologram(Common plugin, Location location)
	{
		this.plugin = plugin;
		this.location = location;
	}
	
	public void remove()
	{
		hologramLines.forEach(TranslatedHologramLine::remove);
		hologramLines.clear();
	}
	
	public TranslatedHologramLine getLine(int line)
	{
		return hologramLines.get(line);
	}
	
	public void newLine()
	{
		newLine(Component.empty());
	}
	
	public void newLine(Message message, Object... args)
	{
		newLine(line -> line.setMessageAndArgs(message, args));
	}
	
	public void newLine(Component component)
	{
		newLine(line -> line.setCustomName(component));
	}
	
	private void newLine(Consumer<TranslatedHologramLine> setTextFunction)
	{
		ArmorStand armorStand = newArmorStandNotSpawned();
		TranslatedHologramLine line = new TranslatedHologramLine(armorStand);
		
		hologramLines.add(line);
		
		TranslatedHologramLine.ARMOR_STAND_UUID_MAP.put(armorStand.getUniqueId(), line);
		plugin.getHologramTranslatorAdapter().getHolograms().put(armorStand, line);
		setTextFunction.accept(line);
		
		spawnArmorStandSynchronously(armorStand);
	}
	
	private void spawnArmorStandSynchronously(ArmorStand armorStand)
	{
		ThreadUtil.runSynchronouslyUntilTerminate(plugin, () -> armorStand.spawnAt(armorStand.getLocation()));
	}
	
	public static boolean isHologram(ArmorStand armorStand)
	{
		return armorStand.isMarker()
				&& !armorStand.hasGravity()
				&& armorStand.isInvisible()
				&& armorStand.isInvulnerable();
	}
	
	private ArmorStand newArmorStandNotSpawned()
	{
		final Location location = this.location.clone().add(0.0D, 0.28D * hologramLines.size(), 0.0D); //TODO strategy
		
		ServerLevel serverLevel = ((CraftWorld) location.getWorld()).getHandle();
		var nmsArmorStand = new net.minecraft.world.entity.decoration.ArmorStand(serverLevel,
				location.getX(),
				location.getY(),
				location.getZ());
		
		final ArmorStand armorStand = (ArmorStand) nmsArmorStand.getBukkitEntity();
		
		armorStand.getPersistentDataContainer().set(FAWE_IGNORE, BOOLEAN, true);
		armorStand.setPersistent(true);
		armorStand.setInvulnerable(true);
		armorStand.setVisible(false);
		armorStand.setGravity(false);
		armorStand.setMarker(true);
		armorStand.setCanPickupItems(false);
		armorStand.setCustomNameVisible(true);
		
		return armorStand;
	}
	
	public void teleport(Location location)
	{
		this.location = location;
		
		for(int i = 0; i < hologramLines.size(); i++)
		{
			hologramLines.get(i).armorStand.teleport(location.clone()
					.add(0.0D, 0.28D * i, 0.0D));
		}
	}
	
	public int size()
	{
		return hologramLines.size();
	}
	
	public void setSize(int size)
	{
		if(size == hologramLines.size())
		{
			return;
		}
		
		remove();
		
		for(int i = 0; i < size; i++)
		{
			newLine();
		}
	}
	
	public void load()
	{
		hologramLines.forEach(TranslatedHologramLine::load);
	}
	
	@Getter
	public class TranslatedHologramLine
	{
		public static final Map<UUID, TranslatedHologramLine> ARMOR_STAND_UUID_MAP = new HashMap<>();
		
		@Setter
		private ArmorStand armorStand;
		
		private Message message;
		private Object[] args;
		
		public TranslatedHologramLine(ArmorStand armorStand)
		{
			this.armorStand = Objects.requireNonNull(armorStand);
		}
		
		public void setMessageAndArgs(Message message, Object... args)
		{
			this.message = message;
			this.args = args;
			
			this.armorStand.customName(message.translate(ResourceBundleHandler.DEFAULT_LOCALE, args));
		}
		
		public void setCustomName(Component component)
		{
			this.message = null;
			this.args = null;
			this.armorStand.customName(component);
		}
		
		public String translate(Locale locale)
		{
			return LegacyComponentSerializer.legacySection().serialize(message == null
					? this.armorStand.customName()
					: message.translate(locale, args));
		}
		
		public Common getPlugin()
		{
			return plugin;
		}
		
		@Override
		public String toString()
		{
			return Optional
					.ofNullable(armorStand)
					.map(ArmorStand::getUniqueId)
					.map(UUID::toString)
					.orElse("null");
		}
		
		public void remove()
		{
			ARMOR_STAND_UUID_MAP.remove(armorStand.getUniqueId());
			armorStand.remove();
		}
		
		public void load()
		{
			ARMOR_STAND_UUID_MAP.put(armorStand.getUniqueId(), this);
		}
	}
}
