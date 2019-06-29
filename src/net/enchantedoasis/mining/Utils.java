 package net.enchantedoasis.mining;
 
import java.util.ArrayList;
import java.util.List;
 import java.util.Random;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
 
 public class Utils
 {
   public static Random random = new Random();
   
   public static int getRandom(int lower, int upper) {
     int range1 = random.nextInt(upper) + lower;
     return range1;
   }
   
     public static void createItem(Material material, Inventory inv, int Slot, String name, String lore)
   {
     ItemStack item = new ItemStack(material);
     ItemMeta meta = item.getItemMeta();
     meta.setDisplayName(name);
     List<String> Lore = new ArrayList();
     String[] arrayOfString; int j = (arrayOfString = lore.split("\n")).length; for (int i = 0; i < j; i++) { String loresplit = arrayOfString[i];
       Lore.add(loresplit);
     }
     meta.setLore(Lore);
     item.setItemMeta(meta);
     inv.setItem(Slot, item);
   }
   public static void createItem(ItemStack stack, Inventory inv, int Slot, String lore)
   {
     ItemStack item = stack;
     ItemMeta meta = item.getItemMeta();
     List<String> Lore = new ArrayList();
     String[] arrayOfString; int j = (arrayOfString = lore.split("\n")).length;
     for (int i = 0; i < j; i++) 
     {
       String loresplit = arrayOfString[i];
       Lore.add(loresplit);
     }
     meta.setLore(Lore);
     item.setItemMeta(meta);
     inv.setItem(Slot, item);
   }
   
   public static void setInventory(Inventory inv, ItemStack[] itemss)
   {
       inv.clear();
       for (int i = 0; i < itemss.length; i++) {
           inv.setItem(i, itemss[i]);
       }
   }
   
   public static String toPercentage(float n){
    return String.format("%.0f",n*100)+"%";
    }
   
 }


