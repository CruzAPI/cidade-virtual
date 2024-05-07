package com.eul4.common.externalizer.reader;

import com.eul4.common.Common;
import com.eul4.common.exception.InvalidVersionException;
import com.eul4.common.model.data.CommonPlayerData;
import com.eul4.common.model.player.CommonPlayer;
import com.eul4.common.wrapper.CommonVersions;
import com.eul4.common.wrapper.Reader;

import java.io.IOException;
import java.io.ObjectInput;
import java.util.function.Supplier;

public class CommonPlayerReader<P extends CommonPlayer> extends ObjectReader<P>
{
	private final Reader<P> reader;
	private final Supplier<P> supplier;
	
	private final CommonPlayerDataReader commonPlayerDataReader;
	
	private P instance;
	
	public CommonPlayerReader(ObjectInput in, CommonVersions commonVersions, Common plugin, Supplier<P> supplier) throws InvalidVersionException
	{
		super(in, commonVersions);
		
		this.commonPlayerDataReader = new CommonPlayerDataReader(in, commonVersions, plugin);
		this.supplier = supplier;
		
		if(commonVersions.getCommonPlayerVersion() == 0)
		{
			this.reader = this::readerVersion0;
		}
		else
		{
			throw new InvalidVersionException("Invalid CommonPlayer version: " + commonVersions.getCommonPlayerVersion());
		}
	}
	
	private P readerVersion0() throws IOException, ClassNotFoundException
	{
		P commonPlayer = supplier.get();
		
		CommonPlayerData commonPlayerData = commonPlayerDataReader.readReference();
		
		commonPlayer.setCommonPlayerData(commonPlayerData);
		
		return commonPlayer;
	}
	
	public P getInstance()
	{
		return instance = (instance == null ? supplier.get() : instance);
	}
	
	@Override
	protected P readObject() throws IOException, ClassNotFoundException
	{
		return reader.readObject();
	}
}
