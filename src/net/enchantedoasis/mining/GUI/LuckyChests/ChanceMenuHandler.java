 package net.enchantedoasis.mining.GUI.LuckyChests;
 
 import net.enchantedoasis.mining.MiningExpertise;
 import net.enchantedoasis.userData.User;
 import net.milkbowl.vault.economy.Economy;
 import org.bukkit.ChatColor;
 import org.bukkit.entity.Player;
 import org.bukkit.event.inventory.InventoryClickEvent;
 import org.bukkit.inventory.Inventory;
 import org.bukkit.inventory.ItemStack;
 import org.bukkit.inventory.meta.ItemMeta;
 
 public class ChanceMenuHandler implements org.bukkit.event.Listener
 {
   static ItemStack clicked;
   
   @org.bukkit.event.EventHandler
   public void onInventoryClick(InventoryClickEvent event)
   {
     Player playerexact = (Player)event.getWhoClicked();
     clicked = event.getCurrentItem();
     Inventory inventory = event.getInventory();
     if (inventory.getName().equals(ChanceMenu.chance.getName()))
     {
       switch (event.getAction()) {
       case PLACE_ALL: 
         event.setCancelled(true);
         break;
       case PLACE_SOME: 
         event.setCancelled(true);
       case HOTBAR_MOVE_AND_READD: 
         event.setCancelled(true);
         break;
       case PICKUP_SOME: 
       case CLONE_STACK: 
       case COLLECT_TO_CURSOR: 
       case DROP_ALL_SLOT: 
       case DROP_ONE_CURSOR: 
       case DROP_ONE_SLOT: 
       case HOTBAR_SWAP: 
       case MOVE_TO_OTHER_INVENTORY: 
       case NOTHING: 
       case PICKUP_HALF: 
       case PICKUP_ONE: 
       case PLACE_ONE: 
       case SWAP_WITH_CURSOR: 
         User u = (User)User.getUserData().get(playerexact.getUniqueId());
         for (int i = 1; i <= 3; i++) {
           if (ChatColor.stripColor(clicked.getItemMeta().getDisplayName()).equalsIgnoreCase(i + "%")) {
             Short p = Short.valueOf((short)(ChanceMenu.price * i));
             if ((u.setChestChance(Integer.valueOf(i)).booleanValue() & MiningExpertise.econ.getBalance(playerexact) >= ChanceMenu.price * i)) {
               playerexact.sendMessage(MiningExpertise.defaultsymbol + ChatColor.GRAY + "-" + p);
               MiningExpertise.econ.withdrawPlayer(playerexact, p.shortValue());
             } else {
               playerexact.sendMessage(MiningExpertise.defaultsymbol + ChatColor.GRAY + 
                 "You don't have enough money :( or you already bought this");
             }
             ChanceMenu.bakeIt(playerexact);
           }
         }
         if ("Back-".equalsIgnoreCase(ChatColor.stripColor(clicked.getItemMeta().getDisplayName()))) {
           LuckyChestsMeny.bakeIt(playerexact);
         }
         event.setCancelled(true);
         break;
       case DROP_ALL_CURSOR: 
         event.setCancelled(true);
default:
    event.setCancelled(true);
       }
     }
   }
 }


