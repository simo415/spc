package com.sijobe.spc.util;

import net.minecraft.util.RegistryNamespaced;

public class RegistryIdCompatible extends RegistryNamespaced {
	@Override
	public Object getObject(String name) {
		if(this.containsKey(name)) {
			return super.getObject(name);
		}
		else {
			try {
				int num = Integer.parseInt(name);
				return this.getObjectById(num);
			}
			catch(NumberFormatException error) {
				return null;
			}
		}
	}
}
