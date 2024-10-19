package com.eul4.enums;

import com.eul4.common.i18n.Message;
import com.eul4.i18n.PluginMessage;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.format.Style;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;
import java.util.ResourceBundle;

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
			DecimalFormat decimalFormat = new DecimalFormat("0", new DecimalFormatSymbols(locale));
			
			decimalFormat.setGroupingSize(3);
			decimalFormat.setGroupingUsed(true);
			
			return decimalFormat;
		}
	},
	
	DISLIKE(PluginMessage.DISLIKES, PluginMessage.DISLIKES, style(RED))
	{
		@Override
		public DecimalFormat getDecimalFormat(Locale locale)
		{
			DecimalFormat decimalFormat = new DecimalFormat("0", new DecimalFormatSymbols(locale));
			
			decimalFormat.setGroupingSize(3);
			decimalFormat.setGroupingUsed(true);
			
			return decimalFormat;
		}
	},
	
	CROWN(PluginMessage.CROWN, PluginMessage.CROWNS, style(YELLOW))
	{
		@Override
		public DecimalFormat getDecimalFormat(Locale locale)
		{
			DecimalFormat decimalFormat = new DecimalFormat("0.00");
			
			decimalFormat.setDecimalFormatSymbols(new DecimalFormatSymbols(locale));
			decimalFormat.setGroupingSize(3);
			decimalFormat.setGroupingUsed(true);
			
			return decimalFormat;
		}
		
		@Override
		public DecimalFormat getAccurateDecimalFormat(Locale locale)
		{
			DecimalFormat decimalFormat = new DecimalFormat("0.00######");
			
			decimalFormat.setDecimalFormatSymbols(new DecimalFormatSymbols(locale));
			decimalFormat.setGroupingSize(3);
			decimalFormat.setGroupingUsed(true);
			
			return decimalFormat;
		}
	},
	;
	
	private final Message singularWord;
	private final Message pluralWord;
	private final Style style;
	
	public Message getWordFor(Number number)
	{
		return number.doubleValue() == 1.0D ? singularWord : pluralWord;
	}
	
	public abstract DecimalFormat getDecimalFormat(Locale locale);
	
	public final DecimalFormat getAccurateDecimalFormat(ResourceBundle bundle)
	{
		return getAccurateDecimalFormat(bundle.getLocale());
	}
	
	public DecimalFormat getAccurateDecimalFormat(Locale locale)
	{
		return getDecimalFormat(locale);
	}
	
	public final DecimalFormat getDecimalFormat(ResourceBundle bundle)
	{
		return getDecimalFormat(bundle.getLocale());
	}
}
