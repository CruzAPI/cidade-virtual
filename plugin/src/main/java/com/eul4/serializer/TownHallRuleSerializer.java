package com.eul4.serializer;

import com.eul4.Main;
import com.eul4.StructureType;
import com.eul4.rule.Rule;
import com.eul4.rule.TownHallAttribute;
import org.bukkit.configuration.ConfigurationSection;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

public class TownHallRuleSerializer extends GenericRuleSerializer
{
	public TownHallRuleSerializer(Main plugin)
	{
		super(plugin);
	}
	
	public Rule<TownHallAttribute> load() throws FileNotFoundException
	{
		return deserializeRule(loadConfig(plugin.getDataFileManager()
				.getTownHallRuleFile()), TownHallAttribute.DEFAULT, this::deserializeAttribute);
	}
	
	private TownHallAttribute deserializeAttribute(ConfigurationSection section)
	{
		TownHallAttribute townHallAttribute = new TownHallAttribute();
		readExternal(townHallAttribute, section);
		return townHallAttribute;
	}
	
	private void readExternal(TownHallAttribute townHallAttribute, ConfigurationSection section)
	{
		super.readExternal(townHallAttribute, section);
		readStructureLimit(townHallAttribute, section);
		
		int likeCapacity = section.getInt("like_capacity");
		int dislikeCapacity = section.getInt("dislike_capacity");
		
		townHallAttribute.setLikeCapacity(likeCapacity);
		townHallAttribute.setDislikeCapacity(dislikeCapacity);
	}
	
	private void readStructureLimit(TownHallAttribute townHallAttribute, ConfigurationSection section)
	{
		ConfigurationSection structureLimitSection = section.getConfigurationSection("structure_limit");
		
		Map<StructureType<?, ?>, Integer> structureLimit = new HashMap<>();
		
		if(structureLimitSection != null)
		{
			for(String key : structureLimitSection.getKeys(false))
			{
				StructureType<?, ?> structureType = StructureType.valueOf(key);
				int limit = structureLimitSection.getInt(key);
				
				structureLimit.put(structureType, limit);
			}
		}
		
		townHallAttribute.setStructureLimit(structureLimit);
	}
}
