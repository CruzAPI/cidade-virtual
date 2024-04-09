package com.eul4.serializer;

import com.eul4.Main;
import com.eul4.rule.DislikeGeneratorAttribute;
import com.eul4.rule.Rule;
import org.bukkit.configuration.ConfigurationSection;

import java.io.FileNotFoundException;

public class DislikeGeneratorRuleSerializer extends GeneratorRuleSerializer
{
	public DislikeGeneratorRuleSerializer(Main plugin)
	{
		super(plugin);
	}
	
	public Rule<DislikeGeneratorAttribute> load() throws FileNotFoundException
	{
		return deserializeRule(loadConfig(plugin.getDataFileManager()
				.getDislikeGeneratorRuleFile()), this::deserializeAttribute);
	}
	
	public DislikeGeneratorAttribute deserializeAttribute(ConfigurationSection section)
	{
		DislikeGeneratorAttribute dislikeGeneratorAttribute = new DislikeGeneratorAttribute();
		readExternal(dislikeGeneratorAttribute, section);
		return dislikeGeneratorAttribute;
	}
	
	private void readExternal(DislikeGeneratorAttribute dislikeGeneratorAttribute, ConfigurationSection section)
	{
		super.readExternal(dislikeGeneratorAttribute, section);
	}
}
