package net.enchantedoasis.mining.GUI.Editor;

import java.util.ArrayList;
import net.enchantedoasis.mining.WeightedItemStack;
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
public class ItemsDisplay implements IGUI {

    private final String name;
    private ArrayList<WeightedItemStack> items;
    private Boolean weightsORstacksize = true;
    private Boolean increase = false;
    private Boolean decrease =false;
    private Boolean delete = false;
    private final int DECREASE_BUTTON_INDEX = 51;
    private final int INCREASE_BUTTON_INDEX = 52;
    private final int DELETE_BUTTON_INDEX = 53;
    private final int WEIGHTS_OR_STACK_SIZE_INDEX = 50;

    public ItemsDisplay(String name, ArrayList<WeightedItemStack> items){
        this.name = name;
        this.items = items;
    }
    
    @Override
    public void onGUIClick(Player whoClicked, int slot, ItemStack clickedItem
            , InventoryAction action, Inventory inv,InventoryClickEvent e) {
        
        //Checks whether an item was dragged into the inventory
        //Adds item to item list and forces gui refresh
        if (action == InventoryAction.DROP_ALL_CURSOR 
                || action== InventoryAction.DROP_ALL_SLOT
                || action == InventoryAction.DROP_ONE_CURSOR 
                || action == InventoryAction.DROP_ONE_SLOT){
            
            ItemStack itemInPlayersHand = whoClicked.getItemInHand();
            
            WeightedItemStack item = new WeightedItemStack(itemInPlayersHand.getType().name(),
                    itemInPlayersHand.getDurability(),
                    Integer.valueOf(itemInPlayersHand.getAmount()).shortValue());
            
            items.add(item);
            whoClicked.openInventory(getInventory());
            return;
        }
        
        if (clickedItem == null || clickedItem.getType().equals(Material.AIR)) {
            return;
        }
        
        switch (slot) {
            case DELETE_BUTTON_INDEX:
                activateDelete();
                break;
            case INCREASE_BUTTON_INDEX:
                activateIncrease();
                break;
            case DECREASE_BUTTON_INDEX:
                activateDecrease();
                break;
            case WEIGHTS_OR_STACK_SIZE_INDEX:
                activateWeightsOrStackSize();
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
            if(index <items.size()){
                GUI.setItem(index, items.get(index).toItemStack());
            }
        }
        
        ItemStack deleteButton = new ItemStack(Material.GLASS,1,(short)14);
        ItemStack increaseButton = new ItemStack(Material.ACTIVATOR_RAIL,1);
        ItemStack decreaseButton =  new ItemStack(Material.RAILS,1);
        
        deleteButton = changeNameItemStack(deleteButton, "Delete Item");
        increaseButton = changeNameItemStack(increaseButton, "Increase Stack");
        decreaseButton = changeNameItemStack(decreaseButton, "Decrease Stack");

        GUI.setItem(53, deleteButton);
        GUI.setItem(52, increaseButton); 
        GUI.setItem(51,decreaseButton);
        GUI.setItem(50, new ItemStack(Material.ARROW,1));
        
        return GUI;
    }
    
    private ItemStack changeNameItemStack(ItemStack stack, String name){
        ItemMeta meta = stack.getItemMeta();
        meta.setDisplayName(name);
        stack.setItemMeta(meta);
        return stack;
    }
    
    private void activateDelete(){
        increase= false;
        delete= false;
        decrease = false;
    }
    private void activateIncrease(){
        increase=true;
        delete= false;
        decrease = false;
    }
    private void activateDecrease(){
        increase=false;
        delete=false;
        decrease = true;
    }
    
    private void activateWeightsOrStackSize(){
        this.weightsORstacksize = !weightsORstacksize;
    }

    private void processItemListClick(ItemStack stack) {
       for(int index=0;index<items.size();index++){
           
            WeightedItemStack item = items.get(index);
            
           if(item.getMaterial() ==stack.getType() && 
                   item.getDurability()==stack.getDurability()){
              
               if(delete){
                   items.set(index, new WeightedItemStack(Material.AIR.name(), (short)0,(short)1));
               }else if(decrease) {
                   if(weightsORstacksize){
                    item.setWeight((item.getQuantity()-1));
                    items.set(index, item);           
                   }else{
                     item.setQuantity((short)(item.getQuantity()-1));
                    items.set(index, item);  
                   }
               }else if (increase){
                    item.setWeight((item.getQuantity()+1));
                    items.set(index, item);           
                   }else{
                     item.setQuantity((short)(item.getQuantity()+1));
                    items.set(index, item);  
                   }
               }
           }
       }
    
    public void openInventory(Player player)
    {
        player.openInventory(getInventory());
    }
}
