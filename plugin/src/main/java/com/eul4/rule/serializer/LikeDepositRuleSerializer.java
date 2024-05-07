package com.eul4.rule.serializer;

import com.eul4.Main;
import com.eul4.StructureType;
import com.eul4.rule.Rule;
import com.eul4.rule.attribute.LikeDepositAttribute;
import org.bukkit.configuration.ConfigurationSection;

import java.io.FileNotFoundException;

public class LikeDepositRuleSerializer extends DepositRuleSerializer
{
	public LikeDepositRuleSerializer(Main plugin)
	{
		super(plugin);
	}
	
	public Rule<LikeDepositAttribute> load() throws FileNotFoundException
	{
		return deserializeRule(loadConfig(plugin.getDataFileManager()
				.getRuleFile(StructureType.LIKE_DEPOSIT)), LikeDepositAttribute.DEFAULT, this::deserializeAttribute);
	}
	
	public LikeDepositAttribute deserializeAttribute(ConfigurationSection section)
	{
		LikeDepositAttribute likeDepositAttribute = new LikeDepositAttribute();
		readExternal(likeDepositAttribute, section);
		return likeDepositAttribute;
	}
	
	private void readExternal(LikeDepositAttribute likeDepositAttribute, ConfigurationSection section)
	{
		super.readExternal(likeDepositAttribute, section);
	}
}
