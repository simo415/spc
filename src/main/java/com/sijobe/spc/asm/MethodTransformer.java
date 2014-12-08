package com.sijobe.spc.asm;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.LinkedList;
import java.util.List;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

/**
 * used to transform methods
 * 
 * @author aucguy
 * @version 1.0
 */
abstract class MethodTransformer extends MethodVisitor {
   
   /**
    * the method id of this transformer a method id is in the format 'class:method:descriptor'
    */
   String id;
   
   /**
    * constructs a MethodTransformer
    * 
    * @param id - the id for this instance
    */
   MethodTransformer(String id) {
      super(Opcodes.ASM4);
      this.id = id;
   }
   
   /**
    * gets teh applicable method
    * 
    * @return the id of the method that this transformer modifies
    */
   String getApplicableMethod() {
      return this.id;
   }
   
   /**
    * gives this MethodTrasnformer the method writer instance
    * 
    * @param mv - the MethodWriter created for this instance. Made just before this instance will visit things
    */
   abstract void injectMethodWriter(MethodVisitor mv);
   
   /**
    * generates an array of MethodTransformers from a class of annotations. This allows for easy use of ASM modifications See com.sijobe.asm.SimpleHooked for an example.
    * 
    * @param clazz - the class containing the modification methods.
    * @return an array of MethodTransformers that were generated
    */
   static MethodTransformer[] generateFromFunctions(Class<?> clazz) {
      List<MethodTransformer> modifiers = new LinkedList<MethodTransformer>();
      for (Method method : clazz.getDeclaredMethods()) {
         if (Modifier.isStatic(method.getModifiers())) {
            if (method.isAnnotationPresent(MethodReplacer.Hook.class)) {
               String annotation = method
                     .getAnnotation(MethodReplacer.Hook.class).value();
               System.out.println("found replacement for " + annotation);
               modifiers.add(new MethodReplacer(annotation, method));
            } else if (method.isAnnotationPresent(MethodPrefixer.Hook.class)) {
               String annotation = method
                     .getAnnotation(MethodPrefixer.Hook.class).value();
               System.out.println("found prefix for " + annotation);
               modifiers.add(new MethodPrefixer(annotation, method));
            }
         }
      }
      
      MethodTransformer[] r = new MethodTransformer[modifiers.size()];
      return modifiers.toArray(r);
   }
}
