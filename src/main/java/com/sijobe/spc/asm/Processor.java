package com.sijobe.spc.asm;

import java.util.Map;
import java.util.HashMap;

import org.objectweb.asm.ClassReader;

/**
 * Give it some bytecode and it will modify that class with the previously given
 * transformers
 * 
 * @author aucguy
 * @version 1.0
 */
class Processor {
   /**
    * the instance
    */
   private static Processor instance = new Processor();
   
   /**
    * how to get the processor instance
    * 
    * @return the Processor instance
    */
   static Processor getInstance() {
      return instance;
   }
   
   /**
    * obfuscated name storage
    */
   public ObfuscationData mappings;
   public ObfuscationData reversedMappings;
   
   /**
    * maps class binary names to classTransformers
    */
   protected Map<String, ClassTransformer> classTransformers;
   
   /**
    * whether or not the class files are obfuscated
    */
   public boolean obfuscated;
   
   /**
    * makes a Processor
    */
   Processor() {
      this.classTransformers = new HashMap<String, ClassTransformer>();
      this.mappings = new ObfuscationData();
      this.mappings.loadMappings();
      this.reversedMappings = this.mappings.reverse();
   }
   
   /**
    * modifies the given bytecode with previously given Transformers
    * 
    * @param name - the binary name of the class
    * @param data - the class's bytecode
    * @return the modified bytecode
    */
   byte[] process(String name, byte[] data) {
      if (this.classTransformers.containsKey(name)) {
         ClassTransformer transformer = this.classTransformers.get(name);
         this.classTransformers.remove(name);
         ClassReader reader = new ClassReader(data);
         try {
            reader.accept(transformer, 0);
         } catch (RuntimeException error) {
            error.printStackTrace();
            throw(error);
         }
         return transformer.getWriter().toByteArray();
      } else {
         return data;
      }
   }
   
   /**
    * registers a classTransformer with this Processor
    * 
    * @param ct - the classTransformer to register
    */
   void registerClassTransformer(ClassTransformer ct) {
      this.classTransformers.put(ct.getApplicableClass(), ct);
   }
   
   /**
    * registers the given method transformer with whatever classTransformer it
    * goes with
    * 
    * @param mt - the methodTransformer to register
    */
   void registerMethodTransformer(MethodTransformer mt) {
      String id = mt.getApplicableMethod();
      System.out.println("registering method transformer " + id);
      String clazz = id.split(":", 2)[0];
      if (!this.classTransformers.containsKey(clazz)) {
         this.classTransformers.put(clazz, new ClassTransformer(clazz));
      }
      this.classTransformers.get(clazz).registerMethodTransformer(mt);
   }
   
   /**
    * registers multiple method transformers
    * 
    * @param mt - the methodTransformers to register
    */
   void registerMethodTransformers(MethodTransformer[] mt) {
      for (MethodTransformer i : mt) {
         this.registerMethodTransformer(i);
      }
   }
}
