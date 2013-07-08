package com.sijobe.spc.util;

import java.util.HashMap;

/**
 * Class for storing obfuscation mappings
 *
 * @author q3hardcore
 * @version 1.3
 */
public class Mappings extends HashMap<String, String[]> {

   private static final long serialVersionUID = 1L;

   /**
    * Adds a value to the map with all the alternate mappings
    * 
    * @see java.util.HashMap#put(java.lang.Object, java.lang.Object)
    */
   @Override
   public String[] put(String mcpName, String[] alternateMappings) {
      return super.put(mcpName, makeMapping(mcpName, alternateMappings));
   }

   /**
    * Adds a value to the map without alteration
    * 
    * @see java.util.HashMap#put(java.lang.Object, java.lang.Object)
    */
   public String[] superPut(String key, String[] value) {
      return super.put(key, value);
   }

   /**
    * Gets mappings for the specified value
    *
    * @see java.util.HashMap#get(java.lang.Object)
    */
   @Override
   public String[] get(Object key) {
      String[] mappings = super.get(key);
      if(mappings == null) {
         return new String[]{ key.toString() };
      } else {
         return mappings;
      }
   }
   
   /**
    * @param mcpName
    * @param mappings
    * @return mappings with mcpName prepended
    */
   private String[] makeMapping(String mcpName, String[] mappings) {
      String[] newMappings = new String[mappings.length + 1];
      newMappings[0] = mcpName;
      for(int i = 0; i < mappings.length; i++) {
         newMappings[i+1] = mappings[i];
      }
      return newMappings;
   }
}