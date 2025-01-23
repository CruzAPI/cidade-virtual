package com.eul4.rule.serializer.data;

import com.eul4.rule.attribute.GeneratorAttribute;
import org.bukkit.configuration.ConfigurationSection;

public class GeneratorAttributeDataSerializer
{
	public static GeneratorAttribute.Data deserialize(ConfigurationSection section)
	{
		final int capacity = section.getInt("capacity");
		final int delay = section.getInt("delay");
		
		return new GeneratorAttribute.Data(capacity, delay);
	}
}
