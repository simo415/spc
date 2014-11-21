package com.sijobe.spc.asm;

import java.util.Map;
import java.util.HashMap;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;

class Processor
{
	private static Processor instance = new Processor();
	
	static Processor getInstance()
	{
		return instance;
	}
	
	protected Map<String, ClassTransformer> classTransformers;
	
	Processor()
	{
		this.classTransformers = new HashMap<String, ClassTransformer>();
	}
	
	byte[] process(String name, byte[] data)
	{
		if(this.classTransformers.containsKey(name))
		{
			ClassTransformer transformer = this.classTransformers.get(name);
			this.classTransformers.remove(name);
			ClassReader reader = new ClassReader(data);
			reader.accept(transformer, 0);
			return transformer.getWriter().toByteArray();
		}
		else
		{
			return data;
		}
	}
	
	void registerClassTransformer(ClassTransformer ct)
	{
		this.classTransformers.put(ct.getApplicableClass(), ct);
	}
	
	void registerMethodTransformer(MethodTransformer mt)
	{
		String id = mt.getApplicableMethod();
		String clazz = id.split(":", 2)[0];
		if(!this.classTransformers.containsKey(clazz))
		{
			this.classTransformers.put(clazz, new ClassTransformer(clazz));
		}
		this.classTransformers.get(clazz).registerMethodTransformer(mt);
	}
	
	void registerMethodTransformers(MethodTransformer[] mt)
	{
		for(MethodTransformer i : mt)
		{
			this.registerMethodTransformer(i);
		}
	}
}
