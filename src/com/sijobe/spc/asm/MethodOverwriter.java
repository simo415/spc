package com.sijobe.spc.asm;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class MethodOverwriter extends MethodVisitor
{
	static Set<String> clazzes;
	static Map<String, Method> replacers;
	
	static void init()
	{
		clazzes = new HashSet<String>();
		replacers = new HashMap<String, Method>();
		for(Method method : MethodOverwriter.class.getDeclaredMethods())
		{
			if(method.isAnnotationPresent(Replacer.class))
			{
				String annotation = method.getAnnotation(Replacer.class).value();
				System.out.println("found replacement for "+annotation);
				String clazz = annotation.split(":", 2)[0];
				if(!clazzes.contains(clazz))
				{
					clazzes.add(clazz);
				}
				replacers.put(annotation, method);
			}
		}
	}
	
	MethodVisitor writer;
	String id;
	
	public MethodOverwriter(MethodVisitor writer, String id)
	{
		super(Opcodes.ASM4);
		this.writer = writer;
		this.id = id;
	}
	
	@Override
	public void visitCode()
	{
		try {
			replacers.get(this.id).invoke(this);
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Replacer("net.minecraft.client.multiplayer.PlayerControllerMP:getBlockReachDistance:()F")
	protected void getBlockReachDistance()
	{
		MethodVisitor mv = this.writer;
		mv.visitCode();
		mv.visitFieldInsn(Opcodes.GETSTATIC, "com/sijobe/spc/command/BlockReach", "reachDistance", "F");
		mv.visitInsn(Opcodes.FRETURN);
		mv.visitMaxs(1, 1);
		mv.visitEnd();
	}
}
