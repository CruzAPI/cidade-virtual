package com.eul4.serializer;

import com.eul4.Main;
import com.eul4.rule.LikeGeneratorAttribute;
import com.eul4.rule.Rule;
import org.bukkit.configuration.ConfigurationSection;

import java.io.FileNotFoundException;

public class LikeGeneratorRuleSerializer extends GeneratorRuleSerializer
{
	public LikeGeneratorRuleSerializer(Main plugin)
	{
		super(plugin);
	}
	
	public Rule<LikeGeneratorAttribute> load() throws FileNotFoundException
	{
		return deserializeRule(loadConfig(plugin.getDataFileManager()
				.getLikeGeneratorRuleFile()), LikeGeneratorAttribute.DEFAULT, this::deserializeAttribute);
	}
	
	private LikeGeneratorAttribute deserializeAttribute(ConfigurationSection section)
	{
		LikeGeneratorAttribute likeGeneratorAttribute = new LikeGeneratorAttribute();
		super.readExternal(likeGeneratorAttribute, section);
		return likeGeneratorAttribute;
	}
}
