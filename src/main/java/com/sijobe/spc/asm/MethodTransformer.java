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
    * allows for deobfuscated names to go through the visit methods
    */
   final class Wrapper extends MethodVisitor {
      Wrapper(MethodVisitor mv) {
         super(Opcodes.ASM4, mv);
      }
      
      @Override
      public void visitMethodInsn(int opcode, String owner, String name, String desc) {
         String[] parts = convertMethod(owner.replace('/', '.'), name.replace('/', '.'), desc.replace('/', '.'), Processor
               .getInstance().reversedMappings);
         super.visitMethodInsn(opcode, parts[0].replace('.', '/'), parts[1].replace('.', '/'), parts[2]
               .replace('.', '/'));
      }
   }
   
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
      String[] parts = id.split(":");
      String[] changed = convertMethod(parts[0], parts[1], parts[2]);
      this.id = changed[0] + ":" + changed[1] + ":" + changed[2];
      System.out.println("id changed from "+id+" to "+this.id);
   }
   
   protected String[] convertMethod(String oldOwner, String oldName, String oldDesc) {
      return convertMethod(oldOwner, oldName, oldDesc, Processor.getInstance().mappings);
   }
   
   /**
    * converts the given development-named method to the obfuscated method if
    * necessary.
    * 
    * @param oldOwner - the deobfuscated name of the class with the given method
    * @param oldName - the deobfuscated name of the method
    * @param oldDesc - the deobfuscated method descriptor
    * @param slashes - whether or not the names contain slashes
    */
   protected static String[] convertMethod(String oldOwner, String oldName, String oldDesc, Map<String, String> mappings) {
      if (Processor.getInstance().obfuscated) {
         String newOwner = mappings.get(oldOwner);
         String[] parts = mappings.get(oldOwner + "." + oldName).split("\\.");
         String newName = parts[parts.length-1];
         
         String[] partsDesc = oldDesc.split("\\)");
         String oldArgs = partsDesc[0].replace("(", "");
         String oldReturn = partsDesc[1];
         
         String newArgs = "";
         for (int i = 0; i < oldArgs.length(); i++) {
            if (oldArgs.charAt(i) == 'L') {
               i++;
               String arg = "";
               for (; oldArgs.charAt(i) != ';'; i++) {
                  arg += oldArgs.charAt(i);
               }
               newArgs += "L" + mappings.get(arg) + ";";
            } else {
               newArgs += oldArgs.charAt(i);
            }
         }
         
         String newReturn;
         if (oldReturn.startsWith("L")) {
            newReturn = "L" + mappings.get(oldReturn.substring(1, oldReturn.length() - 1)) + ";";
         } else {
            newReturn = oldReturn;
         }
         
         String[] newId = new String[] { newOwner, newName, "(" + newArgs + ")" + newReturn };
         return newId;
      } else {
         return new String[] { oldOwner, oldName, oldDesc };
      }
   }
   
   /**
    * gives this MethodTrasnformer the method writer instance
    * 
    * @param mv - the MethodWriter created for this instance. Made just before
    *           this instance will visit things
    */
   abstract void injectMethodWriter(MethodVisitor mv);
   
   /**
    * gets teh applicable method
    * 
    * @return the id of the method that this transformer modifies
    */
   String getApplicableMethod() {
      return this.id;
   }
   
   @Override
   public void visitMethodInsn(int opcode, String owner, String name, String desc) {
      String[] parts = convertMethod(owner.replace('/', '.'), name.replace('/', '.'), desc.replace('/', '.'));
      super.visitMethodInsn(opcode, parts[0].replace('.', '/'), parts[1].replace('.', '/'), parts[2].replace('.', '/'));
   }
   
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
