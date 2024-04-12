package com.eul4.rule.attribute;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class GeneratorAttribute extends GenericAttribute
{
	private int capacity;
	private int delay;
}
