package net.enchantedoasis.mining;

import java.util.ArrayList;
import java.util.HashMap;
import net.enchantedoasis.mining.GUI.Ability.AbilityMenuHandler;
import net.enchantedoasis.mining.GUI.Editor.GUIEH;
import net.enchantedoasis.mining.GUI.LuckyChests.ChanceMenuHandler;
import net.enchantedoasis.mining.GUI.LuckyChests.LuckyChestMenuHandler;
import net.enchantedoasis.mining.ability.AutoSmelt;
import net.enchantedoasis.mining.ability.LuckyChest;
import net.enchantedoasis.mining.ability.Spawners;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.block.DoubleChest;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;

public class MiningExpertise extends org.bukkit.plugin.java.JavaPlugin {

    public static LuckyChest bloodMagic = null;
    public static LuckyChest thaumcraft = null;
    public static LuckyChest witchery = null;
    public static LuckyChest arsMagica = null;
    public static LuckyChest botania = null;
    public static LuckyChest forestry = null;
    public static LuckyChest random;
    public static AutoSmelt autosmelt = null;
    public static Economy econ = null;
    public static String defaultsymbol = ChatColor.GRAY + "[" + ChatColor.GREEN + "+" + ChatColor.GRAY + "] "
            + ChatColor.RESET;
    public static Plugin p;
    public static Spawners spawners = null;
    public static Permission permission = null;

    @Override
    public void onEnable() {
        p = this;
        loadConfiguration();
        setupPermissions();
        createChests();
        createAutosmelt();
        createSpawners();
        registerEvents(this, new Listener[]{new EventHandlers()
                , new AbilityMenuHandler(),
            new LuckyChestMenuHandler(),
            new ChanceMenuHandler(),
            new GUIEH()});
        getCommand("mining").setExecutor(new net.enchantedoasis.mining.GUI.Ability.AbilityCommand());

        if (!setupEconomy()) {
            Bukkit.getLogger().severe(String.format("[%s] - Disabled due to no Vault dependency found!",
                    new Object[]{getDescription().getName()}));
            getServer().getPluginManager().disablePlugin(this);
        }
    }

    @Override
    public void onDisable() {
        for (int i = 0; i < LuckyChest.chests.size(); i++) {
            Location loc = (Location) LuckyChest.chests.get(i);
            if ((loc.getBlock() instanceof Chest)) {
                loc.getBlock().setType(Material.AIR);
            } else if ((loc.getBlock() instanceof DoubleChest)) {
                DoubleChest dc = (DoubleChest) loc.getBlock();
                Chest left = (Chest) dc.getLeftSide();
                Chest right = (Chest) dc.getRightSide();
                left.getBlock().setType(Material.AIR);
                right.getBlock().setType(Material.AIR);
            }
        }
    }

