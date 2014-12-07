package com.sijobe.spc.asm.make;

import net.minecraft.util.IIcon;

public class ItemRenderer {
	@SuppressWarnings("unused")
   private void renderInsideOfBlock(float par1, IIcon par2Icon) {
		if(com.sijobe.spc.ModSpc.instance.proxy.shouldNotRenderInsideOfBlock()) {
			return;
		}
	}
}
