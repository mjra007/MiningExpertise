 package net.enchantedoasis.mining.GUI.LuckyChests;
 
 import java.util.ArrayList;
 import java.util.HashMap;
 import java.util.List;
 import net.enchantedoasis.mining.Ability;
 import net.enchantedoasis.mining.GUI.Ability.AbilityMenu;
 import net.enchantedoasis.userData.User;
 import org.bukkit.Bukkit;
 import org.bukkit.ChatColor;
 import org.bukkit.Material;
 import org.bukkit.entity.Player;
 import org.bukkit.inventory.Inventory;
 import org.bukkit.inventory.ItemStack;
 import org.bukkit.inventory.meta.ItemMeta;
 
 public class LuckyChestsMeny
 {
   public static Inventory luckychestsm = Bukkit.createInventory(null, 36, "Mining Expertise - LuckyChest");
   
   public static void createAllItems(Player player) {
     HashMap<Integer, Ability> abilities = Ability.getAbilitiesList();
     for (int i = 0; i < abilities.size(); i++)
     {
 
 
 
       if ((((Ability)abilities.get(Integer.valueOf(i))).getName().equalsIgnoreCase("BloodMagic") 
		^ ((Ability)abilities.get(Integer.valueOf(i))).getName().equalsIgnoreCase("Botania") 
		^ ((Ability)abilities.get(Integer.valueOf(i))).getName().equalsIgnoreCase("Witchery") 
		^ ((Ability)abilities.get(Integer.valueOf(i))).getName().equalsIgnoreCase("ArsMagica") 
		^ ((Ability)abilities.get(Integer.valueOf(i))).getName().equalsIgnoreCase("Thaumcraft")
		^ ((Ability)abilities.get(Integer.valueOf(i))).getName().equalsIgnoreCase("Forestry"))
		^ ((Ability)abilities.get(Integer.valueOf(i))).getName().equalsIgnoreCase("Random")) {

         createItem(((Ability)abilities.get(Integer.valueOf(i))).getIcon(), luckychestsm, i * 2, 
           ChatColor.GREEN + ((Ability)abilities.get(Integer.valueOf(i))).getName().toString(), ((Ability)abilities.get(Integer.valueOf(i))).getDescription());
        
       }
     }
     
     User user = (User)User.getUserData().get(player.getUniqueId());
     createItem(Material.PAPER, luckychestsm, 30, ChatColor.GREEN + "Details", ChatColor.GRAY + "Selected: " + ChatColor.DARK_PURPLE + user.getAbilitySelected() + "\n" + ChatColor.GRAY + "Abilities Owned: " + ChatColor.RED + AbilityMenu.listAbilities(user.getAbilitiesOwned()));
     createItem(Material.EMERALD, luckychestsm, 32, ChatColor.GREEN + "Upgrade your chance", ChatColor.GRAY + "Chance: " + ChatColor.RED + user.getChestChance());
     createItem(Material.ARROW, luckychestsm, 31, ChatColor.GREEN + "Back", "");
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
   
   public static void bakeIt(Player player)
   {
     luckychestsm.clear();
     createAllItems(player);
     player.openInventory(luckychestsm);
   }
 }


