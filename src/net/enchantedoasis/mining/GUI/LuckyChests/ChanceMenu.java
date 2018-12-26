 package net.enchantedoasis.mining.GUI.LuckyChests;
 
 import java.util.ArrayList;
 import java.util.HashMap;
 import java.util.List;
 import net.enchantedoasis.userData.User;
 import org.bukkit.ChatColor;
 import org.bukkit.Material;
 import org.bukkit.entity.Player;
 import org.bukkit.inventory.Inventory;
 import org.bukkit.inventory.ItemStack;
 import org.bukkit.inventory.meta.ItemMeta;
 
 public class ChanceMenu
 {
   public static Inventory chance = org.bukkit.Bukkit.createInventory(null, 36, "Mining Expertise - LuckyChest");
   public static int price = 400;
   
   public static void createAllItems(Player player) { User user = (User)User.getUserData().get(player.getUniqueId());
     
     Short short1 = 14;
     Short short2 =14;
     Short short3 = 14;
     Short short4 = 0;
     String lore1 = "\n" + ChatColor.GRAY + "Price: " + ChatColor.RED + price * 1;
     String lore2 = "\n" + ChatColor.GRAY + "Price: " + ChatColor.RED + price * 2;
     String lore3 = "\n" + ChatColor.GRAY + "Price: " + ChatColor.RED + price * 3;
     if (user.getChestChance().intValue() == 3) {
       short1 = Short.valueOf((short)5);
       short2 = Short.valueOf((short)5);
       short3 = Short.valueOf((short)5);
       lore1 = "\n" + ChatColor.GRAY + "Bought";
       lore2 = "\n" + ChatColor.GRAY + "Bought";
       lore3 = "\n" + ChatColor.GRAY + "Bought";
     } else if (user.getChestChance().intValue() == 2) {
       short1 = Short.valueOf((short)5);
       short2 = Short.valueOf((short)5);
       lore1 = "\n" + ChatColor.GRAY + "Bought";
       lore2 = "\n" + ChatColor.GRAY + "Bought";
     } else if (user.getChestChance().intValue() == 1) {
       short1 = Short.valueOf((short)5);
       lore1 = "\n" + ChatColor.GRAY + "Bought";
     } else {
       short1 = Short.valueOf((short)5);
       short2 = Short.valueOf((short)5);
       short3 = Short.valueOf((short)5);
       lore1 = "\n" + ChatColor.GRAY + "Bought";
       lore2 = "\n" + ChatColor.GRAY + "Bought";
       lore3 = "\n" + ChatColor.GRAY + "Bought";
     }
     createItem(Material.STAINED_GLASS_PANE, short1, chance, 12, ChatColor.GREEN + "1%", lore1);
     createItem(Material.STAINED_GLASS_PANE, short2, chance, 13, ChatColor.GREEN + "2%", lore2);
     createItem(Material.STAINED_GLASS_PANE, short3, chance, 14, ChatColor.GREEN + "3%", lore3);
     createItem(Material.EMERALD, short4, chance, 30, ChatColor.GREEN + "Upgrade your chance", ChatColor.GRAY + "Chance: " + ChatColor.RED + user.getChestChance());
     createItem(Material.ARROW, short4, chance, 32, ChatColor.GREEN + "Back-", "");
   }
   
   public static void createItem(Material material, Short damage, Inventory inv, int Slot, String name, String lore)
   {
     ItemStack item = new ItemStack(material, 1, damage.shortValue());
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
   
   public static void bakeIt(Player player) {
     System.out.println("chance");
     chance.clear();
     createAllItems(player);
     player.openInventory(chance);
   }
 }


