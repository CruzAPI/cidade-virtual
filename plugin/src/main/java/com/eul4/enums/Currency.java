package com.eul4.enums;

import com.eul4.common.i18n.Message;
import com.eul4.i18n.PluginMessage;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.format.Style;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

import static net.kyori.adventure.text.format.NamedTextColor.*;
import static net.kyori.adventure.text.format.Style.style;

@RequiredArgsConstructor
@Getter
public enum Currency
{
	LIKE(PluginMessage.LIKES, PluginMessage.LIKES, style(GREEN))
	{
		@Override
		public DecimalFormat getDecimalFormat(Locale locale)
		{
			return new DecimalFormat("0", new DecimalFormatSymbols(locale));
		}
	},
	
	DISLIKE(PluginMessage.DISLIKES, PluginMessage.DISLIKES, style(RED))
	{
		@Override
		public DecimalFormat getDecimalFormat(Locale locale)
		{
			return new DecimalFormat("0", new DecimalFormatSymbols(locale));
		}
	},
	
	CROWN(PluginMessage.CROWN, PluginMessage.CROWNS, style(YELLOW))
	{
		@Override
		public DecimalFormat getDecimalFormat(Locale locale)
		{
			return new DecimalFormat("0.00", new DecimalFormatSymbols(locale));
		}
	},
	;
	
	private final Message singularWord;
	private final Message pluralWord;
	private final Style style;
	
	public Message getWordFor(BigDecimal value)
	{
		return value.compareTo(BigDecimal.ONE) == 0 ? singularWord : pluralWord;
	}
	
	public abstract DecimalFormat getDecimalFormat(Locale locale);
}
