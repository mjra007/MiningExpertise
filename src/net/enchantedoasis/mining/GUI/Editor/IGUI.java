/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.enchantedoasis.mining.GUI.Editor;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

/**
 *
 * @author micae
 */
public interface IGUI extends InventoryHolder{
    public void onGUIClick(Player whoClicked, int slot, ItemStack clickedItem
            , InventoryAction action, Inventory inv, InventoryClickEvent e);
}

