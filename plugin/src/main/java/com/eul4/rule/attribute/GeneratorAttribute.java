package com.eul4.rule.attribute;

import com.google.common.base.Preconditions;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class GeneratorAttribute extends GenericAttribute
{
	private final int capacity;
	private final int delay;
	
	public GeneratorAttribute
	(
		GenericAttribute.Data genericAttributeData,
		GeneratorAttribute.Data generatorAttributeData
	)
	{
		super(genericAttributeData);
		
		this.capacity = generatorAttributeData.capacity;
		this.delay = generatorAttributeData.delay;
		
		Preconditions.checkArgument(delay > 0, "Generator delay must be greater than 0.");
	}
	
	@lombok.Data
	public static class Data
	{
		public static final Data DEFAULT = new Data(0, Integer.MAX_VALUE);
		
		private final int capacity;
		private final int delay;
	}
}
