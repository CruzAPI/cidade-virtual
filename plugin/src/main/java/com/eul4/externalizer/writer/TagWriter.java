package com.eul4.externalizer.writer;

import com.eul4.common.externalizer.writer.ObjectWriter;
import com.eul4.common.type.player.Writers;
import com.eul4.wrapper.Tag;

import java.io.IOException;

public class TagWriter extends ObjectWriter<Tag>
{
	public TagWriter(Writers writers)
	{
		super(writers, Tag.class);
	}
	
	@Override
	protected void writeObject(Tag tag) throws IOException
	{
		out.writeUTF(tag.name());
	}
}
