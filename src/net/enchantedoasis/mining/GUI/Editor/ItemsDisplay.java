/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.enchantedoasis.mining.GUI.Editor;

import java.util.ArrayList;
import net.enchantedoasis.mining.ItemList;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

/**
 *
 * @author micae
 */
public class ItemsDisplay implements IGUI {

    private final String name;
    private ArrayList<ItemList> items;
    private Boolean increase = true;
    private Boolean decrease =true;
    private Boolean delete = false;
    
    public ItemsDisplay(String name, ArrayList<ItemList> items){
        this.name = name;
        this.items=items;
    }
    
    @Override
    public void onGUIClick(Player whoClicked, int slot, ItemStack clickedItem) {
        if (clickedItem == null || clickedItem.getType().equals(Material.AIR)) {
            return;
        }
        switch (slot) {
            case 53:
                activateDelete();
                break;
            case 52:
                activateIncrease();
                break;
            case 51:
                activateDecrease();
                break;
            default:
                processItemListClick(clickedItem);
                break;
        }
        whoClicked.openInventory(getInventory());
    }

    @Override
    public Inventory getInventory() {
        int size = 54;
        Inventory GUI = Bukkit.createInventory(this, size, name);
        
        for(int index=0;index<size-4;index++){
            GUI.setItem(index, items.get(index).toItemStack());
        }
        
        GUI.setItem(53, new ItemStack(Material.GLASS,1,(short)14));
        GUI.setItem(52, new ItemStack(Material.ACTIVATOR_RAIL,1)); 
        GUI.setItem(51, new ItemStack(Material.RAILS,1));
        GUI.setItem(50, new ItemStack(Material.ARROW,1));
        return GUI;
    }
    
    private void activateDelete(){
        increase=false;
        delete=false;
        decrease = false;
    }
    private void activateIncrease(){
        increase=true;
        delete=false;
        decrease = false;
    }
    private void activateDecrease(){
        increase=false;
        delete=false;
        decrease = true;
    }

    private void processItemListClick(ItemStack stack) {
       for(int index=0;index<items.size();index++){
           
            ItemList item = items.get(index);
            
           if(item.getMaterial() ==stack.getType() && 
                   item.getDurability()==stack.getDurability()){
              
               if(delete){
                   items.remove(item);
               }else if(decrease) {
                    item.setQuantity((short)(item.getQuantity()-1));
                    items.set(index, item);
               }else if (increase){
                   item.setQuantity((short)(item.getQuantity()+1));
                    items.set(index, item);
               }
           }
       }
    }
}
