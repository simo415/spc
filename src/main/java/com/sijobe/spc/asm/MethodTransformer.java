package com.sijobe.spc.asm;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

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
    * the method id of this transformer a method id is in the format
    * 'class:method:descriptor'
    */
   String id;
   
   /**
    * constructs a MethodTransformer
    * 
    * @param id - the id for this instance
    */
   MethodTransformer(String id) {
      super(Opcodes.ASM4);
      this.id = this.convertId(id);
   }
   
   /**
    * converts the given development-named id to the obfuscated id if necessary.
    * 
    * @param id - the unobfuscated id
    */
   protected String convertId(String id) {
      if(Processor.getInstance().obfuscated) {
         Map<String, String> mappings = Processor.getInstance().mappings;
         String[] parts = id.split(":");
         String oldClass = parts[0];
         String oldMethod = parts[1];
         String oldDescriptor = parts[2];
         String newClass = mappings.get(oldClass);
         String newMethod = mappings.get(oldClass + "." + oldMethod);
         
         String[] partsDesc = oldDescriptor.split("\\)");
         String oldArgs = partsDesc[0].replace("(", "");
         String oldReturn = partsDesc[1];
         
         String newArgs = "";
         for(int i=0; i < oldArgs.length(); i++) {
            if(oldArgs.charAt(i) == 'L') {
               i++;
               String arg = "";
               for(; oldArgs.charAt(i) != ';'; i++) {
                  arg += oldArgs.charAt(i);
               }
               newArgs += "L" + mappings.get(arg.replace('/', '.')) + ";";
            }
            else {
               newArgs += oldArgs.charAt(i);
            }
         }
         
         String newReturn;
         if(oldReturn.startsWith("L")) {
            newReturn = "L"+mappings.get(oldReturn.substring(1, oldReturn.length()-1).replace('/', '.'))+";";
         }
         else {
            newReturn = oldReturn;
         }
         
         String newId = newClass+":"+newMethod+":("+newArgs+")"+newReturn;
         System.out.println("methodtranformer id '"+id+"' changed to '"+newId+"'.");
         return newId;
      }
      else {
         return id;
      }
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
    * @param mv - the MethodWriter created for this instance. Made just before
    *           this instance will visit things
    */
   abstract void injectMethodWriter(MethodVisitor mv);
   
   /**
    * generates an array of MethodTransformers from a class of annotations. This
    * allows for easy use of ASM modifications See com.sijobe.asm.SimpleHooked
    * for an example.
    * 
    * @param clazz - the class containing the modification methods.
    * @return an array of MethodTransformers that were generated
    */
   static MethodTransformer[] generateFromFunctions(Class<?> clazz) {
      List<MethodTransformer> modifiers = new LinkedList<MethodTransformer>();
      for (Method method : clazz.getDeclaredMethods()) {
         if (Modifier.isStatic(method.getModifiers())) {
            if (method.isAnnotationPresent(MethodReplacer.Hook.class)) {
               String annotation = method.getAnnotation(MethodReplacer.Hook.class).value();
               System.out.println("found replacement for " + annotation);
               modifiers.add(new MethodReplacer(annotation, method));
            } else if (method.isAnnotationPresent(MethodPrefixer.Hook.class)) {
               String annotation = method.getAnnotation(MethodPrefixer.Hook.class).value();
               System.out.println("found prefix for " + annotation);
               modifiers.add(new MethodPrefixer(annotation, method));
            }
         }
      }
      
      MethodTransformer[] r = new MethodTransformer[modifiers.size()];
      return modifiers.toArray(r);
   }
}
