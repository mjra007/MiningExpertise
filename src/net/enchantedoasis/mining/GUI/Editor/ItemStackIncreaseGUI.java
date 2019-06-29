package net.enchantedoasis.mining.GUI.Editor;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import net.enchantedoasis.mining.MiningExpertise;
import net.enchantedoasis.mining.Utils;
import static net.enchantedoasis.mining.Utils.toPercentage;
import net.enchantedoasis.mining.WeightedItemStack;
import net.enchantedoasis.mining.ability.LuckyChest;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class ItemStackIncreaseGUI implements IGUI {

    private LuckyChest luckyChest;
    private String name;
    private int pageIndex = 0;

    public ItemStackIncreaseGUI(LuckyChest luckyChest) {
        this.luckyChest = luckyChest;
        name = "Change the item stack:";
    }

    @Override
    public void onGUIClick(Player whoClicked, int slot, ItemStack clickedItem, InventoryAction action, Inventory inv, InventoryClickEvent e) {
        if (inv.getName().equals(name)) {
            if (slot == 49 || slot == 50 || slot == 45) {
                if (clickedItem.getType() != null && clickedItem.getType() != Material.AIR) {

                    switch (slot) {
                        case 50:
                            if (pageIndex + 1 != getTotalNoOfPages() - 1) {
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
                        case 45:
                            whoClicked
                                    .openInventory(
                                            new LuckyChestEditor(luckyChest)
                                                    .getInventory());
                            break;
                    }
                }
            } else {
                if (clickedItem != null && clickedItem.getType() != Material.AIR) {
                    Optional<WeightedItemStack> itemStack = luckyChest.getAllItems().stream()
                            .filter(s -> s.getMaterial() == clickedItem.getType()
                            && s.getDurability() == clickedItem.getDurability())
                            .findFirst();

                    if (itemStack.isPresent() && e.getClick().equals(ClickType.RIGHT)) {
                        if (itemStack.get().getQuantity() - 1 >= 0) {
                            itemStack.get().decreaseItemStack();
                        }
                        whoClicked.playSound(whoClicked.getLocation(), Sound.NOTE_PLING, 10, 1);
                    } else if (itemStack.isPresent() && e.getClick().equals(ClickType.LEFT)) {

                        if (itemStack.get().getQuantity() + 1 <= 64) {
                            itemStack.get().increaseItemStack();
                        }
                        whoClicked.playSound(whoClicked.getLocation(), Sound.NOTE_PLING, 10, 1);

                    }

                    Utils.setInventory(inv, getInventory(pageIndex)
                            .getContents());
                }
            }
        }
    }

    @Override
    public Inventory getInventory() {
        return getInventory(pageIndex);
    }

    public int getTotalNoOfPages() {
        if (luckyChest.getAllItems().size() <= 45) {
            return 1;
        } else {
            return (int) Math.ceil(luckyChest.getAllItems().size() / 45.0);
        }
    }

    public Inventory getInventory(int page) {
        Inventory inv = Bukkit.createInventory(this, 54, name);
        List<WeightedItemStack> items = getItems(page);
        DecimalFormat f = new DecimalFormat("##.00");

        for (int indexOfInventory = 0; indexOfInventory < items.size(); indexOfInventory++) {
            WeightedItemStack item = items.get(indexOfInventory);

            float percentage = (item.getWeight()  / (float) luckyChest.getWeightedList().getTotal());
            int oneInX = (int) Math.ceil(1 / percentage);

            Utils.createItem(item.toItemStack(), inv, indexOfInventory, item.toString()
                    + "\n§3You will find this item in §71 out of " + oneInX + " §7chests! \n§3Percentage: §7" + toPercentage(percentage) + " \n");
       }

        Utils.createItem(Material.ARROW, inv, 50, "§bNext Page", "");
        Utils.createItem(Material.ARROW, inv, 49, "§bBack Page", "");
        Utils.createItem(Material.BONE, inv, 45, "§bGo back to the previous menu", "");

        return inv;
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
}
