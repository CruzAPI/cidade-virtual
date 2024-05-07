package com.eul4.util;

import com.eul4.Price;
import com.eul4.common.model.player.CommonPlayer;
import com.eul4.enums.Currency;
import com.eul4.i18n.PluginMessage;
import joptsimple.internal.Strings;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.OfflinePlayer;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.eul4.i18n.PluginMessage.DECORATED_VALUE_CURRENCY;
import static net.kyori.adventure.text.Component.empty;
import static net.kyori.adventure.text.format.NamedTextColor.*;

public class MessageUtil
{
	public static Component getOfflinePlayerDisplayName(OfflinePlayer offlinePlayer)
	{
		return offlinePlayer.isOnline()
				? offlinePlayer.getPlayer().displayName()
				: Component.text(Optional.ofNullable(offlinePlayer.getName()).orElse("Unknown")).color(DARK_GRAY);
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
					empty().color(GREEN),
					price.getLikes(),
					Currency.LIKE));
		}
		
		if(price.getDislikes() > 0)
		{
			components.add(DECORATED_VALUE_CURRENCY.translate(commonPlayer,
					empty().color(RED),
					price.getDislikes(),
					Currency.DISLIKE));
		}
		
		return components;
	}
}
