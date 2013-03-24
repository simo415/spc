package com.sijobe.spc.worldedit;

import java.lang.reflect.Field;
import java.util.Locale;
import com.sk89q.worldedit.BiomeType;
import net.minecraft.src.BiomeGenBase;

public enum MinecraftBiomeType implements BiomeType {

	OCEAN(BiomeGenBase.biomeList[0]),
	PLAINS(BiomeGenBase.biomeList[1]),
	DESERT(BiomeGenBase.biomeList[2]),
	EXTREMEHILLS(BiomeGenBase.biomeList[3]),
	FOREST(BiomeGenBase.biomeList[4]),
	TAIGA(BiomeGenBase.biomeList[5]),
	SWAMPLAND(BiomeGenBase.biomeList[6]),
	RIVER(BiomeGenBase.biomeList[7]),
	HELL(BiomeGenBase.biomeList[8]),
	SKY(BiomeGenBase.biomeList[9]),
	FROZENOCEAN(BiomeGenBase.biomeList[10]),
	FROZENRIVER(BiomeGenBase.biomeList[11]),
	ICEPLAINS(BiomeGenBase.biomeList[12]),
	ICEMOUNTAINS(BiomeGenBase.biomeList[13]),
	MUSHROOMISLAND(BiomeGenBase.biomeList[14]),
	MUSHROOMISLANDSHORE(BiomeGenBase.biomeList[15]),
	BEACH(BiomeGenBase.biomeList[16]),
	DESERTHILLS(BiomeGenBase.biomeList[17]),
	FORESTHILLS(BiomeGenBase.biomeList[18]),
	TAIGAHILLS(BiomeGenBase.biomeList[19]),
	EXTREMEHILLSEDGE(BiomeGenBase.biomeList[20]),
	JUNGLE(BiomeGenBase.biomeList[21]),
	JUNGLEHILLS(BiomeGenBase.biomeList[22]);

	private BiomeGenBase biome;

	private MinecraftBiomeType(BiomeGenBase biome) {
		this.biome = biome;
	}

	@Override
	public String getName() {
		return name().toLowerCase(Locale.ENGLISH);
	}

	/*
	public BiomeGenBase getSPCBiome() {
		return biome;
	}
	*/
	
	public int getBiomeID() {
		return biome.biomeID;
	}

	/*
	public String getBiomeName() {
		return biome.biomeName;
	}
	*/

}
