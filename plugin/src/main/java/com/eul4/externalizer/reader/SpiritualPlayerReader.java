package com.eul4.externalizer.reader;

import com.eul4.common.exception.InvalidVersionException;
import com.eul4.common.type.player.ObjectType;
import com.eul4.common.type.player.Readers;
import com.eul4.common.wrapper.Reader;
import com.eul4.model.player.SpiritualPlayer;
import com.eul4.type.player.PhysicalPlayerType;
import com.eul4.type.player.PluginObjectType;
import lombok.Getter;

import java.io.IOException;

@Getter
public abstract non-sealed class SpiritualPlayerReader<P extends SpiritualPlayer> extends PluginPlayerReader<P>
{
	private final Reader<P> reader;
	
	public SpiritualPlayerReader(Readers readers, Class<P> type) throws InvalidVersionException
	{
		super(readers, type);
		
		final ObjectType objectType = PluginObjectType.SPIRITUAL_PLAYER;
		final byte version = readers.getVersions().get(objectType);
		
		switch(version)
		{
		case 0:
			this.reader = this::readerVersion0;
			break;
		default:
			throw new InvalidVersionException("Invalid " + objectType + " version: " + version);
		}
	}
	
	private void readerVersion0(P spiritualPlayer) throws IOException, ClassNotFoundException
	{
		super.getReader().readObject(spiritualPlayer);
		
		spiritualPlayer.setReincarnationType(PhysicalPlayerType.values()[in.readInt()]);
	}
}
