package com.eul4.wrapper;

import com.eul4.i18n.BroadcastRichMessage;
import lombok.experimental.Delegate;

import java.util.HashSet;

public class BroadcastHashSet
{
	@Delegate
	private final HashSet<BroadcastRichMessage> hashSet;
	
	public BroadcastHashSet()
	{
		this(new HashSet<>());
	}
	
	public BroadcastHashSet(HashSet<BroadcastRichMessage> hashSet)
	{
		this.hashSet = hashSet;
	}
}
