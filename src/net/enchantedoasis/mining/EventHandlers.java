 package net.enchantedoasis.mining;
 
 import java.util.List;
 import net.enchantedoasis.mining.ability.LuckyChest;
 import net.enchantedoasis.userData.User;
 import org.bukkit.Location;
 import org.bukkit.Material;
 import org.bukkit.block.Block;
 import org.bukkit.block.Chest;
 import org.bukkit.block.DoubleChest;
 import org.bukkit.entity.Player;
 import org.bukkit.event.block.BlockBreakEvent;
 import org.bukkit.event.inventory.InventoryCloseEvent;
 import org.bukkit.inventory.Inventory;
 
 public class EventHandlers implements org.bukkit.event.Listener
 {
   @org.bukkit.event.EventHandler
   public void onBlockBreak(BlockBreakEvent event)
   {
     try
     {
       Player player = event.getPlayer();
       User user = (User)User.getUserData().get(player.getUniqueId());
       
       if (!user.getAbilitySelected().equals("")) {
         if (user.getAbilitySelected().equalsIgnoreCase("AutoSmelt"))
         {
           MiningExpertise.autosmelt.bakeIt(event.getBlock(), event.getPlayer());
         } else if (!user.getAbilitySelected().equalsIgnoreCase("Excavation"))
         {
           if (user.getAbilitySelected().equalsIgnoreCase("Spawner")) if ((event.getBlock().getType() == Material.MOB_SPAWNER & event.getPlayer().getItemInHand().getType().toString().contains("PICK"))) {
               MiningExpertise.spawners.bakeIt(event.getBlock(), event.getPlayer());
               return; } if (!user.getAbilitySelected().equalsIgnoreCase("Bomb"))
           {
             if (user.getAbilitySelected().equalsIgnoreCase("ArsMagica")) {
               MiningExpertise.arsMagica.bakeIt(event.getBlock(), user.getChestChance(), player, event);
 
             }
             else if (user.getAbilitySelected().equalsIgnoreCase("BloodMagic")) {
               MiningExpertise.bloodMagic.bakeIt(event.getBlock(), user.getChestChance(), player, event);
 
             }
             else if (user.getAbilitySelected().equalsIgnoreCase("Thaumcraft")) {
               MiningExpertise.thaumcraft.bakeIt(event.getBlock(), user.getChestChance(), player, event);
 
             }
             else if (user.getAbilitySelected().equalsIgnoreCase("Botania")) {
               MiningExpertise.botania.bakeIt(event.getBlock(), user.getChestChance(), player, event);
 
             }
             else if (user.getAbilitySelected().equalsIgnoreCase("Witchery")) {
               MiningExpertise.witchery.bakeIt(event.getBlock(), user.getChestChance(), player, event);
             }
					  else if (user.getAbilitySelected().equalsIgnoreCase("Forestry")) {
               MiningExpertise.forestry.bakeIt(event.getBlock(), user.getChestChance(), player, event);
             }
					  else if (user.getAbilitySelected().equalsIgnoreCase("Random")){
						MiningExpertise.random.bakeIt(event.getBlock(), user.getChestChance(), player, event);
					  }
           }
         }
       }
     }
     catch (NullPointerException localNullPointerException) {}
   }
   
 
 
 
   public static Boolean isInvEmpty(Inventory inv)
   {
     Boolean empty = Boolean.valueOf(true);
     for (int i = 0; i < inv.getSize(); i++) {
       if (inv.getItem(i) != null) {
         empty = Boolean.valueOf(false);
       }
     }
     return empty;
   }
   
   public static void isLuckyChest(Inventory inv) {
     if ((inv.getHolder() instanceof Chest)) {
       Chest c = (Chest)inv.getHolder();
       if ((LuckyChest.chests.contains(c.getLocation())) && (isInvEmpty(inv).booleanValue())) {
         c.getBlock().setType(Material.AIR);
         LuckyChest.chests.remove(LuckyChest.chests.indexOf(c.getLocation()));
         c.getLocation().getWorld().playEffect(c.getLocation(), org.bukkit.Effect.EXTINGUISH, 2);
         c.getLocation().getWorld().playEffect(c.getLocation(), org.bukkit.Effect.FLYING_GLYPH, 10);
       }
     }
     if ((inv.getHolder() instanceof DoubleChest)) {
       DoubleChest c = (DoubleChest)inv.getHolder();
       if ((LuckyChest.chests.contains(c.getLocation())) && (isInvEmpty(inv).booleanValue())) {
         Chest left = (Chest)c.getLeftSide();
         Chest right = (Chest)c.getRightSide();
         left.getBlock().setType(Material.AIR);
         right.getBlock().setType(Material.AIR);
         LuckyChest.chests.remove(LuckyChest.chests.indexOf(c.getRightSide()));
         LuckyChest.chests.remove(LuckyChest.chests.indexOf(c.getLeftSide()));
         c.getLocation().getWorld().playEffect(c.getLocation(), org.bukkit.Effect.FLYING_GLYPH, 10);
       }
     }
   }
   
 
   @org.bukkit.event.EventHandler
   public void onInv(InventoryCloseEvent event3)
   {
     isLuckyChest(event3.getInventory());
   }
 }


