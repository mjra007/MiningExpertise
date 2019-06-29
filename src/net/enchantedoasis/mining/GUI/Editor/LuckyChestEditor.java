package net.enchantedoasis.mining.GUI.Editor;

import java.util.ArrayList;
import java.util.List;
import net.enchantedoasis.mining.ability.LuckyChest;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import static org.bukkit.event.inventory.ClickType.LEFT;
import static org.bukkit.event.inventory.ClickType.RIGHT;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 *
 * @author micae
 */
public class LuckyChestEditor implements IGUI {

    String name = "Editor";
    LuckyChest luckyChest;

    public LuckyChestEditor(LuckyChest chest) {
        this.luckyChest = chest;
        name += " "+luckyChest.getName();
    }

    @Override
    public void onGUIClick(Player whoClicked, int slot, ItemStack clickedItem, InventoryAction action, Inventory inv, InventoryClickEvent e) {

        if (inv.getName().equals(name)) {

            if (clickedItem == null || clickedItem.getType().equals(Material.AIR)) {
                return;
            }

            switch (clickedItem.getType()) {
                case ANVIL:
                    whoClicked.openInventory(new AddItemsMenu(luckyChest).getInventory());
                    break;
                case BLAZE_POWDER:
                    whoClicked.openInventory(new ItemSpawnChanceGUI(luckyChest).getInventory());
                    break;
                case COMPASS:
                    whoClicked.openInventory(new ItemStackIncreaseGUI(luckyChest).getInventory());
                    break;
                case ENDER_CHEST:
                    whoClicked.openInventory(new SimulatorGUI(luckyChest).getInventory());
                    break;
                case ARROW:
                    whoClicked.openInventory(new MainEditor().getInventory());
                    break;
                case ITEM_FRAME:
                    if(e.getClick() == LEFT){
                        luckyChest.IncreaseNoOfItems();
                    }else if(e.getClick() == RIGHT){
                        luckyChest.decreaseNoOfItems();
                    }
                    break;
                case ENDER_PEARL:
                    if(e.getClick() == LEFT){
                        luckyChest.IncreaseChanceOfSpawningChest();
                    }else if(e.getClick() == RIGHT){
                        luckyChest.DecreaseChanceOfSpawningChest();
                    }
                    break;
            }
                                inv.setContents(this.getInventory().getContents());

        }

    }

    @Override
    public Inventory getInventory() {
        int size = 45;
        Inventory GUI = Bukkit.createInventory(this, size, name);
        createItem(Material.ANVIL, GUI, 10, "§dAdd or remove items!", "§7Drag items and remove them from the inventory! \nDO NOT FORGET TO HIT SAVE!\neven when changing pages\n\n");
        createItem(Material.BLAZE_POWDER, GUI, 12, "§dChance of item spawning!", "§7LEFT-CLICK to increase chance\n§7RIGHT-CLICK to decrease chance\n\n");
        createItem(Material.COMPASS, GUI, 14, "§dChange size of item stack!", "§7LEFT-CLICK to increase stack\n§7RIGHT-CLICK to decrease stack\n\n");
        createItem(Material.ENDER_CHEST, GUI, 16, "§dSimulate results of chest!", "§7Lucky chests on demand!\n\n");
        createItem(Material.ARROW, GUI, 40, "§dGo back!", "");
        createItem(Material.ENDER_PEARL, GUI,29, "§dChance of lucky Chest spawning", "§7LEFT-CLICK to increase chance\n§7RIGHT-CLICK to decrease chance" +"\n§3Chance Of Spawning A Chest: §7"+ luckyChest.chanceOfSpawningAChest()+"§7%");
        createItem(Material.ITEM_FRAME, GUI,33, "§dChange number of items in chest", "§7LEFT-CLICK to increase \n§7RIGHT-CLICK to decrease"
                +"\n§3No of items to spawn: §7"+luckyChest.getNoOfItems()+"§7%");
        createItem(Material.CHEST, GUI, 31, "§dInformation",
                "\n§aName: §7"+luckyChest.getName()+
                        "\n§aDescription: §7"+luckyChest.getDescription()
                +"\n§aChance Of Spawning A Chest: §7"+ luckyChest.chanceOfSpawningAChest()
                +"\n                 §aUpgrade 1%: §7"+(int)(luckyChest.chanceOfSpawningAChest() +1)
                +"\n                 §aUpgrade 2%: §7"+(int)(luckyChest.chanceOfSpawningAChest() +2)
                +"\n                 §aUpgrade 3%: §7"+(int)(luckyChest.chanceOfSpawningAChest() +4)
                +"\n§aNo of items to spawn: §7"+luckyChest.getNoOfItems());
        return GUI;
    }

    public static void createItem(Material material, Inventory inv, int Slot, String name, String lore) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        List<String> Lore = new ArrayList();
        String[] arrayOfString;
        int j = (arrayOfString = lore.split("\n")).length;
        for (int i = 0; i < j; i++) {
            String loresplit = arrayOfString[i];
            Lore.add(loresplit);
        }
        meta.setLore(Lore);
        item.setItemMeta(meta);
        inv.setItem(Slot, item);
    }
}
