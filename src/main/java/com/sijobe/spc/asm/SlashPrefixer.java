package com.sijobe.spc.asm;

import net.minecraft.network.NetHandlerPlayServer;

import org.objectweb.asm.MethodVisitor;

import com.sijobe.spc.command.PrefixSlash;
import com.sun.xml.internal.ws.org.objectweb.asm.Opcodes;

public class SlashPrefixer extends MethodTransformer
{

	SlashPrefixer()
	{
		super("net.minecraft.network.NetHandlerPlayServer:processChatMessage:(Lnet/minecraft/network/play/client/C01PacketChatMessage;)V");
	}

	@Override
	void injectMethodWriter(MethodVisitor mv)
	{
		this.mv = mv;	
	}
	
	@Override
	public void visitLdcInsn(Object cst)
	{
		if(!cst.equals("/"))
		{
			this.mv.visitLdcInsn(cst);
		}
	}
	
	@Override
	public void visitMethodInsn(int opcode, String owner, String name, String desc)
	{
		if(opcode == Opcodes.INVOKEVIRTUAL && owner.equals("java/lang/String") && name.equals("startsWith") && desc.equals("(Ljava/lang/String;)Z"))
		{
		   this.mv.visitVarInsn(Opcodes.ALOAD, 0);
			this.mv.visitMethodInsn(Opcodes.INVOKESTATIC, "com/sijobe/spc/asm/SlashPrefixer", "isCommand", "(Ljava/lang/String;Lnet/minecraft/network/NetHandlerPlayServer;)Z");
		}
		else
		{
			this.mv.visitMethodInsn(opcode, owner, name, desc);
		}
	}

	public static boolean isCommand(String s, NetHandlerPlayServer handler)
	{
		return s.startsWith("/") || PrefixSlash.playersUsing.contains(handler.playerEntity.getCommandSenderName());
	}
}
