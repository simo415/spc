package com.sijobe.spc.worldedit;

import com.sijobe.spc.core.Constants;

import java.io.File;

public class PropertiesConfiguration extends com.sk89q.worldedit.util.PropertiesConfiguration {

   /**
    * Initialises the class
    */
   public PropertiesConfiguration() {
      super(new File(Constants.MOD_DIR,"worldedit.properties"));
   }
   
   /**
    * @see com.sk89q.worldedit.LocalConfiguration#getWorkingDirectory()
    */
   @Override
   public File getWorkingDirectory() {
      return Constants.MOD_DIR;
   }
}
