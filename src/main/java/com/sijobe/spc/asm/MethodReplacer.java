package com.sijobe.spc.asm;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Method;

import org.objectweb.asm.MethodVisitor;

/**
 * used to replace vanilla methods
 * 
 * @author aucguy
 * @version 1.0
 */
class MethodReplacer extends MethodHooker {
   /**
    * the MethodWriter instance. the normal 'mv' field is't used because if it were 'visit' methods would get called on it
    */
   protected MethodVisitor writer;
   
   MethodReplacer(String id, Method method) throws IllegalArgumentException {
      super(id, method);
   }
   
   /**
    * used to signify that a method is supposed to be used with a MethodReplacer
    */
   @Retention(RetentionPolicy.RUNTIME)
   @Target(ElementType.METHOD)
   @interface Hook {
      /**
       * the id of the vanilla method
       */
      String value();
   }
   
   @Override
   void injectMethodWriter(MethodVisitor mv) {
      this.writer = mv;
   }
   
   @Override
   protected MethodVisitor getWriter() {
      return this.writer;
   }
}
