package com.eul4.rule.attribute;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(callSuper = true)
public class DislikeGeneratorAttribute extends GeneratorAttribute
{
	public static final DislikeGeneratorAttribute DEFAULT = new DislikeGeneratorAttribute
	(
		GenericAttribute.Data.DEFAULT,
		GeneratorAttribute.Data.DEFAULT
	);
	
	public DislikeGeneratorAttribute
	(
		GenericAttribute.Data genericAttributeData,
		GeneratorAttribute.Data generatorAttributeData
	)
	{
		super(genericAttributeData, generatorAttributeData);
	}
}
