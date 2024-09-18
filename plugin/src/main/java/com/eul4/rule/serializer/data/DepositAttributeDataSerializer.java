package com.eul4.rule.serializer.data;

import com.eul4.rule.attribute.DepositAttribute;
import org.bukkit.configuration.ConfigurationSection;

import java.math.BigDecimal;

public class DepositAttributeDataSerializer
{
	public static <N extends Number> DepositAttribute.Data<N> deserialize(ConfigurationSection section, Class<N> type)
	{
		if(type == BigDecimal.class)
		{
			BigDecimal capacity = new BigDecimal(section.getString("capacity", "0"));
			return new DepositAttribute.Data<>(type.cast(capacity));
		}
		
		if(type == Integer.class)
		{
			int capacity = section.getInt("capacity");
			return new DepositAttribute.Data<>(type.cast(capacity));
		}
		
		throw new IllegalArgumentException("Unsupported number: " + type.getSimpleName());
	}
}
