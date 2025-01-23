package com.eul4.rule.serializer.rule;

import com.eul4.Main;
import com.eul4.rule.Rule;
import com.eul4.rule.attribute.GenericAttribute;
import com.eul4.rule.attribute.TownHallAttribute;
import com.eul4.rule.serializer.data.GenericAttributeDataSerializer;
import com.eul4.rule.serializer.data.TownHallAttributeDataSerializer;
import org.bukkit.configuration.ConfigurationSection;

import java.io.FileNotFoundException;

public class TownHallRuleSerializer extends GenericRuleSerializer
{
	public TownHallRuleSerializer(Main plugin)
	{
		super(plugin);
	}
	
	public Rule<TownHallAttribute> load() throws FileNotFoundException
	{
		return deserializeRule
		(
			loadConfig(plugin.getDataFileManager().getTownHallRuleFile()),
			TownHallAttribute.DEFAULT,
			this::deserializeAttribute
		);
	}
	
	private TownHallAttribute deserializeAttribute(ConfigurationSection section)
	{
		GenericAttribute.Data genericAttributeData = GenericAttributeDataSerializer.deserialize(section);
		TownHallAttribute.Data townHallAttributeData = TownHallAttributeDataSerializer.deserialize(section);
		
		return new TownHallAttribute(genericAttributeData, townHallAttributeData);
	}
}
