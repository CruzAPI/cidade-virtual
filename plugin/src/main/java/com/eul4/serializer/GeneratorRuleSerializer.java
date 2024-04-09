package com.eul4.serializer;

import com.eul4.Main;
import com.eul4.rule.GeneratorAttribute;
import com.eul4.rule.GenericAttribute;
import com.eul4.rule.Rule;
import lombok.RequiredArgsConstructor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.util.Vector;

import java.io.File;

@RequiredArgsConstructor
public class GeneratorRuleSerializer
{
	private final Main plugin;
	
	public Rule<GeneratorAttribute> load() throws NullPointerException
	{
		File file = plugin.getDataFileManager().getGeneratorRuleFile();
		
		if(!file.exists() || file.length() == 0L)
		{
			plugin.getLogger().warning("Generator rule file not found.");
			plugin.getLogger().warning("Loading empty generator rule instead.");
			return new Rule<>();
		}
		
		Rule<GeneratorAttribute> rule = new Rule<>();
		
		YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
		
		for(String key : config.getKeys(false))
		{
			int level = Integer.parseInt(key);
			ConfigurationSection section = config.getConfigurationSection(String.valueOf(level));
			
			GeneratorAttribute attributes = new GeneratorAttribute();
			
			GenericRuleSerializer.setGenericAttributes(attributes, section);
			
			final int capacity = section.getInt("capacity");
			final int delay = section.getInt("delay");
			
			attributes.setCapacity(capacity);
			attributes.setDelay(delay);
			
			rule.setRule(level, attributes);
		}
		
		return rule;
	}
}
