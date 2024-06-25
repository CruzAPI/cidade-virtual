package com.eul4.rule.serializer;

import com.eul4.Main;
import com.eul4.StructureType;
import com.eul4.rule.Rule;
import com.eul4.rule.attribute.CannonAttribute;
import org.bukkit.configuration.ConfigurationSection;

import java.io.FileNotFoundException;

public class CannonRuleSerializer extends GenericRuleSerializer
{
	public CannonRuleSerializer(Main plugin)
	{
		super(plugin);
	}
	
	public Rule<CannonAttribute> load() throws FileNotFoundException
	{
		return deserializeRule(
				loadConfig(plugin.getDataFileManager().getRuleFile(StructureType.CANNON)),
				CannonAttribute.DEFAULT,
				this::deserializeAttribute);
	}
	
	public CannonAttribute deserializeAttribute(ConfigurationSection section)
	{
		CannonAttribute cannonAttribute = new CannonAttribute();
		readExternal(cannonAttribute, section);
		return cannonAttribute;
	}
	
	private void readExternal(CannonAttribute cannonAttribute, ConfigurationSection section)
	{
		super.readExternal(cannonAttribute, section);
	}
}
