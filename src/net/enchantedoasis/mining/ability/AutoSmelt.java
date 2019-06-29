 package net.enchantedoasis.mining.ability;
 
import com.google.gson.annotations.Expose;
 import java.util.HashMap;
 import net.enchantedoasis.mining.Ability;
 import net.enchantedoasis.userData.User;
 import org.bukkit.ChatColor;
 import org.bukkit.Chunk;
 import org.bukkit.Location;
 import org.bukkit.Material;
 import org.bukkit.World;
 import org.bukkit.block.Block;
 import org.bukkit.block.BlockState;
 import org.bukkit.entity.Player;
 import org.bukkit.inventory.ItemStack;
 
 public class AutoSmelt extends Ability
 {
   @Expose
   public HashMap<String, String> ingotByOre = new HashMap();
   
   public AutoSmelt(String n, String d, String icon2, HashMap<String, String> ores) {
     super(n, d, icon2);
     this.ingotByOre = ores;
   }
   
   public void clicked(Player p, String permission) {
     User userData = (User)User.getUserData().get(p.getUniqueId());
     if (p.hasPermission("miningexpertise.autosmelt")) {
       if (userData.getAbilitiesOwned().contains(ChatColor.stripColor(getName()))) {
         userData.setAbilitySelected(getName());
         p.sendMessage(ChatColor.GRAY + "[" + ChatColor.GREEN + "+" + ChatColor.GRAY + "] " + ChatColor.GRAY + 
           getName() + " was selected");
         System.out.println("Ability was changed to " + getName());
       } else if (p.hasPermission(permission)) {
         userData.addAbilityOwned(getName());
         p.sendMessage(net.enchantedoasis.mining.MiningExpertise.defaultsymbol + ChatColor.GRAY + getName() + " was acquired");
       }
       
       net.enchantedoasis.mining.GUI.Ability.AbilityMenu.bakeIt(p);
     } else {
       p.sendMessage(net.enchantedoasis.mining.MiningExpertise.defaultsymbol + ChatColor.GRAY + "You don't have permission for this");
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
   
   public void checkChunkforTileEntity(Chunk chunk, String nameEntity) {
     BlockState[] arrayOfBlockState;
     int j = (arrayOfBlockState = chunk.getTileEntities()).length; for (int i = 0; i < j; i++) { BlockState te = arrayOfBlockState[i];
       if (te.getBlock().getType().toString().equalsIgnoreCase(nameEntity)) {
         te.getBlock().setType(Material.AIR);
       }
     }
   }
   
   public void bakeIt(Block mined, Player player) {
     Material blocktype = mined.getType();
     ItemStack item = player.getItemInHand();
     Material itemType = item.getType();
     String materialName = itemType.toString();
     if ((this.ingotByOre.containsKey(mined.getType().toString().toUpperCase())) && (materialName.contains("PICK"))) {
       Location loc = mined.getLocation();
       isThereDangerousBlock(loc);
       Material ingot = Material.getMaterial(this.ingotByOre.get(blocktype.toString().toUpperCase()));
       mined.setType(Material.AIR);
       mined.getWorld().dropItemNaturally(loc, new ItemStack(ingot, 1));
       player.giveExp(1);
       loc.getWorld().playEffect(loc, org.bukkit.Effect.MOBSPAWNER_FLAMES, 1);
     }
   }
 }


