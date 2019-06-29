package net.enchantedoasis.mining.GUI.Editor;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

/**
 *
 * @author micael
 */
public class GUIEH implements Listener{

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {

        if(e.getInventory().getHolder() instanceof IGUI) {
            if(e.getInventory().getName().contains("Add items ") == false){
                e.setCancelled(true);
            }
            IGUI gui = (IGUI) e.getInventory().getHolder();
            gui.onGUIClick((Player)e.getWhoClicked(), e.getRawSlot(), e.getCurrentItem(), e.getAction(), e.getInventory(), e);
        }
    }
}
