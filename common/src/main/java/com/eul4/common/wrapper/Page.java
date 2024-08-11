package com.eul4.common.wrapper;

import com.eul4.common.Common;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@RequiredArgsConstructor
@Getter
public abstract class Page<T>
{
	protected final Common plugin;
	protected final int index;
	protected final int pageSize;
	protected final List<T> pageElements;
	protected final List<T> fullList;
	
	public boolean isEmpty()
	{
		return pageElements.isEmpty();
	}
	
	public boolean hasPreviousPage()
	{
		return index > 0;
	}
	
	public boolean hasNextPage()
	{
		return index + 1 < getAmountOfPages();
	}
	
	public int getDisplayIndex()
	{
		return index + 1;
	}
	
	public int getAmountOfPages()
	{
		return fullList.size() / pageSize + (fullList.size() % pageSize != 0 ? 1 : 0);
	}
	
	public final List<Component> getPageComponentsTranslated(Locale locale)
	{
		List<Component> components = new ArrayList<>();
		
		for(int i = 0; i < pageSize; i++)
		{
			if(i < pageElements.size())
			{
				components.add(getComponentTranslated(pageElements.get(i), locale));
			}
			else
			{
				components.add(Component.empty());
			}
		}
		
		return components;
	}
	
	protected abstract Component getComponentTranslated(T element, Locale locale);
}
