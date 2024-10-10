package com.eul4.model.playerdata;

import com.eul4.wrapper.BroadcastHashSet;
import com.eul4.wrapper.Tag;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.jetbrains.annotations.Nullable;

@Data
@AllArgsConstructor
public class PluginPlayerData
{
	private Tag tag;
	private boolean tagHidden;
	private boolean newCombat;
	private BroadcastHashSet mutedBroadcasts;
	
	public PluginPlayerData(Tag tag)
	{
		this(tag, null, null, null);
	}
	
	@Builder
	public PluginPlayerData
	(
		Tag tag,
		@Nullable Boolean tagHidden,
		@Nullable Boolean newCombat,
		@Nullable BroadcastHashSet mutedBroadcasts
	)
	{
		this.tag = tag;
		this.tagHidden = tagHidden != null && tagHidden;
		this.newCombat = newCombat != null && newCombat;
		this.mutedBroadcasts = mutedBroadcasts == null ? new BroadcastHashSet() : mutedBroadcasts;
	}
}
