package net.enchantedoasis.mining.GUI.Editor;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import net.enchantedoasis.mining.MiningExpertise;
import net.enchantedoasis.mining.Utils;
import static net.enchantedoasis.mining.Utils.toPercentage;
import net.enchantedoasis.mining.WeightedItemStack;
import net.enchantedoasis.mining.ability.LuckyChest;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class AddItemsMenu implements IGUI {

    private LuckyChest luckyChest;
    private String name;
    private int pageIndex;

    public AddItemsMenu(LuckyChest luckyChest) {
        this.luckyChest = luckyChest;
        name = "Add items " + luckyChest.getName();
        pageIndex = 0;
    }

    @Override
    public void onGUIClick(Player whoClicked, int slot, ItemStack clickedItem, InventoryAction action, Inventory inv, InventoryClickEvent e) {
        if (inv.getName().equals(name)) {
            if (slot == 45 || slot == 50 || slot == 48 || slot == 49) {
                if (clickedItem.getType() != null && clickedItem.getType() != Material.AIR) {
                    switch (slot) {
                        case 50:
                            if (pageIndex < getNoOfPages() - 1) {
                                Utils.setInventory(inv, getInventory(++pageIndex)
                                        .getContents());
                            } else {
                                whoClicked.sendMessage(MiningExpertise.defaultsymbol + "§7You are on the last page!");
                            }
                            break;
                        case 49:
                            if (pageIndex - 1 >= 0) {
                                Utils.setInventory(inv, getInventory(--pageIndex)
                                        .getContents());
                            } else {
                                whoClicked.sendMessage(MiningExpertise.defaultsymbol + "§7You are on the first page!");
                            }
                            break;
                        case 48:
                            saveItems(inv);
                            whoClicked.sendMessage(MiningExpertise.defaultsymbol + "§7Items saved!");
                            break;
                        case 45:
                            whoClicked
                                    .openInventory(
                                            new LuckyChestEditor(luckyChest)
                                                    .getInventory());
                            break;
                    }
                    e.setCancelled(true);
                }

            }
        }
    }

    @Override
    public Inventory getInventory() {
        return getInventory(pageIndex);
    }

    public Inventory getInventory(int page) {
        Inventory inv = Bukkit.createInventory(this, 54, name);
        List<WeightedItemStack> items = getItems(page);

        for (int indexOfInventory = 0; indexOfInventory < items.size(); indexOfInventory++) {
            WeightedItemStack item = items.get(indexOfInventory);

            float percentage = (item.getWeight() / (float) luckyChest.getWeightedList().getTotal());
            int oneInX = (int) Math.ceil(1 / percentage);

            Utils.createItem(item.toItemStack(), inv, indexOfInventory, item.toString()
                    + "\n§3You will find this item in §71 out of " + oneInX + " §7chests! \n§3Percentage: §7" + toPercentage(percentage) + " \n");
        }

        Utils.createItem(Material.ARROW, inv, 50, "§dNext Page", "");
        Utils.createItem(Material.ARROW, inv, 49, "§dBack Page", "");
        Utils.createItem(Material.BOOK, inv, 48, "§dSave Changes", "§7Whenever you change page or leave the GUI save first!");
        Utils.createItem(Material.BONE, inv, 45, "§dGo back to the previous menu", "");

        return inv;
    }

    public int getNoOfPages() {
        if (luckyChest.getAllItems().size() <= 45) {
            return 1;
        } else {
            return (int) Math.ceil(luckyChest.getAllItems().size() / 45.0);
        }
    }

    public List<WeightedItemStack> getItems(int page) {
        int maxNoOfItemsPerPage = 45;
        int startPos = page * maxNoOfItemsPerPage;
        int sizeToTake = 0;

        if (luckyChest.getAllItems().size() <= maxNoOfItemsPerPage) {
            sizeToTake = luckyChest.getAllItems().size();
        } else {
            sizeToTake = luckyChest.getAllItems().size() - startPos <= 45
                    ? luckyChest.getAllItems().size() - startPos
                    : 45;
        }

        return luckyChest
                .getAllItems()
                .stream()
                .skip(startPos)
                .limit(sizeToTake)
                .collect(Collectors.toList());
    }

    private void saveItems(Inventory inv) {

        List<WeightedItemStack> originalItemsForPage = getItems(pageIndex);
        System.out.println("original items for page size: " + originalItemsForPage.size());
        List<WeightedItemStack> currentItemsOnPage = Arrays.asList(inv.getContents()).stream()
                .limit(45)
                .filter(s -> s != null)
                .map(s -> new WeightedItemStack(s.getType().toString(), s.getDurability(), (short) 1))
                .collect(Collectors.toList());

        System.out.println("current Items on page: " + currentItemsOnPage.size());

//        //adding new items
//        currentItemsOnPage.stream().filter(newItemStack -> luckyChest.getAllItems().contains(newItemStack) == false)
//                .forEach(itemToBeAdded -> luckyChest.getAllItems().add(itemToBeAdded));
//        
        for (int itemIndex = 0; itemIndex < currentItemsOnPage.size(); itemIndex++) {
            WeightedItemStack newItemStack = currentItemsOnPage.get(itemIndex);
            if (luckyChest.getAllItems().contains(newItemStack) == false) {
                luckyChest.getAllItems().add(newItemStack);
            }
        }

        for (int i = 0; i < originalItemsForPage.size(); i++) {
            WeightedItemStack oldItem = originalItemsForPage.get(i);
            if (currentItemsOnPage.contains(oldItem) == false) {
                luckyChest.getAllItems().remove(oldItem);
            }
        }

//        //removing items no longer in the gui
//        originalItemsForPage.stream().filter(oldItemStack -> currentItemsOnPage.contains(oldItemStack) == false)
//                .forEach(itemToBeRemoved -> luckyChest.getAllItems().remove(itemToBeRemoved));
    }
}
