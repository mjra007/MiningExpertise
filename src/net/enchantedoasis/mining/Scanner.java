package net.enchantedoasis.mining;
 
 import java.io.PrintStream;
import java.util.ArrayList;
 import java.util.HashMap;
 import java.util.List;
 import java.util.Map;
 import java.util.Map.Entry;
 import org.bukkit.Location;
 import org.bukkit.Material;
import org.bukkit.block.Chest;
 import org.bukkit.inventory.Inventory;
 import org.bukkit.inventory.ItemStack;
 
 public class Scanner
 {
   Inventory inv;
   String path;
   HashMap<Material, List<Short>> scanned = new HashMap();
   Location loc;
   
   public Scanner(String[] args, Location loc1, int radius) {
     this.path = args[0];
     this.loc = circle(loc1, 5);
     if (!this.loc.equals(null)) {
       Chest chest = (Chest)this.loc.getBlock().getState();
       this.inv = chest.getInventory();
       System.out.print("Found a chest");
     } else {
       System.out.print("Did not Found a chest");
     }
     
     System.out.println("Started scanning");
     scan();
     System.out.print("Scanned Finished");
     System.out.print("Started saving");
     save();
     System.out.print("Finished saving");
   }
   
   private void scan() {
     System.out.println("No of Inventory spots: " + this.inv.getSize());
     
     for (int i = 0; i < this.inv.getSize(); i++) {
       try {
         ItemStack is = this.inv.getItem(i);
         
         
         
         
         List<Short> data = new java.util.ArrayList();
         System.out.print(is.getType() + " " + is.getDurability());
         
         if (this.scanned.containsKey(is.getType())) {
           data = (List)this.scanned.get(is.getType());
           data.add(Short.valueOf(is.getDurability()));
           this.scanned.put(is.getType(), data);
         } else {
           data.add(Short.valueOf(is.getDurability()));
           this.scanned.put(is.getType(), data);
         }
       } catch (NullPointerException error) {
         System.out.println("Nothing in this slot");
       }
     }
     
     System.out.println(this.scanned + "sdsd");
   }
   
   private void save()
   {
       List<String> list = new ArrayList<String>();
       for (Map.Entry<Material, List<Short>> entry : scanned.entrySet()) {
           String finalString = entry.getKey().toString();
         
           for (Short damage : entry.getValue()) {
               finalString = finalString + "/" + damage.toString();
           }
           list.add(finalString);
       }
     System.out.println(list);
     MiningExpertise.p.getConfig().set(this.path, list);
     MiningExpertise.p.saveConfig();
   }
   
   public static Object getKeyFromValue(Map hm, Object value) {
     for (Object o : hm.keySet()) {
       if (hm.get(o).equals(value)) {
         return o;
       }
     }
     return null;
   }
   
   private Location circle(Location loc1, int radius) {
     int cx = loc1.getBlockX();
     int cy = loc1.getBlockY();
     int cz = loc1.getBlockZ();
     
     for (int x = cx - radius; x <= cx + radius; x++) {
       for (int z = cz - radius; z <= cz + radius; z++) {
         for (int y = cy - radius; y < cy + radius; y++) {
           double dist = (cx - x) * (cx - x) + (cz - z) * (cz - z) + (cy - y) * (cy - y);
           
           if (dist < radius * radius) {
             Location l = new Location(loc1.getWorld(), x, y + 2, z);
             if (l.getBlock().getType() == Material.CHEST) {
               return l;
             }
           }
         }
       }
     }
     return null;
   }
 }


