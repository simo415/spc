package com.sijobe.spc.asm;

import net.minecraft.network.NetHandlerPlayServer;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

/**
 * makes the server allow customized reach distance for entities effectively
 * changes the line in NetHandlerPlayServer.processUseEntity double d0 = 36.0 to
 * double d0 = ReachChanger.getEntityReachDistance(this);
 * 
 * @author aucguy
 * @version 1.0
 * 
 */
public class EntityReacherServer extends MethodTransformer {
   
   EntityReacherServer() {
      super("net.minecraft.network.NetHandlerPlayServer:processUseEntity:(Lnet/minecraft/network/play/client/C02PacketUseEntity;)V");
   }
   
   @Override
   void injectMethodWriter(MethodVisitor mv) {
      this.mv = mv;
   }
   
   @Override
   public void visitLdcInsn(Object cst) {
      if(cst.equals(36.0D)) {
         super.visitVarInsn(Opcodes.ALOAD, 0);
         super.visitMethodInsn(Opcodes.INVOKESTATIC, "com/sijobe/spc/asm/EntityReacherServer", "getEntityReachDistance", "(Lnet/minecraft/network/NetHandlerPlayServer;)D");
      }
      else{
         super.visitLdcInsn(cst);
      }
   }
   
   public static double getEntityReachDistance(NetHandlerPlayServer handler) {
      return Math.pow(handler.playerEntity.theItemInWorldManager.getBlockReachDistance(), 2);
   }
}
