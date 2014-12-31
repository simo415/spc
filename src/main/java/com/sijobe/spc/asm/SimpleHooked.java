package com.sijobe.spc.asm;

import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

/**
 * simple ASM hooks that either replace or prefix methos
 * 
 * @author aucguy
 * @version 1.0
 */
public class SimpleHooked {
   /**
    * Allows spc to have control over client-sided block reach
    * effectively makes the method this:
    *    return com.sijobe.spc.command.BlockReach.reachDistance;
    * 
    * @param mv - the MethodWriter instance
    */
   @MethodReplacer.Hook("net.minecraft.client.multiplayer.PlayerControllerMP:getBlockReachDistance:()F")
   public static void getBlockReachDistance(MethodVisitor mv) {
      mv.visitCode();
      mv.visitFieldInsn(Opcodes.GETSTATIC, "com/sijobe/spc/command/BlockReach", "reachDistance", "F");
      mv.visitInsn(Opcodes.FRETURN);
      mv.visitMaxs(1, 1);
      mv.visitEnd();
   }
   
   /**
    * Allows spc to have control over whether or not the inside of blocks are rendered
    * effectivly prefixes the method with this
    *    if(com.sijobe.spc.ModSpc.instance.proxy.shouldNotRenderInsideOfBlock()) {
    *       return;
    *    }
    * 
    * @param mv - the MethodWriter
    */
   @MethodPrefixer.Hook("net.minecraft.client.renderer.ItemRenderer:renderInsideOfBlock:(FLnet/minecraft/util/IIcon;)V")
   public static void renderInsideOfBlock(MethodVisitor mv) {
      mv.visitCode();
      mv.visitFieldInsn(Opcodes.GETSTATIC, "com/sijobe/spc/ModSpc", "instance", "Lcom/sijobe/spc/ModSpc;");
      mv.visitFieldInsn(Opcodes.GETFIELD, "com/sijobe/spc/ModSpc", "proxy", "Lcom/sijobe/spc/proxy/Proxy;");
      mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "com/sijobe/spc/proxy/Proxy", "shouldNotRenderInsideOfBlock", "()Z");
      Label l0 = new Label();
      mv.visitJumpInsn(Opcodes.IFEQ, l0);
      mv.visitInsn(Opcodes.RETURN);
      mv.visitLabel(l0);
      mv.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
   }
}
