 package net.enchantedoasis.mining.GUI.Ability;
 
import java.util.Collection;
import java.util.stream.Stream;
import net.enchantedoasis.mining.Ability;
import net.enchantedoasis.mining.GUI.Editor.ItemsDisplay;
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
      Collection<Ability> abilities = Ability.getAbilitiesList().values();
       Stream<LuckyChest> luckyChests = abilities.stream().filter(s -> s instanceof LuckyChest).map(s -> (LuckyChest) s);

       if (args[0] != null) {
           switch (args[0]) {
               case "BloodMagic":
                   luckyChests.filter(s -> s.getName().equalsIgnoreCase("BloodMagic"))
                           .findFirst()
                           .ifPresent(s -> new ItemsDisplay(s.getName(),
                                   s.getAllItems()).openInventory(playerSender));
                   break;
               case "Thaumcraft":
                   luckyChests.filter(s -> s.getName().equalsIgnoreCase("Thaumcraft"))
                           .findFirst()
                           .ifPresent(s -> new ItemsDisplay(s.getName(),
                                   s.getAllItems()).openInventory(playerSender));
                   break;
               case "Botania":
                   luckyChests.filter(s -> s.getName().equalsIgnoreCase("Botania"))
                           .findFirst()
                           .ifPresent(s -> new ItemsDisplay(s.getName(),
                                   s.getAllItems()).openInventory(playerSender));
                   break;
               case "Witchery":
                   luckyChests.filter(s -> s.getName().equalsIgnoreCase("Witchery"))
                           .findFirst()
                           .ifPresent(s -> new ItemsDisplay(s.getName(),
                                   s.getAllItems()).openInventory(playerSender));
                   break;
               case "Forestry":
                   luckyChests.filter(s -> s.getName().equalsIgnoreCase("Forestry"))
                           .findFirst()
                           .ifPresent(s -> new ItemsDisplay(s.getName(),
                                   s.getAllItems()).openInventory(playerSender));
                   break;
               case "ArsMagica2":
                   luckyChests.filter(s -> s.getName().equalsIgnoreCase("ArsMagica2"))
                           .findFirst()
                           .ifPresent(s -> new ItemsDisplay(s.getName(),
                                   s.getAllItems()).openInventory(playerSender));
                   break;
          }
      }
      
     playerSender.sendMessage("Not a valid Command");
     return false;
   }
 }


