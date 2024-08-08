package com.eul4.util;

import com.eul4.Price;
import com.eul4.common.model.player.CommonPlayer;
import com.eul4.enums.Currency;
import com.eul4.i18n.PluginMessage;
import com.eul4.model.player.PluginPlayer;
import com.eul4.wrapper.Cost;
import com.google.common.collect.Multimap;
import joptsimple.internal.Strings;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.apache.commons.lang.WordUtils;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;

import java.util.*;

import static com.eul4.i18n.PluginMessage.DECORATED_VALUE_CURRENCY;
import static net.kyori.adventure.text.format.NamedTextColor.*;

public class MessageUtil
{
	public static Component getOfflinePlayerDisplayName(OfflinePlayer offlinePlayer)
	{
		return offlinePlayer.isOnline()
				? offlinePlayer.getPlayer().displayName()
				: Component.text(Optional.ofNullable(offlinePlayer.getName()).orElse("Unknown")).color(DARK_GRAY);
	}
	
	public static Component getDecrescentPercentageProgressBar(int percentage)
	{
		percentage = Math.max(0, Math.min(100, percentage));
		return getPercentageProgressBar(100 - percentage);
	}
	
	public static Component getPercentageProgressBar(int percentage)
	{
		percentage = Math.max(0, Math.min(100, percentage));
		int remainingPercentage = 100 - percentage;
		
		Component done = Component.text(Strings.repeat('|', percentage)).color(NamedTextColor.GREEN);
		Component remaining = Component.text(Strings.repeat('|', remainingPercentage)).color(NamedTextColor.DARK_GRAY);
		
		return done.append(remaining).decoration(TextDecoration.ITALIC, false);
	}
	
	public static List<Component> getPriceLore(Price price, CommonPlayer commonPlayer)
	{
		List<Component> components = new ArrayList<>();
		
		if(price.getLikes() > 0)
		{
			components.add(DECORATED_VALUE_CURRENCY.translate(commonPlayer,
					Currency.LIKE,
					price.getLikes()));
		}
		
		if(price.getDislikes() > 0)
		{
			components.add(DECORATED_VALUE_CURRENCY.translate(commonPlayer,
					Currency.DISLIKE,
					price.getDislikes()));
		}
		
		return components;
	}
	
	public static List<Component> getCostLore(Cost cost, CommonPlayer commonPlayer)
	{
		List<Component> components = new ArrayList<>();
		
		Price price = cost.getPrice();
		
		if(price.getLikes() > 0)
		{
			components.add(getPontuatedComponent()
					.append(DECORATED_VALUE_CURRENCY.translate(commonPlayer,
					Currency.LIKE,
					price.getLikes())));
		}
		
		if(price.getDislikes() > 0)
		{
			components.add(getPontuatedComponent()
					.append(DECORATED_VALUE_CURRENCY.translate(commonPlayer,
					Currency.DISLIKE,
					price.getDislikes())));
		}
		
		for(Map.Entry<Material, Integer> entry : cost.getResources().entrySet())
		{
			Material type = entry.getKey();
			components.add(getPontuatedComponent()
					.append(Component.text(entry.getValue()))
					.append(Component.text(" "))
					.append(Component.translatable(type.translationKey())));
		}
		
		if(!components.isEmpty())
		{
			components.add(0, getBlankComponent()
					.append(PluginMessage.PRICE.translateWord(commonPlayer, WordUtils::capitalizeFully))
					.append(Component.text(":")));
		}
		
		return components;
	}
	
	public static List<Component> getCostLoreV2(Cost cost, CommonPlayer commonPlayer)
	{
		if(cost.isFree())
		{
			return Collections.emptyList();
		}
		
		List<Component> components = new ArrayList<>();
		
		components.add(PluginMessage.COST.translateWord(commonPlayer, WordUtils::capitalize)
				.append(Component.text(":"))
				.color(GRAY));
		
		components.add(Component.empty());
		
		Price price = cost.getPrice();
		
		if(price.getLikes() > 0)
		{
			components.add(getPontuatedComponent()
					.append(DECORATED_VALUE_CURRENCY.translate(commonPlayer,
							Currency.LIKE,
							price.getLikes())));
		}
		
		if(price.getDislikes() > 0)
		{
			components.add(getPontuatedComponent()
					.append(DECORATED_VALUE_CURRENCY.translate(commonPlayer,
							Currency.DISLIKE,
							price.getDislikes())));
		}
		
		for(Map.Entry<Material, Integer> entry : cost.getResources().entrySet())
		{
			Material type = entry.getKey();
			components.add(getPontuatedComponent()
					.append(Component.text(entry.getValue()))
					.append(Component.text(" "))
					.append(Component.translatable(type.translationKey())));
		}
		
		return components;
	}
	
	public static List<Component> getDurabilityLore(int durability, PluginPlayer pluginPlayer)
	{
		if(durability < 0)
		{
			return Collections.emptyList();
		}
		
		return List.of(getBlankComponent()
				.append(PluginMessage.DURABILITY.translateWord(pluginPlayer, WordUtils::capitalizeFully))
				.append(Component.text(": ")
				.append(Component.text(durability))));
	}
	
	public static List<Component> getAttributesLore(Multimap<Attribute, AttributeModifier> attributeModifiers, PluginPlayer pluginPlayer)
	{
		List<Component> components = new ArrayList<>();
		
		for(Map.Entry<Attribute, AttributeModifier> entry : attributeModifiers.entries())
		{
			Attribute attribute = entry.getKey();
			AttributeModifier attributeModifier = entry.getValue();
			
			components.add(getBlankComponent()
					.append(Component.translatable(attribute.translationKey())
					.append(Component.text(": "))
					.append(Component.text(attributeModifier.getAmount()))));
		}
		
		return components;
	}
	
	private static Component getBlankComponent()
	{
		return Component.empty().color(WHITE).decoration(TextDecoration.ITALIC, false);
	}
	
	private static Component getPontuatedComponent()
	{
		return getBlankComponent().append(Component.text("- "));
	}
}
