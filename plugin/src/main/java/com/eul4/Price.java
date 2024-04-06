package com.eul4;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Price implements Externalizable
{
	private int likes;
	private int dislikes;
	
	@Override
	public void writeExternal(ObjectOutput out) throws IOException
	{
		out.writeInt(likes);
		out.writeInt(dislikes);
	}
	
	@Override
	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException
	{
		likes = in.readInt();
		dislikes = in.readInt();
	}
}
