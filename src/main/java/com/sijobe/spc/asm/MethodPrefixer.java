package com.sijobe.spc.asm;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Method;

import org.objectweb.asm.MethodVisitor;

/**
 * used to prefix vanilla methods with custom code
 * 
 * @author aucguy
 * @version 1.0
 */
class MethodPrefixer extends MethodHooker {
   /**
    * constructs a MethodPrefixer
    * 
    * @param id - the id of the vanilla method
    * @param method - the method that calls the necessary 'visit' methods
    * @throws IllegalArgumentException - if the method is not static
    */
   MethodPrefixer(String id, Method method) throws IllegalArgumentException {
      super(id, method);
   }
   
   /**
    * used to signify that a method is supposed to be used with a MethodPrefixer
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
      this.mv = mv;
   }
   
   @Override
   protected MethodVisitor getWriter() {
      return this.mv;
   }
}
