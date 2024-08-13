package com.eul4.common.wrapper;

import com.eul4.common.Common;
import com.eul4.common.model.permission.Expirable;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

import java.util.List;
import java.util.Locale;

import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.*;

public abstract class ExpirablePage<E extends Expirable> extends Page<E>
{
	public ExpirablePage(Common plugin, int index, int pageSize, List<E> pageElements, List<E> fullList)
	{
		super(plugin, index, pageSize, pageElements, fullList);
	}
	
	protected Component getComponentTranslated(E expirable, Locale locale)
	{
		String name = expirable.getNameOrUnknown(plugin);
		Component timer = Component.empty();
		NamedTextColor color;
		
		if(!expirable.isValid(plugin))
		{
			color = RED;
		}
		else if(expirable.isPermanent())
		{
			color = GREEN;
		}
		else
		{
			color = YELLOW;
			timer = TimerTranslator.translate(expirable.getRemainingTick(plugin), locale);
		}
		
		Component userNameComponent = text(name).color(color);
		
		return text(" - ").color(GRAY)
				.append(userNameComponent)
				.appendSpace()
				.append(timer);
	}
}