    private boolean setupPermissions() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Permission> permissionProvider = getServer().getServicesManager()
                .getRegistration(Permission.class);
        if (permissionProvider != null) {
            permission = (Permission) permissionProvider.getProvider();
        }
        return permission != null;
    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = (Economy) rsp.getProvider();
        return econ != null;
    }

    public static void registerEvents(Plugin plugin, Listener... listeners) {
        Listener[] arrayOfListener;
        int j = (arrayOfListener = listeners).length;
        for (int i = 0; i < j; i++) {
            Listener listener = arrayOfListener[i];
            Bukkit.getServer().getPluginManager().registerEvents(listener, plugin);
        }
    }

    public void createAutosmelt() {
        autosmelt = new AutoSmelt("AutoSmelt",
                ChatColor.GRAY + "Smelts your ores instantly" + "\n" + "\n" + ChatColor.DARK_GRAY + "Requires" + ChatColor.GREEN + " VIP" + ChatColor.YELLOW + "+", Material.COAL_ORE, getOresMaterial());
    }

    public void createSpawners() {
        spawners = new Spawners("Spawner",
                ChatColor.GRAY + "Break spawners with your average pickaxe" + "\n" + "\n" + ChatColor.DARK_GRAY + "Requires" + ChatColor.AQUA + " MVP", Material.MOB_SPAWNER);
    }

    @SuppressWarnings("unchecked")
    public void createChests() {
        forestry = new LuckyChest("Forestry",
                ChatColor.GRAY + "Find chests with different" + "\n" + ChatColor.GRAY
                + "kinds of loot in them while mining" + "\n" + "\n" + ChatColor.DARK_GRAY + "Requires" + ChatColor.RED + " DONOR RANK", Material.CHEST, getObjectConfig("Chest.Forestry.Legendary"), getObjectConfig("Chest.Forestry.Common"),
                getObjectConfig("Chest.Forestry.Rare"), 2, 10);

        bloodMagic = new LuckyChest("BloodMagic",
                ChatColor.GRAY + "Find chests with different" + "\n" + ChatColor.GRAY
                + "kinds of loot in them while mining" + "\n" + "\n" + ChatColor.DARK_GRAY + "Requires" + ChatColor.RED + " DONOR RANK", Material.CHEST, getObjectConfig("Chest.BloodMagic.Legendary"), getObjectConfig("Chest.BloodMagic.Common"),
                getObjectConfig("Chest.BloodMagic.Rare"), 2, 10);

        thaumcraft = new LuckyChest("Thaumcraft",
                ChatColor.GRAY + "Find chests with different" + "\n" + ChatColor.GRAY
                + "kinds of loot in them while mining" + "\n" + "\n" + ChatColor.DARK_GRAY + "Requires" + ChatColor.RED + " DONOR RANK", Material.CHEST, getObjectConfig("Chest.Thaumcraft.Legendary"), getObjectConfig("Chest.Thaumcraft.Common"),
                getObjectConfig("Chest.Thaumcraft.Rare"), 3, 10);
        witchery = new LuckyChest("Witchery",
                ChatColor.GRAY + "Find chests with different" + "\n" + ChatColor.GRAY
                + "kinds of loot in them while mining" + "\n" + "\n" + ChatColor.DARK_GRAY + "Requires" + ChatColor.RED + " DONOR RANK", Material.CHEST, getObjectConfig("Chest.Witchery.Legendary"), getObjectConfig("Chest.Witchery.Common"),
                getObjectConfig("Chest.Witchery.Rare"), 3, 10);
        arsMagica = new LuckyChest("ArsMagica",
                ChatColor.GRAY + "Find chests with different" + "\n" + ChatColor.GRAY
                + "kinds of loot in them while mining" + "\n" + "\n" + ChatColor.DARK_GRAY + "Requires" + ChatColor.RED + " DONOR RANK", Material.CHEST, getObjectConfig("Chest.ArsMagica.Legendary"), getObjectConfig("Chest.ArsMagica.Common"),
                getObjectConfig("Chest.ArsMagica.Rare"), 3, 10);
        botania = new LuckyChest("Botania",
                ChatColor.GRAY + "Find chests with different" + "\n" + ChatColor.GRAY
                + "kinds of loot in them while mining" + "\n" + "\n" + ChatColor.DARK_GRAY + "Requires" + ChatColor.RED + " DONOR RANK", Material.CHEST, getObjectConfig("Chest.Botania.Legendary"), getObjectConfig("Chest.Botania.Common"),
                getObjectConfig("Chest.Botania.Rare"), 3, 10);

        ArrayList<WeightedItemStack> common = MergeItemLists(getObjectConfig("Chest.Forestry.Common"),
                getObjectConfig("Chest.BloodMagic.Common"),
                getObjectConfig("Chest.Thaumcraft.Common"),
                getObjectConfig("Chest.Witchery.Common"),
                getObjectConfig("Chest.ArsMagica.Common"),
                getObjectConfig("Chest.Botania.Common"));

        ArrayList<WeightedItemStack> rare = MergeItemLists(getObjectConfig("Chest.Forestry.Rare"),
                getObjectConfig("Chest.BloodMagic.Rare"),
                getObjectConfig("Chest.Thaumcraft.Rare"),
                getObjectConfig("Chest.Witchery.Rare"),
                getObjectConfig("Chest.ArsMagica.Rare"),
                getObjectConfig("Chest.Botania.Rare"));

        ArrayList<WeightedItemStack> legendary = MergeItemLists(getObjectConfig("Chest.Forestry.Legendary"),
                getObjectConfig("Chest.BloodMagic.Legendary"),
                getObjectConfig("Chest.Thaumcraft.Legendary"),
                getObjectConfig("Chest.Witchery.Legendary"),
                getObjectConfig("Chest.ArsMagica.Legendary"),
                getObjectConfig("Chest.Botania.Legendary"));

        random = new LuckyChest("Random",
                ChatColor.GRAY + "Find chests with different" + "\n" + ChatColor.GRAY
                + "kinds of loot in them while mining. Includes all LC mods' loot" + "\n" + "\n" + ChatColor.DARK_GRAY + "Requires" + ChatColor.RED + " DONOR RANK", Material.CHEST, legendary, common,
                rare, 3, 10);
    }

    public ArrayList<WeightedItemStack> MergeItemLists(ArrayList<WeightedItemStack>... lists) {
        ArrayList<WeightedItemStack> newList = new ArrayList();

        for (ArrayList<WeightedItemStack> list : lists) {
            for (WeightedItemStack item : list) {
                if (!newList.contains(item)) {
                    newList.add(item);
                }
            }
        }
        return newList;
    }

    public HashMap<Material, Material> getOresMaterial() {
        HashMap<Material, Material> ores = new HashMap();
        for (String key : getConfig().getConfigurationSection("OresIngot").getKeys(false)) {
            String ingot = getConfig().getString("OresIngot." + key);
            ores.put(Material.getMaterial(key), Material.getMaterial(ingot));
        }
        return ores;
    }

    public ArrayList<WeightedItemStack> getObjectConfig(String path) {
        ArrayList<WeightedItemStack> items = new ArrayList();

        for (String key : getConfig().getStringList(path)) {
            if (key.contains("/")) {
                String[] allData = key.split("/");
                Material item = Material.getMaterial(allData[0]);
                for (int i = 1; i < allData.length; i++) {
                    Short durability = Short.valueOf(allData[i]);
                    Short quantity = 1;
                    if(key.contains("-")){
                        String[] quantityL = key.split("-");
                        if(quantityL.length>1){
                            quantity = Short.valueOf(quantityL[1]);
                        }
                    }
                    items.add(new WeightedItemStack(item, durability,quantity));
                }
            }
        }
        return items;
    }

    public void loadConfiguration() {
        getConfig().options().copyDefaults(true);
        saveConfig();
    }
}
