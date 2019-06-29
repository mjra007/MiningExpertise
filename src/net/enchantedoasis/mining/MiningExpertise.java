package net.enchantedoasis.mining;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import guava10.com.google.common.collect.ImmutableList;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.enchantedoasis.mining.GUI.Ability.AbilityMenuHandler;
import net.enchantedoasis.mining.GUI.Editor.GUIEH;
import net.enchantedoasis.mining.GUI.LuckyChests.ChanceMenuHandler;
import net.enchantedoasis.mining.GUI.LuckyChests.LuckyChestMenuHandler;
import net.enchantedoasis.mining.ability.AutoSmelt;
import net.enchantedoasis.mining.ability.LuckyChest;
import net.enchantedoasis.mining.ability.LuckyDrop;
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

    public static Economy econ = null;
    public static String defaultsymbol = ChatColor.GRAY + "[" + ChatColor.GREEN + "+" + ChatColor.GRAY + "] "
            + ChatColor.RESET;
    public static MiningExpertise instance;
    public static Permission permission = null;
    final Gson gson = new GsonBuilder()
            .excludeFieldsWithoutExposeAnnotation()
            .setPrettyPrinting()
            .create();
    private File configFile;
    public Config config;

    @Override
    public void onEnable() {
        configFile = new File(getDataFolder(), "config.json");
        loadConfiguration();

        if (!configFile.exists()) {
            saveResource(configFile.getName(), false);
        }
        try {
            config = gson.fromJson(new FileReader(configFile), Config.class);
        } catch (FileNotFoundException ex) {
        }
       
        String json = gson.toJson(config, Config.class); // Remember pretty printing? This is needed here.
        
        instance = this;
        setupPermissions();

        registerEvents(this, new Listener[]{new EventHandlers(),
            new AbilityMenuHandler(),
            new LuckyChestMenuHandler(),
            new LuckyDrop(),
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
        ClearLuckyChests();
        String json = gson.toJson(config, Config.class); // Remember pretty printing? This is needed here.
        configFile.delete(); // won't throw an exception, don't worry.

        try {
            Files.write(configFile.toPath(), json.getBytes(), StandardOpenOption.CREATE, StandardOpenOption.WRITE); // java.nio.Files
        } catch (IOException ex) {
            //    Logger.getLogger(TwilightForestBossTweak.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void ClearLuckyChests() {
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

    public ArrayList<WeightedItemStack> getObjectConfig(String path) {
        ArrayList<WeightedItemStack> items = new ArrayList();

        for (String key : getConfig().getStringList(path)) {
            if (key.contains("/")) {
                String[] allData = key.split("/");
                Material item = Material.getMaterial(allData[0]);
                for (int i = 1; i < allData.length; i++) {
                    Short durability = Short.valueOf(allData[i]);
                    Short quantity = 1;
                    if (key.contains("-")) {
                        String[] quantityL = key.split("-");
                        if (quantityL.length > 1) {
                            quantity = Short.valueOf(quantityL[1]);
                        }
                    }
                    items.add(new WeightedItemStack(item.toString(), durability, quantity));
                }
            }
        }
        return items;
    }

    public void changeToOldValues() {

        ArrayList<WeightedItemStack> bloodMagicL
                = getObjectConfig("Chest.BloodMagic.Legendary");
        ArrayList<WeightedItemStack> bloodMagicR
                = getObjectConfig("Chest.BloodMagic.Rare");
        ArrayList<WeightedItemStack> bloodMagicC
                = getObjectConfig("Chest.BloodMagic.Common");
        
        bloodMagicL.forEach(s->s.setWeight(10));
        bloodMagicR.forEach(s->s.setWeight(35));
        bloodMagicC.forEach(s->s.setWeight(60));

        ArrayList<WeightedItemStack> allItems = new ArrayList<>();
        allItems.addAll(bloodMagicL);
        allItems.addAll(bloodMagicR);
        allItems.addAll(bloodMagicC);

        config.getLuckyChest("BloodMagic").changeItems(allItems);
        
        ArrayList<WeightedItemStack> witcheryL
                = getObjectConfig("Chest.Witchery.Legendary");
        ArrayList<WeightedItemStack> witcheryR
                = getObjectConfig("Chest.Witchery.Rare");
        ArrayList<WeightedItemStack> witcheryC
                = getObjectConfig("Chest.Witchery.Common");
        
        witcheryL.forEach(s->s.setWeight(10));
        witcheryR.forEach(s->s.setWeight(35));
        witcheryC.forEach(s->s.setWeight(60));
        
        ArrayList<WeightedItemStack> allItemsW = new ArrayList<>();
        allItemsW.addAll(witcheryL);
        allItemsW.addAll(witcheryR);
        allItemsW.addAll(witcheryC);
        
        
        config.getLuckyChest("Witchery").changeItems(allItemsW);

        
        ArrayList<WeightedItemStack> arsmagica2L
                = getObjectConfig("Chest.ArsMagica2.Legendary");
        ArrayList<WeightedItemStack> arsmagica2R
                = getObjectConfig("Chest.ArsMagica2.Rare");
        ArrayList<WeightedItemStack> arsmagica2C
                = getObjectConfig("Chest.ArsMagica2.Common");
        
        arsmagica2L.forEach(s->s.setWeight(10));
        arsmagica2R.forEach(s->s.setWeight(35));
        arsmagica2C.forEach(s->s.setWeight(60));
        
        ArrayList<WeightedItemStack> allItemsAM = new ArrayList<>();
        allItemsAM.addAll(arsmagica2L);
        allItemsAM.addAll(arsmagica2R);
        allItemsAM.addAll(arsmagica2C);
        
        
        config.getLuckyChest("ArsMagica").changeItems(allItemsAM);

        
        ArrayList<WeightedItemStack> botaniaL
                = getObjectConfig("Chest.Botania.Legendary");
        ArrayList<WeightedItemStack> botaniaR
                = getObjectConfig("Chest.Botania.Rare");
        ArrayList<WeightedItemStack> botaniaC
                = getObjectConfig("Chest.Botania.Common");
      
        botaniaL.forEach(s->s.setWeight(10));
        botaniaR.forEach(s->s.setWeight(35));
        botaniaC.forEach(s->s.setWeight(60));
        
        ArrayList<WeightedItemStack> allItemsBotania = new ArrayList<>();
        allItemsBotania.addAll(botaniaL);
        allItemsBotania.addAll(botaniaR);
        allItemsBotania.addAll(botaniaC);
        
        
        config.getLuckyChest("Botania").changeItems(allItemsBotania);

        ArrayList<WeightedItemStack> thaumcraftL
                = getObjectConfig("Chest.Thaumcraft.Legendary");
        ArrayList<WeightedItemStack> thaumcraftR
                = getObjectConfig("Chest.Thaumcraft.Rare");
        ArrayList<WeightedItemStack> thaumcraftC
                = getObjectConfig("Chest.Thaumcraft.Common");
      
        thaumcraftL.forEach(s->s.setWeight(10));
        thaumcraftR.forEach(s->s.setWeight(35));
        thaumcraftC.forEach(s->s.setWeight(60));
        
        ArrayList<WeightedItemStack> allItemsT = new ArrayList<>();
        allItemsT.addAll(thaumcraftL);
        allItemsT.addAll(thaumcraftR);
        allItemsT.addAll(thaumcraftC);
        
        config.getLuckyChest("Thaumcraft").changeItems(allItemsT);

        ArrayList<WeightedItemStack> forestryL
                = getObjectConfig("Chest.Forestry.Legendary");
        ArrayList<WeightedItemStack> forestryR
                = getObjectConfig("Chest.Forestry.Rare");
        ArrayList<WeightedItemStack> forestryc
                = getObjectConfig("Chest.Forestry.Common");
        
        
        forestryL.forEach(s->s.setWeight(10));
        forestryR.forEach(s->s.setWeight(35));
        forestryc.forEach(s->s.setWeight(60));
        
        ArrayList<WeightedItemStack> allItemsF = new ArrayList<>();
        allItemsF.addAll(forestryL);
        allItemsF.addAll(forestryR);
        allItemsF.addAll(forestryc);
        
        config.getLuckyChest("Forestry").changeItems(allItemsF);
     
        
        
        forestryL.forEach(s->s.setWeight(10));
        forestryR.forEach(s->s.setWeight(35));
        forestryc.forEach(s->s.setWeight(60));
        
        ArrayList<WeightedItemStack> allItemsR = new ArrayList<>();
        allItemsR.addAll(forestryL);
        allItemsR.addAll(forestryR);
        allItemsR.addAll(forestryc);
        allItemsR.addAll(thaumcraftL);
        allItemsR.addAll(thaumcraftR);
        allItemsR.addAll(thaumcraftC);
        allItemsR.addAll(botaniaL);
        allItemsR.addAll(botaniaR);
        allItemsR.addAll(botaniaC);
        allItemsR.addAll(arsmagica2L);
        allItemsR.addAll(arsmagica2R);
        allItemsR.addAll(arsmagica2C);
        allItemsR.addAll(witcheryL);
        allItemsR.addAll(witcheryR);
        allItemsR.addAll(witcheryC);
        allItemsR.addAll(bloodMagicL);
        allItemsR.addAll(bloodMagicR);
        allItemsR.addAll(bloodMagicC);
        
        config.getLuckyChest("Random").changeItems(allItemsR);
        
    }

    public void loadConfiguration() {
        getConfig().options().copyDefaults(true);
        saveConfig();
    }

}
