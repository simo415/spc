package com.sijobe.spc.asm;

import java.util.Map;
import java.util.HashMap;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

/**
 * used to transform a class
 * 
 * @author aucguy
 * @version 1.0
 */
class ClassTransformer extends ClassVisitor {
   /**
    * the binary name of the class this transformer modifies
    */
   protected String name;
   
   /**
    * binds method ids to methodTransformerss
    */
   protected Map<String, MethodTransformer> methodTransformers;
   
   /**
    * creates a ClassTransformer
    * 
    * @param name - the binary name of the class this transformer modifies
    */
   ClassTransformer(String name) {
      super(Opcodes.ASM4, new ClassWriter(Opcodes.ASM4));
      this.name = name;
      this.methodTransformers = new HashMap<String, MethodTransformer>();
   }
   
   /**
    * @return the binary name of the class this transformer modifies
    */
   String getApplicableClass() {
      return this.name;
   }
   
   /**
    * used to modify classes
    * 
    * @return this instance's ClassWriter
    */
   ClassWriter getWriter() {
      return (ClassWriter) this.cv;
   }
   
   @Override
   public MethodVisitor visitMethod(int access, String name, String desc,
         String signature, String[] exceptions) {
      MethodVisitor writer = super
            .visitMethod(access, name, desc, signature, exceptions);
      String id = this.getApplicableClass() + ":" + name + ":" + desc;
      
      if (this.methodTransformers.containsKey(id)) {
         MethodTransformer transformer = this.methodTransformers.get(id);
         transformer.injectMethodWriter(writer);
         return transformer;
      } else {
         return writer;
      }
   }
   
   /**
    * registers the given method transformer for the with this instance
    * 
    * @param mt - the methodTransformer to register
    * @throws IllegalArgumentExceptions - if the tranformer doesn't modify the a method of this instance's class
    */
   void registerMethodTransformer(MethodTransformer mt) throws IllegalArgumentException {
      String name = mt.getApplicableMethod();
      String clazz = name.split(":", 2)[0];
      if (!clazz.equals(this.getApplicableClass())) {
         throw (new IllegalArgumentException(
               "MethodTransformer not of correct class"));
      }
      this.methodTransformers.put(mt.getApplicableMethod(), mt);
   }
   
   /**
    * registers multiple methodTransformers
    * 
    * @param mt - the MethodTransformers to register
    * @throws IllegalArgumentException - if the tranformer doesn't modify the a method of this instance's class
    */
   void regsiterMethodTransformers(MethodTransformer[] mt) throws IllegalArgumentException {
      for (MethodTransformer i : mt) {
         this.registerMethodTransformer(i);
      }
   }
}
