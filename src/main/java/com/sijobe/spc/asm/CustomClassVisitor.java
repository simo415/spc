package com.sijobe.spc.asm;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Opcodes;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;

public class CustomClassVisitor extends ClassVisitor
{
	String clazz;
	
	public CustomClassVisitor(String clazz, ClassWriter writer)
	{
		super(Opcodes.ASM4, writer);
		this.clazz = clazz;
	}
	
	@Override
	public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions)
	{
		MethodVisitor mv = super.visitMethod(access, name, desc, signature, exceptions);
		String id = this.clazz+":"+name+":"+desc;
		if(MethodOverwriter.replacers.containsKey(id))
		{
			return (MethodVisitor) new MethodOverwriter(mv, id);
		}
		else
		{
			return mv;
		}
	}
}
