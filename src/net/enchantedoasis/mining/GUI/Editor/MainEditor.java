/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.enchantedoasis.mining.GUI.Editor;

import java.util.ArrayList;
import java.util.List;
import net.enchantedoasis.mining.MiningExpertise;
import net.enchantedoasis.mining.ability.LuckyChest;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 *
 * @author micae
 */
public class MainEditor implements IGUI{
    
    final String name = "LuckyChests Editor";

    @Override
    public void onGUIClick(Player whoClicked, int slot, ItemStack clickedItem, InventoryAction action, Inventory inv,InventoryClickEvent e) {
           if(inv.getName().equals(name)){
               if(slot < MiningExpertise.instance.config.luckyChests.size())
               {
                   whoClicked.openInventory(new LuckyChestEditor(MiningExpertise.instance.config.luckyChests.get(slot)).getInventory());
               }
            }
    }

    @Override
    public Inventory getInventory() {
        int size = 27;
       
        Inventory GUI = Bukkit.createInventory(this, size, name);
        
        List<LuckyChest> luckyChests = MiningExpertise.instance.config.luckyChests;
        
        for (int i = 0; i < luckyChests.size(); i++) {
            createItem(Material.CHEST, GUI, i, "§d"+luckyChests.get(i).getName(),"§7Edit "+luckyChests.get(i).getName()+" lucky chest!");
        }
        return GUI;
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
}
