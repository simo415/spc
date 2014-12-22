package com.sijobe.spc.asm;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

/**
 * makes the client allow customized reach distance for entities effectively
 * removes the code:
 * 
 * if (this.mc.playerController.extendedReach()) { d0 = 6.0D; d1 = 6.0D; } else
 * { if (d0 > 3.0D) { d1 = 3.0D; }
 * 
 * d0 = d1; }
 * 
 * @author aucguy
 * @version 1.0
 */
public class EntityReacherClient extends MethodTransformer {
   State state;
   MethodVisitor writer;
   
   EntityReacherClient() {
      super("net.minecraft.client.renderer.EntityRenderer:getMouseOver:(F)V");
   }
   
   @Override
   void injectMethodWriter(MethodVisitor mv) {
      this.mv = mv;
      this.writer = mv;
   }
   
   @Override
   public void visitCode() {
      this.state = State.PRE_CHANGE;
   }
   
   @Override
   public void visitMethodInsn(int opcode, String owner, String name, String desc) {
      if (this.state == State.PRE_CHANGE && opcode == Opcodes.INVOKEVIRTUAL
            && owner.equals("net/minecraft/client/multiplayer/PlayerControllerMP") && name.equals("extendedReach")
            && desc.equals("()Z")) {
         this.state = State.AFTER_EXTEND;
         this.mv = null;
         this.writer.visitInsn(Opcodes.POP);
      } else {
         super.visitMethodInsn(opcode, owner, name, desc);
      }
   }
   
   @Override
   public void visitVarInsn(int opcode, int var) {
      if (this.state == State.AFTER_EXTEND && var == 0) {
         this.state = State.POST_CHANGE;
         this.mv = this.writer;
         super.visitVarInsn(opcode, var);
      } else {
         super.visitVarInsn(opcode, var);
      }
   }
   
   enum State {
      PRE_CHANGE, // before extendedReach() is called
      AFTER_EXTEND, // after extendedReach() is called
      POST_CHANGE; // after all the changes
   }
}
