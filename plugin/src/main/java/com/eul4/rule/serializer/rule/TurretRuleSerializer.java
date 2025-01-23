package com.eul4.rule.serializer.rule;

import com.eul4.Main;
import com.eul4.StructureType;
import com.eul4.rule.Rule;
import com.eul4.rule.attribute.GenericAttribute;
import com.eul4.rule.attribute.TurretAttribute;
import com.eul4.rule.serializer.data.GenericAttributeDataSerializer;
import com.eul4.rule.serializer.data.TurretAttributeDataSerializer;
import org.bukkit.configuration.ConfigurationSection;

import java.io.FileNotFoundException;

public class TurretRuleSerializer extends GenericRuleSerializer
{
	public TurretRuleSerializer(Main plugin)
	{
		super(plugin);
	}
	
	public Rule<TurretAttribute> load() throws FileNotFoundException
	{
		return deserializeRule
		(
			loadConfig(plugin.getDataFileManager().getRuleFile(StructureType.TURRET)),
			TurretAttribute.DEFAULT,
			this::deserializeAttribute
		);
	}
	
	public TurretAttribute deserializeAttribute(ConfigurationSection section)
	{
		GenericAttribute.Data genericAttributeData = GenericAttributeDataSerializer.deserialize(section);
		TurretAttribute.Data turretAttributeData = TurretAttributeDataSerializer.deserialize(section);
		
		return new TurretAttribute(genericAttributeData, turretAttributeData);
	}
}
