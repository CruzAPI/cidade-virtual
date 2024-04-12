package com.eul4.enums;

import com.eul4.common.i18n.Message;
import com.eul4.i18n.PluginMessage;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

import static net.kyori.adventure.text.format.NamedTextColor.GREEN;
import static net.kyori.adventure.text.format.NamedTextColor.RED;

@RequiredArgsConstructor
@Getter
public enum Currency
{
	LIKE(PluginMessage.LIKES, Component.empty().color(GREEN)),
	DISLIKE(PluginMessage.DISLIKES, Component.empty().color(RED)),
	;
	
	private final Message pluralWord;
	private final Component baseComponent;
}
