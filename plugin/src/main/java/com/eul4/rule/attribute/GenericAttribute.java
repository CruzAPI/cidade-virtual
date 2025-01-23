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
	private final Price price;
	private final double hp;
	private final int requiresTownHallLevel;
	private final int totalBuildTicks;
	private final Vector hologramVector;
	
	protected GenericAttribute(GenericAttribute.Data genericAttributeData)
	{
		this.price = genericAttributeData.price;
		this.hp = genericAttributeData.hp;
		this.requiresTownHallLevel = genericAttributeData.requiresTownHallLevel;
		this.totalBuildTicks = genericAttributeData.totalBuildTicks;
		this.hologramVector = genericAttributeData.hologramVector;
	}
	
	@lombok.Data
	public static class Data
	{
		public static final Data DEFAULT = new Data(null, 100.0D, Integer.MAX_VALUE, Integer.MAX_VALUE, new Vector());
		
		private final Price price;
		private final double hp;
		private final int requiresTownHallLevel;
		private final int totalBuildTicks;
		private final Vector hologramVector;
	}
}
