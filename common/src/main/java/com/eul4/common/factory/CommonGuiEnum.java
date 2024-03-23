package com.eul4.common.factory;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum CommonGuiEnum implements GuiEnum
{
	;
	private final GuiInstantiation instantiation;
}
