package com.eul4.enums;

import com.eul4.common.i18n.Message;
import com.eul4.i18n.PluginMessage;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextDecoration;

import static net.kyori.adventure.text.format.NamedTextColor.*;
import static net.kyori.adventure.text.format.Style.style;
import static net.kyori.adventure.text.format.TextDecoration.BOLD;

@RequiredArgsConstructor
@Getter
public enum Rarity
{
	COMMON((byte) 0, PluginMessage.COMMON, PluginMessage.RARITY_COMMON, style(GREEN, BOLD)),
	RARE((byte) 1, PluginMessage.RARE, PluginMessage.RARITY_RARE, style(DARK_PURPLE, BOLD)),
	LEGENDARY((byte) 2, PluginMessage.LEGENDARY, PluginMessage.RARITY_LEGENDARY, style(GOLD, BOLD)),
	;
	
	public static final Rarity DEFAULT_RARITY = COMMON;
	
	private final byte id;
	private final Message rawMessage;
	private final Message stylizedMessage;
	private final Style style;
	
	public static Rarity getRarityById(byte id)
	{
		for(Rarity rarity : values())
		{
			if(rarity.getId() == id)
			{
				return rarity;
			}
		}
		
		return null;
	}
}
