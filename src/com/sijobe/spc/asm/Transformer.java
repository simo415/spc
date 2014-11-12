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
		MethodOverwriter.init();
	}
	
	@Override
	public byte[] transform(String name, String transformedName, byte[] basicClass)
	{
		if(MethodOverwriter.clazzes.contains(name)) //TODO do obfucated names
		{
			System.out.println("modifiying "+name);
			return this.applyPatch(name, basicClass); 
		}
		else
		{
			return basicClass;
		}
	}
	
	protected byte[] applyPatch(String name, byte[] orginal)
	{
		ClassWriter writer = new ClassWriter(Opcodes.ASM4);
		ClassVisitor visitor = new CustomClassVisitor(name, writer);
		ClassReader reader = new ClassReader(orginal);
		reader.accept(visitor, 0);
		return writer.toByteArray();
	}
}
