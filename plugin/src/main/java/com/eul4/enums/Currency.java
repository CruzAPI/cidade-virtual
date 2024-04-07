package com.eul4.enums;

import com.eul4.common.i18n.Message;
import com.eul4.i18n.PluginMessage;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum Currency
{
	LIKE(PluginMessage.LIKES),
	DISLIKE(PluginMessage.DISLIKES),
	;
	
	private final Message pluralWord;
}
