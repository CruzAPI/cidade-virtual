package com.eul4.rule.attribute;

import com.eul4.StructureType;
import com.google.common.base.Preconditions;
import com.sk89q.worldedit.math.Vector3;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Map;

@Getter
@Setter
@ToString(callSuper = true)
public class TownHallAttribute extends GenericAttribute
{
	public static final TownHallAttribute DEFAULT = new TownHallAttribute
	(
		GenericAttribute.Data.DEFAULT,
		TownHallAttribute.Data.DEFAULT
	);
	
	private Map<StructureType, Integer> structureLimit;
	private int likeCapacity;
	private int dislikeCapacity;
	private BigDecimal crownCapacity;
	private Vector3 spawnPosition;
	
	public TownHallAttribute
	(
		GenericAttribute.Data genericAttributeData,
		TownHallAttribute.Data townHallAttributeData
	)
	{
		super(genericAttributeData);
		
		this.structureLimit = townHallAttributeData.structureLimit;
		this.likeCapacity = townHallAttributeData.likeCapacity;
		this.dislikeCapacity = townHallAttributeData.dislikeCapacity;
		this.crownCapacity = townHallAttributeData.crownCapacity;
		this.spawnPosition = Preconditions.checkNotNull(townHallAttributeData.spawnPosition, "spawnPosition is null");
	}
	
	@lombok.Data
	public static class Data
	{
		public static final Data DEFAULT = new Data(Collections.emptyMap(), 0, 0, BigDecimal.ZERO, Vector3.ZERO);
		
		private final Map<StructureType, Integer> structureLimit;
		private final int likeCapacity;
		private final int dislikeCapacity;
		private final BigDecimal crownCapacity;
		private final Vector3 spawnPosition;
	}
}
