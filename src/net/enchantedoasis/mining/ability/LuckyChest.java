package net.enchantedoasis.mining.ability;

import java.util.ArrayList;
import java.util.List;
import net.enchantedoasis.mining.Ability;
import net.enchantedoasis.mining.CommonUTIL;
import net.enchantedoasis.mining.GUI.Ability.AbilityMenu;
import net.enchantedoasis.mining.WeightedItemStack;
import net.enchantedoasis.userData.User;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class LuckyChest extends Ability {
	private ArrayList<WeightedItemStack> legendary = new ArrayList();
	private ArrayList<WeightedItemStack> common = new ArrayList();
	private ArrayList<WeightedItemStack> rare = new ArrayList();
	private List<ItemStack> items = new ArrayList();
	public static List<Location> chests = new ArrayList();
	Integer noOfItems;
	Integer chanceOf;

	public LuckyChest(String n, String d, Material icon, ArrayList<WeightedItemStack> l, ArrayList<WeightedItemStack> c,
			ArrayList<WeightedItemStack> r, Integer no, Integer chance) {
		super(n, d, icon);
		this.common = c;
		this.legendary = l;
		this.rare = r;
		System.out.println(c);
		System.out.print(r);
		System.out.println(l);
		this.noOfItems = no;
		this.chanceOf = chance;
	}

	public List<ItemStack> assignItems() {
		this.items.clear();
		getItems(this.noOfItems, this.chanceOf, 1, this.legendary);
		getItems(8, 35, 1, this.rare);
		getItems(10, 60, 5, this.common);
		return this.items;
	}

	public void getItems(Integer noLoop, Integer random, Integer maxStack, ArrayList<WeightedItemStack> list) {
            	System.out.println("Size: "+list.size() + " "+this.getName());
		for (int i = 1; i < noLoop; i++) {
			if (CommonUTIL.getRandom(0, 100) < random.intValue()) {
				WeightedItemStack item = (WeightedItemStack) list.get(CommonUTIL.getRandom(0, list.size()));
				Material randomMaterial = item.getMaterial();
				Short damage = item.getDurability();
				Bukkit.getLogger().info("item " + randomMaterial);
				Bukkit.getLogger().info("damage " + damage);
                                int quantity = CommonUTIL.getRandom(1, item.getQuantity());
                                Bukkit.getLogger().info("quantity "+quantity);
				this.items.add(new ItemStack(randomMaterial, quantity,
						damage));
			}
		}
	}

        public ArrayList<WeightedItemStack> getAllItems(){
            ArrayList<WeightedItemStack> allItems = new ArrayList<WeightedItemStack>();
            allItems.addAll(rare);
            allItems.addAll(common);
            allItems.addAll(legendary);
            return allItems;
        }
        
	public void chestBuilder(Inventory chest) {
		for (int i = 0; i < this.items.size(); i++) {
			ItemStack current = (ItemStack) this.items.get(i);
			chest.setItem(CommonUTIL.random.nextInt(27), current);
		}
	}

	public void bakeIt(Block block, Integer c, Player player, BlockBreakEvent e) {
		ItemStack item = player.getItemInHand();
		Material itemType = item.getType();
		String materialName = itemType.toString();
		
		//Checks if the player is in a cooldown
		if(!Cooldown.isInCooldown(player.getUniqueId(), "luckychest")){
		//If isn't, start it
			Cooldown cooldown = new Cooldown(player.getUniqueId(), "luckychest", 240);
			cooldown.start();
		}else {
			return;
		}
		
		if ((player.hasPermission("mining.chest")) && (block.getType() == Material.STONE)
				&& (CommonUTIL.getRandom(0, 200) <= c) && (materialName.contains("PICK"))
				&& (!materialName.equalsIgnoreCase("THAUMICTINKERER_ICHORPICKGEM"))) {
			e.setCancelled(true);
			spawnChest(block);
			if(CommonUTIL.getRandom(0, 100)<25){
				new LuckyDrop().spawnLuckyDrop(player);
			}
		}
	}

	public void spawnChest(Block block) {
		assignItems();
		block.setType(Material.CHEST);
		Chest chest = (Chest) block.getState();
		Inventory inv = chest.getBlockInventory();
		chestBuilder(inv);
		block.getLocation().getWorld().playEffect(block.getLocation(), org.bukkit.Effect.WITCH_MAGIC, 10);
		chests.add(chest.getLocation());
		
	}

	public void clicked(Player p, String permission) {
		User userData = (User) User.getUserData().get(p.getUniqueId());
		if (userData.getAbilitySelected().equals(this.getName())) {
			userData.setAbilitySelected("");
			p.sendMessage(ChatColor.GRAY + "[" + ChatColor.GREEN + "+" + ChatColor.GRAY + "] " + ChatColor.GRAY
					+ getName() + " was unselected");
			return;
		}

		if (userData.getAbilitiesOwned().contains(ChatColor.stripColor(getName()))) {
			userData.setAbilitySelected(getName());
			p.sendMessage(ChatColor.GRAY + "[" + ChatColor.GREEN + "+" + ChatColor.GRAY + "] " + ChatColor.GRAY
					+ getName() + " was selected");
		} else if ((p.hasPermission(permission) & userData.addAbilityOwned(getName()).booleanValue())) {
			userData.setAbilitySelected(getName());
			p.sendMessage(ChatColor.GRAY + "[" + ChatColor.GREEN + "+" + ChatColor.GRAY + "] " + ChatColor.GRAY
					+ getName() + " was selected");
			p.sendMessage(net.enchantedoasis.mining.MiningExpertise.defaultsymbol + ChatColor.GRAY + getName()
					+ " was acquired");
		}
		AbilityMenu.bakeIt(p);
	}
}
