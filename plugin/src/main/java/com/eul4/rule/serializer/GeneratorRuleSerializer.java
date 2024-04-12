package com.eul4.rule.serializer;

import com.eul4.Main;
import com.eul4.rule.attribute.GeneratorAttribute;
import org.bukkit.configuration.ConfigurationSection;

public abstract class GeneratorRuleSerializer extends GenericRuleSerializer
{
	public GeneratorRuleSerializer(Main plugin)
	{
		super(plugin);
	}
	
	public void readExternal(GeneratorAttribute generatorAttribute, ConfigurationSection section)
	{
		super.readExternal(generatorAttribute, section);
		
		final int capacity = section.getInt("capacity");
		final int delay = section.getInt("delay");
		
		if(delay <= 0)
		{
			throw new IllegalArgumentException("Generator delay must be greater than 0.");
		}
		
		generatorAttribute.setCapacity(capacity);
		generatorAttribute.setDelay(delay);
	}
}
