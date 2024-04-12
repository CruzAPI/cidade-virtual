package com.eul4.rule.attribute;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(callSuper = true)
public class LikeGeneratorAttribute extends GeneratorAttribute
{
	public static final LikeGeneratorAttribute DEFAULT = new LikeGeneratorAttribute();
}
