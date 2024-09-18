package com.eul4.rule.serializer.rule;

import com.eul4.Main;
import com.eul4.rule.Rule;
import com.eul4.rule.attribute.GeneratorAttribute;
import com.eul4.rule.attribute.GenericAttribute;
import com.eul4.rule.attribute.LikeGeneratorAttribute;
import com.eul4.rule.serializer.data.GeneratorAttributeDataSerializer;
import com.eul4.rule.serializer.data.GenericAttributeDataSerializer;
import org.bukkit.configuration.ConfigurationSection;

import java.io.FileNotFoundException;

public class LikeGeneratorRuleSerializer extends GenericRuleSerializer
{
	public LikeGeneratorRuleSerializer(Main plugin)
	{
		super(plugin);
	}
	
	public Rule<LikeGeneratorAttribute> load() throws FileNotFoundException
	{
		return deserializeRule
		(
			loadConfig(plugin.getDataFileManager().getLikeGeneratorRuleFile()),
			LikeGeneratorAttribute.DEFAULT,
			this::deserializeAttribute
		);
	}
	
	private LikeGeneratorAttribute deserializeAttribute(ConfigurationSection section)
	{
		GenericAttribute.Data genericAttributeData = GenericAttributeDataSerializer.deserialize(section);
		GeneratorAttribute.Data generatorAttributeData = GeneratorAttributeDataSerializer.deserialize(section);
		
		return new LikeGeneratorAttribute(genericAttributeData, generatorAttributeData);
	}
}
