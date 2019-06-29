 package net.enchantedoasis.mining.GUI.Ability;
 
import java.util.Collection;
import java.util.stream.Stream;
import net.enchantedoasis.mining.Ability;
import net.enchantedoasis.mining.GUI.Editor.ItemsDisplay;
import net.enchantedoasis.mining.GUI.Editor.MainEditor;
import net.enchantedoasis.mining.MiningExpertise;
import net.enchantedoasis.mining.ability.LuckyChest;
 import org.bukkit.command.Command;
 import org.bukkit.command.CommandExecutor;
 import org.bukkit.command.CommandSender;
 import org.bukkit.entity.Player;
 
 public class AbilityCommand implements CommandExecutor
 {
   @Override
   public boolean onCommand(CommandSender sender, Command cmd, String alias, String[] args)
   {
     Player playerSender = (Player)sender;
     if ((alias.equalsIgnoreCase("mining") & args.length <= 0)) {
       AbilityMenu.bakeIt(playerSender);
       return true; }
//     if ((args[0].length()>0 & sender.isOp())) {
//       new Scanner(args, playerSender.getLocation(), 6);
//       return true;
//     }
      Collection<Ability> abilities = MiningExpertise.instance.config.getAbilities();

       if (args[0] != null && playerSender.isOp()) {
           switch (args[0]) {
               case "editor":
                   playerSender.openInventory(new MainEditor().getInventory());
                   return true;
          }
      }
      
     playerSender.sendMessage("Not a valid Command");
     return false;
   }
 }


