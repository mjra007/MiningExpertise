 package net.enchantedoasis.mining;
 
 import java.util.Random;
 
 public class CommonUTIL
 {
   public static Random random = new Random();
   
   public static int getRandom(int lower, int upper) {
     int range1 = random.nextInt(upper) + lower;
     return range1;
   }
 }


