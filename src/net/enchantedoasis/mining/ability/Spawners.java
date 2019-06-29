 package net.enchantedoasis.mining.ability;
 
;
 import net.enchantedoasis.mining.Ability;
 import net.enchantedoasis.mining.GUI.Ability.AbilityMenu;
 import net.enchantedoasis.mining.MiningExpertise;
 import net.enchantedoasis.userData.User;
 import org.bukkit.ChatColor;
 import org.bukkit.Chunk;
 import org.bukkit.Location;
 import org.bukkit.Material;
 import org.bukkit.World;
 import org.bukkit.block.Block;
 import org.bukkit.block.BlockState;
 import org.bukkit.block.CreatureSpawner;
 import org.bukkit.entity.Player;
 import org.bukkit.inventory.ItemStack;
 import org.bukkit.inventory.meta.ItemMeta;
 
 
 public class Spawners
   extends Ability
 {
   public Spawners(String n, String d, String icon2)
   {
     super(n, d, icon2);
   }
   
   @Override
   public void clicked(Player p, String permission) {
     User userData = (User)User.getUserData().get(p.getUniqueId());
     if (p.hasPermission("miningexpertise.spawner")) {
       if (userData.getAbilitiesOwned().contains(ChatColor.stripColor(getName()))) {
         userData.setAbilitySelected(getName());
         p.sendMessage(ChatColor.GRAY + "[" + ChatColor.GREEN + "+" + ChatColor.GRAY + "] " + ChatColor.GRAY + getName() + " was selected");
         System.out.println("Ability was changed to " + getName());
       } else if (p.hasPermission(permission))
       {
         userData.addAbilityOwned(getName());
         p.sendMessage(MiningExpertise.defaultsymbol + ChatColor.GRAY + getName() + " was acquired");
       }
       AbilityMenu.bakeIt(p);
     } else {
       p.sendMessage(MiningExpertise.defaultsymbol + ChatColor.GRAY + "You don't have permission for this");
     }
   }
   
   public void isThereDangerousBlock(Location loc) {
     Chunk chunk = loc.getChunk();
     
     checkChunkforTileEntity(chunk, "ARTIFACTS_ANTI_BUILDER");
     int[] offset = { -1, 0, 1 };
     
     int baseX = chunk.getX();
     int baseZ = chunk.getZ();
     World world = loc.getWorld();
     int[] arrayOfInt1;
     int j = (arrayOfInt1 = offset).length; for (int i = 0; i < j; i++) { int x = arrayOfInt1[i];
       int[] arrayOfInt2; int m = (arrayOfInt2 = offset).length; for (int k = 0; k < m; k++) { int z = arrayOfInt2[k];
         Chunk chunkNew = world.getChunkAt(baseX + x, baseZ + z);
         checkChunkforTileEntity(chunkNew, "ARTIFACTS_ANTI_BUILDER");
       }
     }
   }
   
   public void checkChunkforTileEntity(Chunk chunk, String nameEntity)
   {
     BlockState[] arrayOfBlockState;
     int j = (arrayOfBlockState = chunk.getTileEntities()).length; for (int i = 0; i < j; i++) { BlockState te = arrayOfBlockState[i];
       if (te.getBlock().getType().toString().equalsIgnoreCase(nameEntity)) {
         te.getBlock().setType(Material.AIR);
       }
     }
   }
   
   public void bakeIt(Block block, Player player) {
     ItemStack item = player.getItemInHand();
     System.out.println(block.toString());
     if ((block.getType() == Material.MOB_SPAWNER) && (item.getType().toString().contains("PICK"))) {
       Location location = block.getLocation();
       isThereDangerousBlock(location);
       CreatureSpawner creatureSpawner = (CreatureSpawner)block.getState();
       ItemStack spawnerItem = new ItemStack(block.getType(), 1, creatureSpawner.getCreatureType().getTypeId());
       ItemMeta spawnerMeta = spawnerItem.getItemMeta();
       spawnerMeta.setDisplayName(ChatColor.RESET + creatureSpawner.getCreatureTypeName() + " Spawner");
       block.setType(Material.AIR);
       location.getWorld().dropItemNaturally(location, spawnerItem);
       player.giveExp(0);
     }
   }
 }


