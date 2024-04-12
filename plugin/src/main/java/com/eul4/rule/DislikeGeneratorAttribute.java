package com.eul4.rule;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(callSuper = true)
public class DislikeGeneratorAttribute extends GeneratorAttribute
{
	public static final DislikeGeneratorAttribute DEFAULT = new DislikeGeneratorAttribute();
}
