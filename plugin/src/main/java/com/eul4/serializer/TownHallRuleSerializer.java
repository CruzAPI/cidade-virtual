package com.eul4.serializer;

import com.eul4.Main;
import com.eul4.StructureType;
import com.eul4.rule.Rule;
import com.eul4.rule.TownHallAttribute;
import lombok.RequiredArgsConstructor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
public class TownHallRuleSerializer
{
	private final Main plugin;
	
	public Rule<TownHallAttribute> load() throws NullPointerException
	{
		File file = plugin.getDataFileManager().getTownHallRuleFile();
		
		if(!file.exists() || file.length() == 0L)
		{
			plugin.getLogger().warning("TownHall rule file not found.");
			plugin.getLogger().warning("Loading empty TownHall rule instead.");
			return new Rule<>();
		}
		
		Rule<TownHallAttribute> rule = new Rule<>();
		
		YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
		
		for(String key : config.getKeys(false))
		{
			int level = Integer.parseInt(key);
			ConfigurationSection section = config.getConfigurationSection(String.valueOf(level));
			
			TownHallAttribute attributes = new TownHallAttribute();
			
			GenericRuleSerializer.setGenericAttributes(attributes, section);
			
			setStructureLimit(attributes, config.getConfigurationSection("structure_limit"));
			
			rule.setRule(level, attributes);
		}
		
		return rule;
	}
	
	private void setStructureLimit(TownHallAttribute townHallAttribute, ConfigurationSection section)
	{
		Map<StructureType, Integer> structureLimit = new HashMap<>();
		
		if(section != null)
		{
			for(String key : section.getKeys(false))
			{
				StructureType structureType = StructureType.valueOf(key);
				int limit = section.getInt(key);
				
				structureLimit.put(structureType, limit);
			}
		}
		
		townHallAttribute.setStructureLimit(structureLimit);
	}
}
