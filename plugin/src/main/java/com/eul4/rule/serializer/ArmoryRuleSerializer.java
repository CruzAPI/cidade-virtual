package com.eul4.rule.serializer;

import com.eul4.Main;
import com.eul4.StructureType;
import com.eul4.rule.Rule;
import com.eul4.rule.attribute.ArmoryAttribute;
import org.bukkit.configuration.ConfigurationSection;

import java.io.FileNotFoundException;

public class ArmoryRuleSerializer extends GenericRuleSerializer
{
	public ArmoryRuleSerializer(Main plugin)
	{
		super(plugin);
	}
	
	public Rule<ArmoryAttribute> load() throws FileNotFoundException
	{
		return deserializeRule(
				loadConfig(plugin.getDataFileManager().getRuleFile(StructureType.ARMORY)),
				ArmoryAttribute.DEFAULT,
				this::deserializeAttribute);
	}
	
	public ArmoryAttribute deserializeAttribute(ConfigurationSection section)
	{
		ArmoryAttribute armoryAttribute = new ArmoryAttribute();
		readExternal(armoryAttribute, section);
		return armoryAttribute;
	}
	
	private void readExternal(ArmoryAttribute armoryAttribute, ConfigurationSection section)
	{
		super.readExternal(armoryAttribute, section);
	}
}
