package com.sijobe.spc.asm;

import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class SimpleHooked
{
	@MethodReplacer.Hook("net.minecraft.client.multiplayer.PlayerControllerMP:getBlockReachDistance:()F")
	public static void getBlockReachDistance(MethodVisitor mv)
	{
		mv.visitCode();
		mv.visitFieldInsn(Opcodes.GETSTATIC, "com/sijobe/spc/command/BlockReach", "reachDistance", "F");
		mv.visitInsn(Opcodes.FRETURN);
		mv.visitMaxs(1, 1);
		mv.visitEnd();
	}
	
	@MethodPrefixer.Hook("net.minecraft.client.renderer.ItemRenderer:renderInsideOfBlock:(FLnet/minecraft/util/IIcon;)V")
	public static void renderInsideOfBlock(MethodVisitor mv)
	{
		mv.visitCode();
		mv.visitMethodInsn(Opcodes.INVOKESTATIC, "com/sijobe/spc/wrapper/Minecraft", "getMinecraft", "()Lnet/minecraft/client/Minecraft;");
		mv.visitFieldInsn(Opcodes.GETFIELD, "net/minecraft/client/Minecraft", "thePlayer", "Lnet/minecraft/client/entity/EntityClientPlayerMP;");
		mv.visitFieldInsn(Opcodes.GETFIELD, "net/minecraft/client/entity/EntityClientPlayerMP", "noClip", "Z");
		Label l0 = new Label();
		mv.visitJumpInsn(Opcodes.IFEQ, l0);
		mv.visitLdcInsn(new Float("1.1"));
		mv.visitInsn(Opcodes.RETURN);
		mv.visitLabel(l0);
		mv.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
	}
}
