package com.eul4.rule;

import com.eul4.StructureType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Map;

@Getter
@Setter
@ToString(callSuper = true)
public class TownHallAttribute extends GenericAttribute
{
	private Map<StructureType, Integer> structureLimit;
	private int likeCapacity;
	private int dislikeCapacity;
}
