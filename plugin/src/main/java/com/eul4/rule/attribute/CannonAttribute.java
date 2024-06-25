package com.eul4.rule.attribute;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(callSuper = true)
public class CannonAttribute extends GenericAttribute
{
	public static final CannonAttribute DEFAULT = new CannonAttribute();
}
