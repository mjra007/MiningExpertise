 package net.enchantedoasis.mining.GUI.Ability;
 
 import java.util.HashMap;
 import net.enchantedoasis.Menu.GUI.Central;
 import net.enchantedoasis.mining.Ability;
import net.enchantedoasis.mining.MiningExpertise;
 import org.bukkit.ChatColor;
 import org.bukkit.entity.Player;
 import org.bukkit.event.EventHandler;
 import org.bukkit.event.inventory.InventoryClickEvent;
 import org.bukkit.inventory.Inventory;
 import org.bukkit.inventory.ItemStack;
 import org.bukkit.inventory.meta.ItemMeta;
 
 public class AbilityMenuHandler implements org.bukkit.event.Listener
 {
   static ItemStack clicked;
   
   @EventHandler
   public void onInventoryClick(InventoryClickEvent event)
   {
     Player playerexact = (Player)event.getWhoClicked();
     clicked = event.getCurrentItem();
     Inventory inventory = event.getInventory();
     if (inventory.getName().equals(AbilityMenu.mining.getName()))
     {
       switch (event.getAction()) {
       case PLACE_ALL: 
       case PICKUP_SOME: 
       case PICKUP_HALF: 
       case PICKUP_ONE: 
         for (int i = 0; i < MiningExpertise.instance.config.getAbilities().size(); i++) {
           if ((MiningExpertise.instance.config.getAbilities().get(i)).getName().equalsIgnoreCase(ChatColor.stripColor(clicked.getItemMeta().getDisplayName())))
           {
             (MiningExpertise.instance.config.getAbilities().get(i)).clicked(playerexact, "mining");
           }
         }
         if (ChatColor.stripColor(clicked.getItemMeta().getDisplayName()).equalsIgnoreCase("LuckyChest"))
         {
           net.enchantedoasis.mining.GUI.LuckyChests.LuckyChestsMeny.bakeIt(playerexact);
         }
         if ("-Back.".equalsIgnoreCase(ChatColor.stripColor(clicked.getItemMeta().getDisplayName()))) {
           Central.bakeIt(playerexact);
         }
         event.setCancelled(true);
         break;
			   default:
		        event.setCancelled(true);
       }
     }
   }
 }


