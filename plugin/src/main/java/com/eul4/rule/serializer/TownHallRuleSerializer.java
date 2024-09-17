package com.eul4.rule.serializer;

import com.eul4.Main;
import com.eul4.StructureType;
import com.eul4.rule.Rule;
import com.eul4.rule.attribute.TownHallAttribute;
import com.sk89q.worldedit.math.Vector3;
import org.bukkit.configuration.ConfigurationSection;

import java.io.FileNotFoundException;
import java.math.BigDecimal;
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
		BigDecimal crownCapacity = new BigDecimal(section.getString("crown_capacity", "0"));
		double spawnPositionX = section.getDouble("spawn_position.x");
		double spawnPositionY = section.getDouble("spawn_position.y");
		double spawnPositionZ = section.getDouble("spawn_position.z");
		Vector3 spawnPosition = Vector3.at(spawnPositionX, spawnPositionY, spawnPositionZ);
		
		townHallAttribute.setLikeCapacity(likeCapacity);
		townHallAttribute.setDislikeCapacity(dislikeCapacity);
		townHallAttribute.setCrownCapacity(crownCapacity);
		townHallAttribute.setSpawnPosition(spawnPosition);
	}
	
	private void readStructureLimit(TownHallAttribute townHallAttribute, ConfigurationSection section)
	{
		ConfigurationSection structureLimitSection = section.getConfigurationSection("structure_limit");
		
		Map<StructureType, Integer> structureLimit = new HashMap<>();
		
		if(structureLimitSection != null)
		{
			for(String key : structureLimitSection.getKeys(false))
			{
				StructureType structureType = StructureType.valueOf(key);
				int limit = structureLimitSection.getInt(key);
				
				structureLimit.put(structureType, limit);
			}
		}
		
		townHallAttribute.setStructureLimit(structureLimit);
	}
}
