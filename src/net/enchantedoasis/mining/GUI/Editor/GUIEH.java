/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.enchantedoasis.mining.GUI.Editor;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

/**
 *
 * @author micae
 */
public class GUIEH implements Listener{

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        if(e.getInventory().getHolder() instanceof IGUI) {
            e.setCancelled(true);
            IGUI gui = (IGUI) e.getInventory().getHolder();
            gui.onGUIClick((Player)e.getWhoClicked(), e.getRawSlot(), e.getCurrentItem());
        }  
    }
}
