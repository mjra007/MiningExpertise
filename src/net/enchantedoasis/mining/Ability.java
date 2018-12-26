 package net.enchantedoasis.mining;
 
 import java.util.HashMap;
 import net.enchantedoasis.mining.GUI.Ability.AbilityMenu;
 import net.enchantedoasis.userData.User;
 import org.bukkit.ChatColor;
 import org.bukkit.Material;
 import org.bukkit.entity.Player;
 
 public class Ability
 {
   String name;
   String description;
   Material icon = Material.ENDER_CHEST;
   static Integer counter = Integer.valueOf(0);
   Integer id;
   static HashMap<Integer, Ability> abilities = new HashMap();
   
   public Ability(String n, String d, Material icon2) {
     this.id = counter;
     this.name = n;
     this.description = d;
     this.icon = icon2;
     abilities.put(this.id, this);
     counter = Integer.valueOf(counter.intValue() + 1);
     toString();
   }
   
   public static HashMap<Integer, Ability> getAbilitiesList() {
     return abilities;
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
     return this.icon;
   }
   
   public void clicked(Player p, String permission) {
     User userData = (User)User.getUserData().get(p.getUniqueId());
     if(userData.getAbilitySelected().equals(this.getName())) {
	  			userData.setAbilitySelected("");
	  			p.sendMessage(ChatColor.GRAY + "[" + ChatColor.GREEN + "+" + ChatColor.GRAY + "] " + ChatColor.GRAY + getName() + " was unselected");
	  			return;
 			 }
     if (userData.getAbilitiesOwned().contains(ChatColor.stripColor(this.name))) {
       userData.setAbilitySelected(this.name);
       p.sendMessage(ChatColor.GRAY + "[" + ChatColor.GREEN + "+" + ChatColor.GRAY + "] " + ChatColor.GRAY + this.name + " was selected");
     } else if (p.hasPermission(permission))
     {
       userData.addAbilityOwned(this.name);
       userData.setAbilitySelected(this.name);
       p.sendMessage(MiningExpertise.defaultsymbol + ChatColor.GRAY + this.name + " was acquired");
       p.sendMessage(ChatColor.GRAY + "[" + ChatColor.GREEN + "+" + ChatColor.GRAY + "] " + ChatColor.GRAY + this.name + " was selected");

     }
     
 
 
     AbilityMenu.bakeIt(p);
   }
   
   public void bakeIt() {}
   
   public String toString()
   {
     return 
       "Ability: " + this.name + " Description: " + this.description + " ID: " + this.id + " Icon: " + this.icon;
   }
 }


