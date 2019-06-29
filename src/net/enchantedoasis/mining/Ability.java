 package net.enchantedoasis.mining;
 
import com.google.gson.annotations.Expose;
 import java.util.HashMap;
 import net.enchantedoasis.mining.GUI.Ability.AbilityMenu;
 import net.enchantedoasis.userData.User;
 import org.bukkit.ChatColor;
 import org.bukkit.Material;
 import org.bukkit.entity.Player;
 
 public class Ability
 {   
   @Expose
   String name;
   @Expose
   String description;
   @Expose
   String icon_In_GUI = Material.ENDER_CHEST.toString();

   public Ability(String n, String d, String icon2) {
     this.name = n;
     this.description = d;
     this.icon_In_GUI = icon2;
   }

   public void setName(String newName) {
     this.name = newName;
   }
   
   public void setDescription(String newDescription) {
     this.description = newDescription;
   }
   
   public String getName() {
     return this.name;
   }
   
   public String getDescription() {
     return this.description;
   }
   
   public Material getIcon() {
     return Material.getMaterial(this.icon_In_GUI);
   }
   
   public void clicked(Player player, String permission) {
     User userData = (User)User.getUserData().get(player.getUniqueId());
     if(userData.getAbilitySelected().equals(this.getName())) {
	  			userData.setAbilitySelected("");
	  			player.sendMessage(ChatColor.GRAY + "[" + ChatColor.GREEN + "+" + ChatColor.GRAY + "] " + ChatColor.GRAY + getName() + " was unselected");
	  			return;
 			 }
     if (userData.getAbilitiesOwned().contains(ChatColor.stripColor(this.name))) {
       userData.setAbilitySelected(this.name);
       player.sendMessage(ChatColor.GRAY + "[" + ChatColor.GREEN + "+" + ChatColor.GRAY + "] " + ChatColor.GRAY + this.name + " was selected");
     } else if (player.hasPermission(permission))
     {
       userData.addAbilityOwned(this.name);
       userData.setAbilitySelected(this.name);
       player.sendMessage(MiningExpertise.defaultsymbol + ChatColor.GRAY + this.name + " was acquired");
       player.sendMessage(ChatColor.GRAY + "[" + ChatColor.GREEN + "+" + ChatColor.GRAY + "] " + ChatColor.GRAY + this.name + " was selected");

     }
     
 
 
     AbilityMenu.bakeIt(player);
   }
   
   public void bakeIt() {}
   
   @Override
   public String toString()
   {
     return 
       "Ability: " + this.name + " Description: " + this.description + " Icon: " + this.icon_In_GUI;
   }
 }


