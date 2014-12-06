package com.sijobe.spc.asm;

import net.minecraft.launchwrapper.IClassTransformer;

public class Transformer implements IClassTransformer
{
	public Transformer()
	{
		Processor processor = Processor.getInstance();
		processor.registerMethodTransformers(MethodTransformer.generateFromFunctions(SimpleHooked.class));
		processor.registerMethodTransformer(new SlashPrefixer());
	}
	
	@Override
	public byte[] transform(String name, String transformedName, byte[] basicClass)
	{
		return Processor.getInstance().process(name, basicClass);
	}
}
