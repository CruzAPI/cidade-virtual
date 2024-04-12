package com.eul4.util;

import com.eul4.Price;
import com.eul4.common.model.player.CommonPlayer;
import com.eul4.enums.Currency;
import com.eul4.i18n.PluginMessage;
import net.kyori.adventure.text.Component;

import java.util.ArrayList;
import java.util.List;

import static com.eul4.i18n.PluginMessage.DECORATED_VALUE_CURRENCY;
import static net.kyori.adventure.text.Component.empty;
import static net.kyori.adventure.text.format.NamedTextColor.GREEN;
import static net.kyori.adventure.text.format.NamedTextColor.RED;

public class MessageUtil
{
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
