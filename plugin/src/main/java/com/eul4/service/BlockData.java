package com.eul4.service;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Getter
@Setter
@ToString
public class BlockData implements Serializable
{
	@Accessors(fluent = true)
	private boolean hasHardness;
}
