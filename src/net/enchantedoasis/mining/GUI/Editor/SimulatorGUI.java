package net.enchantedoasis.mining.GUI.Editor;

import net.enchantedoasis.mining.Utils;
import net.enchantedoasis.mining.ability.LuckyChest;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class SimulatorGUI implements IGUI{
    
    private LuckyChest luckyChest;
    private String name;
    
    public SimulatorGUI(LuckyChest luckyChest){
        this.luckyChest = luckyChest;
        name = "LuckyChest Simulator "+ luckyChest.getName();
    }
    
    @Override
    public void onGUIClick(Player whoClicked, int slot, ItemStack clickedItem, InventoryAction action, Inventory inv,InventoryClickEvent e) {
        if (inv.getName().equals(name)) {
            if(slot == 31){
                inv.setContents(getInventory().getContents());
            }
        }
    }

    @Override
    public Inventory getInventory() {
        Inventory inv = Bukkit.createInventory(this, 36, name);
        luckyChest.changeInventoryItems(inv);
        Utils.createItem(Material.CHEST, inv, 31, "Next chest!", "");
        return inv;
    }
    
    
    
    
}
