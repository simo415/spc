package com.sijobe.spc.asm;

import org.objectweb.asm.ClassVisitor;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;

import net.minecraft.launchwrapper.IClassTransformer;

public class Transformer implements IClassTransformer
{
	public Transformer()
	{
		Processor.getInstance().registerMethodTransformers(MethodTransformer.generateFromFunctions(SimpleHooked.class));
	}
	
	@Override
	public byte[] transform(String name, String transformedName, byte[] basicClass)
	{
		return Processor.getInstance().process(name, basicClass);
	}
}
