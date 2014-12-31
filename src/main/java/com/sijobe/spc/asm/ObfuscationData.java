package com.sijobe.spc.asm;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map.Entry;

/**
 * stores obfuscation mappings
 * 
 * @author aucguy
 * @version 1.0
 */
class ObfuscationData extends HashMap<String, String> {
   private static final long serialVersionUID = 8501973207922800333L;
   /**
    * whether or not to warn about having no obfuscated mapping
    */
   boolean shouldWarn = false;
   
   @Override
   public String get(Object key) {
      String str = ((String) key).replace('/', '.');
      if (this.containsKey(str)) {
         return super.get(str);
      } else {
         if (shouldWarn && str.startsWith("net.minecraft.")) {
            System.err.println("no obfuscated mapped for " + str);
         }
         return str;
      }
   }
   
   /**
    * @param key - the key
    * @param converted - if true, all '.' are replaced with '/'
    * @return
    */
   public String get(Object key, boolean converted) {
      String value = this.get(key);
      if(converted) {
         value = value.replace('.', '/');
      }
      return value;
   }
   
   /**
    * returns the value to key map of this
    */
   
   public ObfuscationData reverse() {
      ObfuscationData map = new ObfuscationData();
      for(Entry<String, String> entry : this.entrySet()) {
         map.put(entry.getValue(), entry.getKey());
      }
      return map;
   }
   
   /**
    * loads the obfuscated name mappings
    */
   protected void loadMappings() {
      try {
         InputStream file = this.getClass().getResourceAsStream("renames.txt");
         if(file == null) {
            throw(new IOException());
         }
         byte[] data = new byte[file.available()];
         file.read(data);
         String[] lines = new String(data).replace("\r", "\n").split("\n");
         for(String line : lines) {
            if(line.length() == 0) {
               continue; // blank line
            }
            line = line.split("#", 2)[0];
            String[] parts = line.split(" ");
            if(parts.length != 2) {
               continue; //invalid syntax
            }
            String deobfuscated = parts[0];
            String obfuscated = parts[1];
            this.put(deobfuscated, obfuscated);
         }
      } catch (IOException error) {
         System.err.println("couldn't read obfuscation mappings");
         error.printStackTrace();
      }
   }
}
