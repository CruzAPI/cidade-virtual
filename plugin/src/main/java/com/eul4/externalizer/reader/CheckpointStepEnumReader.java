package com.eul4.externalizer.reader;

import com.eul4.common.exception.InvalidVersionException;
import com.eul4.common.externalizer.reader.ObjectReader;
import com.eul4.common.type.player.ObjectType;
import com.eul4.common.type.player.Readers;
import com.eul4.common.wrapper.Readable;
import com.eul4.common.wrapper.Reader;
import com.eul4.model.player.tutorial.step.CheckpointStepEnum;
import com.eul4.type.player.PluginObjectType;
import lombok.Getter;

import java.io.IOException;

@Getter
public class CheckpointStepEnumReader extends ObjectReader<CheckpointStepEnum>
{
	private final Reader<CheckpointStepEnum> reader;
	private final Readable<CheckpointStepEnum> readable;
	
	public CheckpointStepEnumReader(Readers readers) throws InvalidVersionException
	{
		super(readers, CheckpointStepEnum.class);
		
		final ObjectType objectType = PluginObjectType.CHECKPOINT_STEP_ENUM;
		final byte version = readers.getVersions().get(objectType);
		
		switch(version)
		{
		case 0:
			this.reader = Reader.identity();
			this.readable = this::readableVersion0;
			break;
		default:
			throw new InvalidVersionException("Invalid " + objectType + " version: " + version);
		}
	}
	
	private CheckpointStepEnum readableVersion0() throws IOException
	{
		return CheckpointStepEnum.valueOf(in.readUTF());
	}
	
	public CheckpointStepEnum readReference() throws IOException, ClassNotFoundException
	{
		return super.readReference(readable);
	}
}
