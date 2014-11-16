package com.sijobe.spc.wrapper;


/**
 * Class is a container that gets the coordinate values
 *
 * @author simo_415
 */
public class Coordinate {
   
   private final double x;
   private final double y;
   private final double z;
   
   /**
    * Initialises the class using double values
    * 
    * @param x - The X coordinate
    * @param y - The Y coordinate
    * @param z - The Z coordinate
    */
   public Coordinate(double x, double y, double z) {
      this.x = x;
      this.y = y;
      this.z = z;
   }
   
   /**
    * Initialises the class using integer values
    * 
    * @param x - The X coordinate
    * @param y - The Y coordinate
    * @param z - The Z coordinate
    */
   public Coordinate(int x, int y, int z) {
      this.x = x;
      this.y = y;
      this.z = z;
   }
   
   /**
    * Gets the X coordinate
    * 
    * @return The X coordinate
    */
   public double getX() {
      return x;
   }
   
   /**
    * Gets the X coordinate rounded to the closest block
    * 
    * @return The X coordinate
    */
   public int getBlockX() {
      int x = (int)getX();
      return getX() < (double)x ? x - 1 : x;
   }
   
   /**
    * Gets the Y coordinate
    * 
    * @return The Y coordinate
    */
   public double getY() {
      return y;
   }
   
   /**
    * Gets the Y coordinate rounded to the closest block
    * 
    * @return The Y coordinate
    */
   public int getBlockY() {
      return (int)getY();
   }
   
   /**
    * Gets the Z coordinate
    * 
    * @return The Z coordinate
    */
   public double getZ() {
      return z;
   }
   
   /**
    * Gets the Z coordinate rounded to the closest block
    * 
    * @return The Z coordinate
    */
   public int getBlockZ() {
      int z = (int)getZ();
      return getZ() < (double)z ? z - 1 : z;
   }
   
   /**
    * Gets the distance between two coordinates
    * 
    * @param compare - The coordinate to compare
    * @return The distance between the coordinates
    */
   public double getDistanceBetweenCoordinates(Coordinate compare) {
      double diffX = getX() - compare.getX();
      double diffY = getY() - compare.getY();
      double diffZ = getZ() - compare.getZ();
      return Math.sqrt((diffX * diffX) + (diffY * diffY) + (diffZ * diffZ));
   }
   
   /**
    * Checks if this coordinate matches the provided object by comparing the 
    * double X, Y, Z fields. If they match true is returned
    * 
    * @see java.lang.Object#equals(java.lang.Object)
    */
   @Override
   public boolean equals(Object obj) {
      if (obj != null && obj instanceof Coordinate) {
         Coordinate compare = (Coordinate)obj;
         return compare.getX() == getX() && compare.getY() == getY() && compare.getZ() == getZ();
      }
      return false;
   }
   
   /**
    * Converts the Coordinate to a String value
    * 
    * @see java.lang.Object#toString()
    */
   @Override
   public String toString() {
      return x + "," + y + "," + z;
   }
}
