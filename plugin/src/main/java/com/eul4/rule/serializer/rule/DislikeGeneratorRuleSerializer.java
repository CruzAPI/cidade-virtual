package com.eul4.rule.serializer.rule;

import com.eul4.Main;
import com.eul4.rule.Rule;
import com.eul4.rule.attribute.DislikeGeneratorAttribute;
import com.eul4.rule.attribute.GeneratorAttribute;
import com.eul4.rule.attribute.GenericAttribute;
import com.eul4.rule.serializer.data.GeneratorAttributeDataSerializer;
import com.eul4.rule.serializer.data.GenericAttributeDataSerializer;
import org.bukkit.configuration.ConfigurationSection;

import java.io.FileNotFoundException;

public class DislikeGeneratorRuleSerializer extends GenericRuleSerializer
{
	public DislikeGeneratorRuleSerializer(Main plugin)
	{
		super(plugin);
	}
	
	public Rule<DislikeGeneratorAttribute> load() throws FileNotFoundException
	{
		return deserializeRule
		(
			loadConfig(plugin.getDataFileManager().getDislikeGeneratorRuleFile()),
			DislikeGeneratorAttribute.DEFAULT,
			this::deserializeAttribute
		);
	}
	
	public DislikeGeneratorAttribute deserializeAttribute(ConfigurationSection section)
	{
		GenericAttribute.Data genericAttributeData = GenericAttributeDataSerializer.deserialize(section);
		GeneratorAttribute.Data generatorAttributeData = GeneratorAttributeDataSerializer.deserialize(section);
		
		return new DislikeGeneratorAttribute(genericAttributeData, generatorAttributeData);
	}
}
