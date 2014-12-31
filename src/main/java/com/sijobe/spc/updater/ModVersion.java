package com.sijobe.spc.updater;

import java.util.Date;

/**
 * Standard version class
 *
 * @author simo_415
 */
public class ModVersion {

   private String name;
   private String version;
   private Date lastupdate;

   public ModVersion(String name, String version, Date lastupdate) {
      this.name = name;
      this.version = version;
      this.lastupdate = lastupdate;
   }
   
   public String getName() {
      return name;
   }

   public String getVersion() {
      return version;
   }

   public Date getLastUpdate() {
      return lastupdate;
   }
}
