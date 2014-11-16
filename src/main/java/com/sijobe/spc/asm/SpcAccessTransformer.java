package com.sijobe.spc.asm;

import java.io.IOException;

import cpw.mods.fml.common.asm.transformers.AccessTransformer;

public class SpcAccessTransformer extends AccessTransformer
{
	public SpcAccessTransformer() throws IOException
	{
		super("com/sijobe/spc/asm/spc_at.txt");
	}
}
