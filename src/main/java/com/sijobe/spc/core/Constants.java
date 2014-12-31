package com.sijobe.spc.core;

import com.sijobe.spc.updater.ModVersion;
import com.sijobe.spc.util.Settings;
import com.sijobe.spc.wrapper.MinecraftServer;

import java.io.File;
import java.util.Date;

/**
 * Contains all of the constant values that the mod uses
 *
 * @author simo_415
 * @version 1.0
 */
public class Constants {
	/**
    * Contains the version string of the current Minecraft version
    */
   public static final String VERSION = "1.7.2";
   
   /**
    * The name of the mod
    */
   public static final String NAME = "Single Player Commands";
   
   /**
    * The current version of the mod
    */
   public static final ModVersion SPC_VERSION = new ModVersion(NAME, VERSION, new Date(1415419100)); // November 7, 2014 9:58 pm

   /**
    * The directory that the mod saves/loads global settings from
    */
   public static final File MOD_DIR = new File(MinecraftServer.getDirectoryName(), "mods/spc");
   
   // Creates the mod directory if it doesn't already exist
   static {
      if (!MOD_DIR.exists()) {
         MOD_DIR.mkdirs();
      }
   }
   
   /**
    * The default settings file to use
    */
   public static final Settings DEFAULT_SETTINGS = new Settings(new File(MOD_DIR, "default.properties"));
   
   /**
    * Directory where the Minecraft level saves are located
    */
   public static final File SAVES_DIR = new File(MinecraftServer.getDirectoryName(), MinecraftServer.getMinecraftServer().isDedicatedServer() ? "" : "saves");
   
   /**
    * @return the version
    */
   public static String getVersion() {
      return VERSION;
   }
   
   /**
    * @return the name
    */
   public static String getName() {
      return NAME;
   }
   
   /**
    * @return the spcVersion
    */
   public static ModVersion getSpcVersion() {
      return SPC_VERSION;
   }
   
   /**
    * @return the modDir
    */
   public static File getModDir() {
      return MOD_DIR;
   }
}
