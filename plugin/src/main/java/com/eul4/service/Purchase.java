package com.eul4.service;

import com.eul4.Price;
import com.eul4.common.model.player.CommonPlayer;
import com.eul4.function.Execution;
import com.eul4.model.town.Town;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class Purchase
{
	private final CommonPlayer commonPlayer;
	private final Town town;
	private final Price price;
	private final Execution execution;
}
