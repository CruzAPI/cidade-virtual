package com.eul4.common.wrapper;

import com.eul4.common.model.player.CommonPlayer;
import net.kyori.adventure.text.Component;

import java.util.Locale;

import static com.eul4.common.i18n.CommonMessage.*;
import static net.kyori.adventure.text.Component.empty;
import static net.kyori.adventure.text.Component.text;

public class TimerTranslater
{
	public static Component translate(int ticks, CommonPlayer commonPlayer)
	{
		return translate(ticks, commonPlayer.getLocale());
	}
	
	public static Component translate(int ticks, Locale locale)
	{
		int days = ticks / (20 * 60 * 60 * 24);
		int hours = ticks / (20 * 60 * 60) % 24;
		int minutes = ticks / (20 * 60) % 60;
		int seconds = ticks / 20 % 60;
		
		return translate(days, hours, minutes, seconds, locale);
	}
	
	public static Component translate(int days, int hours, int minutes, int seconds, Locale locale)
	{
		Component translated = empty();
		boolean appended = false;
		
		if(days != 0)
		{
			translated = translated.append(DAYS_CHAR.translate(locale));
			appended = true;
		}
		
		if(hours != 0)
		{
			translated = translated.append(text((appended ? " " : "") + hours)).append(HOURS_CHAR.translate(locale));
			appended = true;
		}
		
		if(minutes != 0)
		{
			translated = translated.append(text((appended ? " " : "") + minutes)).append(MINUTES_CHAR.translate(locale));
			appended = true;
		}
		
		if(seconds != 0 || (days == 0 && hours == 0 && minutes == 0))
		{
			translated = translated.append(text((appended ? " " : "") + seconds)).append(SECONDS_CHAR.translate(locale));
		}
		
		return translated;
	}
}
