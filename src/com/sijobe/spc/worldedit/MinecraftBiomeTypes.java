package com.sijobe.spc.worldedit;

// Obfuscated references: 0

import com.sk89q.worldedit.BiomeType;
import com.sk89q.worldedit.BiomeTypes;
import com.sk89q.worldedit.UnknownBiomeTypeException;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MinecraftBiomeTypes implements BiomeTypes {

	@Override
	public boolean has(String name) {
		try {
			MinecraftBiomeType.valueOf(name.toUpperCase(Locale.ENGLISH));
			return true;
		} catch (IllegalArgumentException var3) {
			return false;
		}
	}

	@Override
	public BiomeType get(String name) throws UnknownBiomeTypeException {
		try {
			return MinecraftBiomeType.valueOf(name.toUpperCase(Locale.ENGLISH));
		} catch (IllegalArgumentException var3) {
			throw new UnknownBiomeTypeException(name);
		}
	}

	@Override
	public List<BiomeType> all() {
		return Arrays.<BiomeType>asList(MinecraftBiomeType.values());
	}
}
