 package net.enchantedoasis.mining.GUI.Ability;
 
 import net.enchantedoasis.mining.Scanner;
 import org.bukkit.command.Command;
 import org.bukkit.command.CommandExecutor;
 import org.bukkit.command.CommandSender;
 import org.bukkit.entity.Player;
 
 public class AbilityCommand implements CommandExecutor
 {
   public boolean onCommand(CommandSender sender, Command cmd, String alias, String[] args)
   {
     Player playerSender = (Player)sender;
     if ((alias.equalsIgnoreCase("mining") & args.length <= 0)) {
       AbilityMenu.bakeIt(playerSender);
       return true; }
     if ((!args[0].equals(null) & sender.isOp())) {
       new Scanner(args, playerSender.getLocation(), 6);
       return true;
     }
     playerSender.sendMessage("Not a valid Command");
     return false;
   }
 }


