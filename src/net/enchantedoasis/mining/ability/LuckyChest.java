package net.enchantedoasis.mining.ability;

import com.google.gson.annotations.Expose;
import java.util.ArrayList;
import java.util.List;
import net.enchantedoasis.mining.Ability;
import net.enchantedoasis.mining.Utils;
import net.enchantedoasis.mining.GUI.Ability.AbilityMenu;
import net.enchantedoasis.mining.WeightedItemStack;
import net.enchantedoasis.mining.WeightedList;
import net.enchantedoasis.userData.User;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class LuckyChest extends Ability {

    @Expose
    Integer number_of_items_to_spawn_in_chest;
    @Expose
    Integer chance_of_chest_spawning;
    private WeightedList<WeightedItemStack> weightedListOfItems = new WeightedList<>();
    @Expose
    private ArrayList<WeightedItemStack> chest_items = new ArrayList<>();
    public static List<Location> chests = new ArrayList();

    public LuckyChest(String name, String description, String icon, ArrayList<WeightedItemStack> items, Integer noOfItems, Integer chanceOfSpawningAChest) {
        super(name, description, icon);
        chest_items = items;

        this.number_of_items_to_spawn_in_chest = noOfItems;
        this.chance_of_chest_spawning = chanceOfSpawningAChest;
    }

    public int getNoOfItems() {
        return number_of_items_to_spawn_in_chest;
    }

    public void decreaseNoOfItems(){
        number_of_items_to_spawn_in_chest--;
    }
    
    public void IncreaseNoOfItems(){
        number_of_items_to_spawn_in_chest++;
    }
    
    public int chanceOfSpawningAChest() {
        return chance_of_chest_spawning;
    }
    
    public void IncreaseChanceOfSpawningChest(){
        chance_of_chest_spawning++;
    }
    
    public void DecreaseChanceOfSpawningChest(){
        chance_of_chest_spawning--;
    }

    public ArrayList<WeightedItemStack> getAllItems() {
        return chest_items;
    }

    public WeightedList<WeightedItemStack> getWeightedList() {
        if (weightedListOfItems == null || weightedListOfItems.size() == 0) {
            weightedListOfItems = new WeightedList<>();
            chest_items.stream()
                    .forEach(Item -> {
                        weightedListOfItems.add(Item.getWeight(), Item);
                    });
            return weightedListOfItems;
        } else {
            return weightedListOfItems;
        }
    }

    public void changeItems(List<WeightedItemStack> items){
       this.chest_items = new ArrayList<>(items);
       weightedListOfItems = null;
       
    }
    
    public void changeInventoryItems(Inventory inv) {
        ArrayList<ItemStack> itemsForChest = getRandomItems(number_of_items_to_spawn_in_chest);
        
        for (int i = 0; i < itemsForChest.size(); i++) {
            ItemStack current = itemsForChest.get(i);
            inv.setItem(Utils.random.nextInt(27), current);
        }
    }

    public void bakeIt(Block block, Integer playerChance, Player player, BlockBreakEvent e) {
        ItemStack item = player.getItemInHand();
        Material itemType = item.getType();
        String materialName = itemType.toString();
        //Checks if the player is in a cooldown
        if (Cooldown.isInCooldown(player.getUniqueId(), "luckychest")) {
            System.out.println("In cool down");
            return;
        }
        System.out.println("Triggered");

        if ((player.hasPermission("mining.chest"))
                && (block.getType() == Material.STONE)
                && (Utils.getRandom(0, 100) <= playerChance + chance_of_chest_spawning)
                && (materialName.contains("PICK"))
                && (!materialName.equalsIgnoreCase("THAUMICTINKERER_ICHORPICKGEM"))) {

            //cancel event
            e.setCancelled(true);
            //spawn chest
            spawnChest(block);
            SpawnLuckyDrop(player);
            //set player on a cool down
            Cooldown cooldown = new Cooldown(player.getUniqueId(), "luckychest", 240);
            cooldown.start();
        }
    }

    private void SpawnLuckyDrop(Player player) {
        if (Utils.getRandom(0, 100) < 10) {
            new LuckyDrop().spawnLuckyDrop(player);
        }
    }

    private ArrayList<ItemStack> getRandomItems(Integer noOfItems) {
        ArrayList<ItemStack> itemsForChest = new ArrayList<>();
        for (int itemIndex = 0; itemIndex < noOfItems; itemIndex++) {
            itemsForChest.add(getWeightedList().sampleWithoutReplacement().toItemStack());
        }
        return itemsForChest;
    }

    public void spawnChest(Block block) {
        block.setType(Material.CHEST);
        Chest chest = (Chest) block.getState();
        Inventory inv = chest.getBlockInventory();
        changeInventoryItems(inv);
        block.getLocation().getWorld().playEffect(block.getLocation(), org.bukkit.Effect.WITCH_MAGIC, 10);
        chests.add(chest.getLocation());
    }

    public void clicked(Player p, String permission) {
        User userData = (User) User.getUserData().get(p.getUniqueId());
        if (userData.getAbilitySelected().equals(this.getName())) {
            userData.setAbilitySelected("");
            p.sendMessage(ChatColor.GRAY + "[" + ChatColor.GREEN + "+" + ChatColor.GRAY + "] " + ChatColor.GRAY
                    + getName() + " was unselected");
            return;
        }

        if (userData.getAbilitiesOwned().contains(ChatColor.stripColor(getName()))) {
            userData.setAbilitySelected(getName());
            p.sendMessage(ChatColor.GRAY + "[" + ChatColor.GREEN + "+" + ChatColor.GRAY + "] " + ChatColor.GRAY
                    + getName() + " was selected");
        } else if ((p.hasPermission(permission) & userData.addAbilityOwned(getName()).booleanValue())) {
            userData.setAbilitySelected(getName());
            p.sendMessage(ChatColor.GRAY + "[" + ChatColor.GREEN + "+" + ChatColor.GRAY + "] " + ChatColor.GRAY
                    + getName() + " was selected");
            p.sendMessage(net.enchantedoasis.mining.MiningExpertise.defaultsymbol + ChatColor.GRAY + getName()
                    + " was acquired");
        }
        AbilityMenu.bakeIt(p);
    }

}
