package com.sijobe.spc.wrapper;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.stats.Achievement;
import net.minecraft.stats.AchievementList;

/**
 * Contains methods that interact with the Minecraft statistics engine 
 * 
 * @author simo_415
 * @version 1.0
 */
public class Stats {

   /**
    * A NAME > ID pair to match a String name with an internal Minecraft List
    * element position
    */
   private static Map<String, Integer> ACHIEVEMENTS;
   
   /**
    * Generates a list of achievement name to ID pairs that are used to find 
    * and load the achievements within minecraft
    */
   static {
      ACHIEVEMENTS = new HashMap<String, Integer>();
      for (int i = 0; i < AchievementList.achievementList.size(); i++) {
         Object achievement = AchievementList.achievementList.get(i);
         if (achievement instanceof Achievement) {
            Achievement a = (Achievement)achievement;
            ACHIEVEMENTS.put(a.toString().replace(' ', '_'), i);
         }
      }
   }
   
   /**
    * Gets a list of achievements that can be unlocked
    * 
    * @return A List of Strings that are the names of the achievements
    */
   public static List<String> getAchievementNames() {
      return Collections.list(Collections.enumeration(ACHIEVEMENTS.keySet()));
   }
   
   /**
    * Returns a Minecraft Achievement object
    * 
    * @param name - The name of the achievement to retrieve
    * @return The matched achievement or null if it doesn't exist
    */
   public static Achievement getAchievementByName(String name) {
      if (name == null || name.length() == 0) {
         return null;
      }
      for (String a : ACHIEVEMENTS.keySet()) {
         if (name.equalsIgnoreCase(a)) {
            return (Achievement)AchievementList.achievementList.get(ACHIEVEMENTS.get(a));
         }
      }
      return null;
   }
   
   /**
    * Checks if the specified achievement name exists within the generated list
    * 
    * @param name - The name of the achievement
    * @return True if the achievement exists, false otherwise
    */
   public static boolean doesAchievementExist(String name) {
      return getAchievementByName(name) != null;
   }
}
