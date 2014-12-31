package com.sijobe.spc.asm;

import net.minecraft.network.NetHandlerPlayServer;

import org.objectweb.asm.MethodVisitor;

import com.sijobe.spc.command.PrefixSlash;
import org.objectweb.asm.Opcodes;

/**
 * prefixes slashes onto commands if the player has it enabled
 * effectivly replaces
 *    if(s.startsWith("/")) {
 *       this.handleSlashCommand(s);
 *    }
 * to:
 *    if(com.sijobe.spc.asm.SlashPrefixer.isCommand(this, s)) {
 *       this.handleSlashCommand(s);
 *    }
 * 
 * @author aucguy
 * @version 1.0
 */
public class SlashPrefixer extends MethodTransformer {
   
   SlashPrefixer() {
      super("net.minecraft.network.NetHandlerPlayServer:processChatMessage:(Lnet/minecraft/network/play/client/C01PacketChatMessage;)V");
   }
   
   @Override
   void injectMethodWriter(MethodVisitor mv) {
      this.mv = mv;
   }
   
   @Override
   public void visitLdcInsn(Object cst) {
      if (!cst.equals("/")) {
         super.visitLdcInsn(cst);
      }
   }
   
   @Override
   public void visitMethodInsn(int opcode, String owner, String name, String desc) {
      if (opcode == Opcodes.INVOKEVIRTUAL && owner.equals("java/lang/String") && name.equals("startsWith") && desc.equals("(Ljava/lang/String;)Z")) {
         super.visitVarInsn(Opcodes.ALOAD, 0);
         super.visitMethodInsn(Opcodes.INVOKESTATIC, "com/sijobe/spc/asm/SlashPrefixer", "isCommand", "(Ljava/lang/String;Lnet/minecraft/network/NetHandlerPlayServer;)Z");
      } else {
         super.visitMethodInsn(opcode, owner, name, desc);
      }
   }
   
   /**
    * @param s - the chat that was sent
    * @param handler - the netHandler instance for that player
    * @return - whether of not the chat should be treated as a command
    */
   public static boolean isCommand(String s, NetHandlerPlayServer handler) {
      return s.startsWith("/") || PrefixSlash.playersUsing.contains(handler.playerEntity.getCommandSenderName());
   }
}
