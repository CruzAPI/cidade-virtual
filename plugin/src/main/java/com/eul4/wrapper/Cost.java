package com.eul4.wrapper;

import com.eul4.Price;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.Material;

import java.util.Collections;
import java.util.Map;

@RequiredArgsConstructor
@Builder
@Getter
public class Cost
{
	private final Price price;
	private final Map<Material, Integer> resources;
	
	public Cost(Price price)
	{
		this.price = price;
		this.resources = Collections.emptyMap();
	}
	
	public boolean isFree()
	{
		return price.isFree() && resources.isEmpty();
	}
}
