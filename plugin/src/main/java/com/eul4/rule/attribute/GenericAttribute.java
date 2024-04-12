package com.eul4.rule.attribute;

import com.eul4.Price;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.bukkit.util.Vector;

@Getter
@Setter
@ToString
public abstract class GenericAttribute
{
	private Price price;
	private int requiresTownHallLevel;
	private int totalBuildTicks;
	private Vector hologramVector;
}
