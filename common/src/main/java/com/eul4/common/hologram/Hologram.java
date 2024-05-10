package com.eul4.common.hologram;

import com.eul4.common.Common;
import com.eul4.common.i18n.Message;
import com.eul4.common.i18n.ResourceBundleHandler;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.minecraft.server.level.ServerLevel;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_20_R3.CraftWorld;
import org.bukkit.entity.ArmorStand;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;
import java.util.function.Consumer;

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
		hologramLines.forEach(translatedHologramLine -> translatedHologramLine.armorStand.remove());
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
		plugin.getHologramTranslatorAdapter().getHolograms().put(armorStand, line);
		setTextFunction.accept(line);
		
		armorStand.spawnAt(armorStand.getLocation());
	}
	
	private ArmorStand newArmorStandNotSpawned()
	{
		final Location location = this.location.clone().subtract(0.0D, 0.28D * hologramLines.size(), 0.0D);
		
		ServerLevel serverLevel = ((CraftWorld) location.getWorld()).getHandle();
		var nmsArmorStand = new net.minecraft.world.entity.decoration.ArmorStand(serverLevel,
				location.getX(),
				location.getY(),
				location.getZ());
		
		final ArmorStand armorStand = (ArmorStand) nmsArmorStand.getBukkitEntity();
		
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
					.subtract(0.0D, 0.28D * i, 0.0D));
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
	
	public class TranslatedHologramLine
	{
		private final ArmorStand armorStand;
		
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
	}
}
