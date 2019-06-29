 package net.enchantedoasis.mining.GUI.LuckyChests;
 
 import java.util.HashMap;
 import net.enchantedoasis.mining.Ability;
 import net.enchantedoasis.mining.GUI.Ability.AbilityMenu;
import net.enchantedoasis.mining.MiningExpertise;
 import org.bukkit.ChatColor;
 import org.bukkit.entity.Player;
 import org.bukkit.event.EventHandler;
 import org.bukkit.event.Listener;
 import org.bukkit.event.inventory.InventoryClickEvent;
 import org.bukkit.inventory.Inventory;
 import org.bukkit.inventory.ItemStack;
 import org.bukkit.inventory.meta.ItemMeta;
 
 public class LuckyChestMenuHandler implements Listener
 {
   static ItemStack clicked;
   
   @EventHandler
   public void onInventoryClick(InventoryClickEvent event)
   {
     Player playerexact = (Player)event.getWhoClicked();
     clicked = event.getCurrentItem();
     Inventory inventory = event.getInventory();
     if (inventory.getName().equals(LuckyChestsMeny.luckychestsm.getName()))
     {
       switch (event.getAction()) {
       case PLACE_ALL: 
       case PICKUP_SOME: 
       case PICKUP_HALF: 
       case PICKUP_ONE: 
         for (int i = 0; i < MiningExpertise.instance.config.getAbilities().size(); i++) {
           if ((MiningExpertise.instance.config.getAbilities().get(i))
                   .getName()
                   .equalsIgnoreCase(ChatColor.stripColor(clicked.getItemMeta().getDisplayName()))) {
             (MiningExpertise.instance.config.getAbilities().get(i)).clicked(playerexact, "mining");
           }
         }
         if (ChatColor.stripColor(clicked.getItemMeta().getDisplayName()).equalsIgnoreCase("Upgrade your chance")) {
           ChanceMenu.bakeIt(playerexact);
         }
         if (ChatColor.stripColor(clicked.getItemMeta().getDisplayName()).equalsIgnoreCase("Back")) {
           AbilityMenu.bakeIt(playerexact);
         }
         event.setCancelled(true);
         break;
				default:
    			event.setCancelled(true);
       }
     }
   }
 }


