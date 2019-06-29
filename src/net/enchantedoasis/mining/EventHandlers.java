package net.enchantedoasis.mining;

import java.util.List;
import net.enchantedoasis.mining.ability.LuckyChest;
import net.enchantedoasis.userData.User;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.block.DoubleChest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;

public class EventHandlers implements org.bukkit.event.Listener {

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        try {
            Player player = event.getPlayer();
            User user = (User) User.getUserData().get(player.getUniqueId());
            if (!user.getAbilitySelected().equals("")) {
                if (user.getAbilitySelected().equalsIgnoreCase("AutoSmelt")) {
                    MiningExpertise.instance.config.automSmelt.bakeIt(event.getBlock(), event.getPlayer());
                } else if (!user.getAbilitySelected().equalsIgnoreCase("Excavation")) {
                    if (user.getAbilitySelected().equalsIgnoreCase("Spawner")) {
                        if ((event.getBlock().getType() == Material.MOB_SPAWNER
                                & event.getPlayer().getItemInHand().getType().toString().contains("PICK"))) {
                            MiningExpertise.instance.config.spawners.bakeIt(event.getBlock(), event.getPlayer());
                            return;
                        }
                    }
                    if (!user.getAbilitySelected().equalsIgnoreCase("Bomb")) {
                        if (user.getAbilitySelected().equalsIgnoreCase("ArsMagica")) {
                            MiningExpertise.instance.config.
                                    getLuckyChest("ArsMagica").
                                    bakeIt(event.getBlock(), user.getChestChance(), player, event);

                        } else if (user.getAbilitySelected().equalsIgnoreCase("BloodMagic")) {
                            MiningExpertise.instance.config.
                                    getLuckyChest("BloodMagic").
                                    bakeIt(event.getBlock(), user.getChestChance(), player, event);

                        } else if (user.getAbilitySelected().equalsIgnoreCase("Thaumcraft")) {
                            MiningExpertise.instance.config.
                                    getLuckyChest("Thaumcraft").
                                    bakeIt(event.getBlock(), user.getChestChance(), player, event);

                        } else if (user.getAbilitySelected().equalsIgnoreCase("Botania")) {
                            MiningExpertise.instance.config.
                                    getLuckyChest("Botania").
                                    bakeIt(event.getBlock(), user.getChestChance(), player, event);
                        } else if (user.getAbilitySelected().equalsIgnoreCase("Witchery")) {
                            MiningExpertise.instance.config.
                                    getLuckyChest("Witchery").
                                    bakeIt(event.getBlock(), user.getChestChance(), player, event);
                        } else if (user.getAbilitySelected().equalsIgnoreCase("Forestry")) {
                            MiningExpertise.instance.config.
                                    getLuckyChest("Forestry").
                                    bakeIt(event.getBlock(), user.getChestChance(), player, event);
                        } else if (user.getAbilitySelected().equalsIgnoreCase("Random")) {
                            MiningExpertise.instance.config.
                                    getLuckyChest("Random").
                                    bakeIt(event.getBlock(), user.getChestChance(), player, event);
                        }
                    }
                }
            }
        } catch (NullPointerException localNullPointerException) {
            localNullPointerException.printStackTrace();
        }
    }

    public static Boolean isInvEmpty(Inventory inv) {
        Boolean empty = Boolean.valueOf(true);
        for (int i = 0; i < inv.getSize(); i++) {
            if (inv.getItem(i) != null) {
                empty = Boolean.valueOf(false);
            }
        }
        return empty;
    }

    public static void isLuckyChest(Inventory inv) {
        if ((inv.getHolder() instanceof Chest)) {
            Chest c = (Chest) inv.getHolder();
            if ((LuckyChest.chests.contains(c.getLocation())) && (isInvEmpty(inv).booleanValue())) {
                c.getBlock().setType(Material.AIR);
                LuckyChest.chests.remove(LuckyChest.chests.indexOf(c.getLocation()));
                c.getLocation().getWorld().playEffect(c.getLocation(), org.bukkit.Effect.EXTINGUISH, 2);
                c.getLocation().getWorld().playEffect(c.getLocation(), org.bukkit.Effect.FLYING_GLYPH, 10);
            }
        }
        if ((inv.getHolder() instanceof DoubleChest)) {
            DoubleChest c = (DoubleChest) inv.getHolder();
            if ((LuckyChest.chests.contains(c.getLocation())) && (isInvEmpty(inv).booleanValue())) {
                Chest left = (Chest) c.getLeftSide();
                Chest right = (Chest) c.getRightSide();
                left.getBlock().setType(Material.AIR);
                right.getBlock().setType(Material.AIR);
                LuckyChest.chests.remove(LuckyChest.chests.indexOf(c.getRightSide()));
                LuckyChest.chests.remove(LuckyChest.chests.indexOf(c.getLeftSide()));
                c.getLocation().getWorld().playEffect(c.getLocation(), org.bukkit.Effect.FLYING_GLYPH, 10);
            }
        }
    }

    @org.bukkit.event.EventHandler
    public void onInv(InventoryCloseEvent event3) {
        isLuckyChest(event3.getInventory());
    }
}
