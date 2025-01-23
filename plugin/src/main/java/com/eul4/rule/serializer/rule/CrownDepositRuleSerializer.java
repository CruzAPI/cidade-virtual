package com.eul4.rule.serializer.rule;

import com.eul4.Main;
import com.eul4.StructureType;
import com.eul4.rule.Rule;
import com.eul4.rule.attribute.CrownDepositAttribute;
import com.eul4.rule.attribute.DepositAttribute;
import com.eul4.rule.attribute.GenericAttribute;
import com.eul4.rule.serializer.data.DepositAttributeDataSerializer;
import com.eul4.rule.serializer.data.GenericAttributeDataSerializer;
import org.bukkit.configuration.ConfigurationSection;

import java.io.FileNotFoundException;
import java.math.BigDecimal;

public class CrownDepositRuleSerializer extends GenericRuleSerializer
{
	public CrownDepositRuleSerializer(Main plugin)
	{
		super(plugin);
	}
	
	public Rule<CrownDepositAttribute> load() throws FileNotFoundException
	{
		return deserializeRule
		(
			loadConfig(plugin.getDataFileManager().getRuleFile(StructureType.CROWN_DEPOSIT)),
			CrownDepositAttribute.DEFAULT,
			this::deserializeAttribute
		);
	}
	
	public CrownDepositAttribute deserializeAttribute(ConfigurationSection section)
	{
		GenericAttribute.Data genericAttributeData = GenericAttributeDataSerializer.deserialize(section);
		DepositAttribute.Data<BigDecimal> depositAttributeData = DepositAttributeDataSerializer.deserialize(section, BigDecimal.class);
		
		return new CrownDepositAttribute(genericAttributeData, depositAttributeData);
	}
}
