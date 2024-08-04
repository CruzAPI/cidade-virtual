package com.eul4.externalizer.writer;

import com.eul4.common.externalizer.writer.ObjectWriter;
import com.eul4.common.type.player.Writers;
import com.eul4.model.player.tutorial.step.CheckpointStepEnum;

import java.io.IOException;

public class CheckpointStepEnumWriter extends ObjectWriter<CheckpointStepEnum>
{
	public CheckpointStepEnumWriter(Writers writers)
	{
		super(writers, CheckpointStepEnum.class);
	}
	
	@Override
	protected void writeObject(CheckpointStepEnum checkpointStepEnum) throws IOException
	{
		out.writeUTF(checkpointStepEnum.name());
	}
}
