package com.eul4.externalizer.writer;

import com.eul4.common.externalizer.writer.ObjectWriter;
import com.eul4.common.type.player.Writers;
import com.eul4.wrapper.StabilityFormula;

import java.io.IOException;

public class StabilityFormulaWriter extends ObjectWriter<StabilityFormula>
{
	public StabilityFormulaWriter(Writers writers)
	{
		super(writers, StabilityFormula.class);
	}
	
	@Override
	protected void writeObject(StabilityFormula stabilityFormula) throws IOException
	{
		boolean stable = stabilityFormula.isStable();
		
		out.writeBoolean(stable);
		
		if(!stable)
		{
			out.writeFloat(stabilityFormula.getBase());
			out.writeFloat(stabilityFormula.getBaseMultiplier());
			out.writeFloat(stabilityFormula.getEnchantBase());
			out.writeFloat(stabilityFormula.getEnchantBaseMultiplier());
		}
	}
}
