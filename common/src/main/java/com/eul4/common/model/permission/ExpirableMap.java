package com.eul4.common.model.permission;

import com.eul4.common.Common;

import java.util.Map;

public class ExpirableMap<K, V extends Expirable>
{
	protected final Map<K, V> expirableMap;
	
	public ExpirableMap(Map<K, V> expirableMap)
	{
		this.expirableMap = expirableMap;
	}
	
	protected void computeExpirable(Common plugin, K key, V expirable)
	{
		expirableMap.compute(key, (k, value) ->
		{
			if(value == null || !value.isValid(plugin))
			{
				return expirable;
			}
			
			value.incrementDuration(expirable.getDurationTick());
			return value;
		});
	}
}
