 package net.enchantedoasis.mining.GUI.Ability;
 
 import java.util.ArrayList;
 import java.util.HashMap;
 import java.util.List;
 import net.enchantedoasis.mining.Ability;
import net.enchantedoasis.mining.MiningExpertise;
import net.enchantedoasis.mining.ability.LuckyChest;
 import net.enchantedoasis.userData.User;
 import org.bukkit.Bukkit;
 import org.bukkit.ChatColor;
 import org.bukkit.Material;
 import org.bukkit.entity.Player;
 import org.bukkit.inventory.Inventory;
 import org.bukkit.inventory.ItemStack;
 import org.bukkit.inventory.meta.ItemMeta;
 
 
 public class AbilityMenu
 {
   public static Inventory mining = Bukkit.createInventory(null, 54, "Mining Expertise");
   
   public static void createAllItems(Player player) {
     User user = (User)User.getUserData().get(player.getUniqueId());
     List<Ability> abilities = MiningExpertise.instance.config.getAbilities();
     int i2 = 1;
     for (int i = 0; i < abilities.size(); i++) {
       if (!(abilities.get(i) instanceof LuckyChest)) {
           createItem(((Ability)abilities.get(i)).getIcon(), mining, i2 * 2, 
           ChatColor.GREEN + ((Ability)abilities.get(Integer.valueOf(i))).getName().toString(), ((Ability)abilities.get(Integer.valueOf(i))).getDescription());
     	i2++;      
				}
     }
     String lore = ChatColor.GRAY + "Selected: " + ChatColor.DARK_PURPLE + user.getAbilitySelected() + "\n" + ChatColor.GRAY + 
       "Abilities Owned: " + ChatColor.RED + listAbilities(user.getAbilitiesOwned()) + "\n";
     createItem(Material.CHEST, mining, i2 * 2, ChatColor.GREEN + "LuckyChest", ChatColor.GRAY + "Find chests with different" + "\n" + ChatColor.GRAY + 
       "kinds of loot in them while mining" + "\n" + "\n" + ChatColor.AQUA + 
       "MVP" + ChatColor.GRAY + ": no limit" + "\n" + ChatColor.GREEN + 
       "VIP" + ChatColor.YELLOW + "+" + ChatColor.GRAY + ": up to 2 LuckyChests" + "\n" + ChatColor.GREEN + 
       "VIP" + ChatColor.GRAY + ": up to 1 LuckyChests");
     createItem(Material.PAPER, mining, 40, ChatColor.GREEN + "Details", 
       lore);
     createItem(Material.ARROW, mining, 49, ChatColor.GREEN + "-Back", "");
   }
   
   public static String listAbilities(List<String> list) {
     String finalString = "";
     for (int i = 0; i < list.size(); i++) {
       finalString = finalString + "\n " + (String)list.get(i) + " ";
     }
     return finalString;
   }
   
   public static void createItem(Material material, Inventory inv, int Slot, String name, String lore) {
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
   
   public static void bakeIt(Player player) {
     mining.clear();
     createAllItems(player);
     player.openInventory(mining);
   }
 }


