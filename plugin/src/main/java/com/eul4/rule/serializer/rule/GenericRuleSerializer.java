package com.eul4.rule.serializer.rule;

import com.eul4.Main;
import com.eul4.rule.Rule;
import com.eul4.rule.attribute.GenericAttribute;
import lombok.RequiredArgsConstructor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.function.Function;

@RequiredArgsConstructor
public abstract class GenericRuleSerializer
{
	protected final Main plugin;
	
	public YamlConfiguration loadConfig(File file) throws FileNotFoundException
	{
		if(!file.exists() || file.length() == 0L)
		{
			throw new FileNotFoundException(file.getName() + " file not found.");
		}
		
		return YamlConfiguration.loadConfiguration(file);
	}
	
	public <A extends GenericAttribute> Rule<A> deserializeRule
	(
		YamlConfiguration config,
		A defaultAttribute,
		Function<ConfigurationSection, A> function
	)
	{
		Rule<A> rule = new Rule<>(defaultAttribute);
		
		for(String key : config.getKeys(false))
		{
			int level = Integer.parseInt(key);
			ConfigurationSection section = config.getConfigurationSection(String.valueOf(level));
			
			rule.setRule(level, function.apply(section));
		}
		
		return rule;
	}
}
