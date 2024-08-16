package com.eul4.common.wrapper;

import com.eul4.common.model.player.CommonPlayer;
import lombok.experimental.UtilityClass;
import net.kyori.adventure.text.Component;

import java.util.Locale;
import java.util.ResourceBundle;

import static com.eul4.common.i18n.CommonMessage.*;
import static net.kyori.adventure.text.Component.empty;
import static net.kyori.adventure.text.Component.text;

@UtilityClass
public class TimerTranslator
{
	public static Component translate(long ticks, ResourceBundle bundle)
	{
		return translate(ticks, bundle.getLocale());
	}
	
	public static Component translate(long ticks, CommonPlayer commonPlayer)
	{
		return translate(ticks, commonPlayer.getLocale());
	}
	
	public static Component translate(long ticks, Locale locale)
	{
		if(ticks < 0L)
		{
			return Component.empty();
		}
		
		if(ticks == Long.MAX_VALUE)
		{
			return Component.text("∞");
		}
		
		long days = ticks / (20L * 60L * 60L * 24L);
		long hours = ticks / (20L * 60L * 60L) % 24L;
		long minutes = ticks / (20L * 60L) % 60L;
		long seconds = ticks / 20L % 60L;
		
		return translate(days, hours, minutes, seconds, locale);
	}
	
	public static Component translate(long days, long hours, long minutes, long seconds, Locale locale)
	{
		Component translated = empty();
		boolean appended = false;
		
		if(days != 0L)
		{
			translated = translated.append(text((appended ? " " : "") + days)).append(DAYS_CHAR.translate(locale));
			appended = true;
		}
		
		if(hours != 0L)
		{
			translated = translated.append(text((appended ? " " : "") + hours)).append(HOURS_CHAR.translate(locale));
			appended = true;
		}
		
		if(minutes != 0L)
		{
			translated = translated.append(text((appended ? " " : "") + minutes)).append(MINUTES_CHAR.translate(locale));
			appended = true;
		}
		
		if(seconds != 0L || (days == 0L && hours == 0L && minutes == 0L))
		{
			translated = translated.append(text((appended ? " " : "") + seconds)).append(SECONDS_CHAR.translate(locale));
		}
		
		return translated;
	}
}
