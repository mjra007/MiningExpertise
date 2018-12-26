package net.enchantedoasis.mining.ability;


import java.util.HashSet;
import java.util.LinkedList;
import java.util.Random;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityChangeBlockEvent;

import net.enchantedoasis.mining.Ability;
 

public  class LuckyDrop implements Listener{

	Random random = new Random();
	
	public void spawnLuckyDrop(Player sponsor) {
	//	Player player = getRandomPlayer();
		Location location = getRandomLocation(getRandomChunk());
        Bukkit.broadcastMessage(ChatColor.GRAY + "[" + ChatColor.GREEN + "+" + ChatColor.GRAY + "] "
        		+ ChatColor.GRAY + " was A LuckyDrop has spawn at "+location.getBlockX()+", "+location.getBlockZ()
        		+"! Sponsor: "+sponsor.getDisplayName());
		spawnFallingBlock(location);
	}

	
	private void spawnFallingBlock(Location loc) {
		World world = loc.getWorld();
		Location highestPosition = world.getHighestBlockAt(loc).getLocation();
        Byte blockData = 0x0;
		world.spawnFallingBlock(highestPosition, Material.CHEST, blockData);
	}
	
	@EventHandler
	public void onEntityChangeBlock(EntityChangeBlockEvent event)
	{
	    if (event.getEntity() instanceof FallingBlock)
	    {
	        Block blockWhereFallingLands = event.getBlock();
	        blockWhereFallingLands.setType(Material.AIR);
	        LuckyChest[] abilities = (LuckyChest[]) Ability.getAbilitiesList().values().stream()
	        .filter(s ->s instanceof LuckyChest).toArray();
	        LuckyChest luckyChest = abilities[random.nextInt(abilities.length)];
	        luckyChest.spawnChest(blockWhereFallingLands);
	    }
	}
	
	private Player getRandomPlayer() {
		LinkedList<UUID> nonRankedPlayers = new LinkedList<UUID>();
		for(Player player :Bukkit.getServer().getOnlinePlayers()){
			if(!player.hasPermission("miningexpertise")) {
				nonRankedPlayers.add(player.getUniqueId());
			}
		}
		UUID playerChosenUUID = nonRankedPlayers.get(random.nextInt(nonRankedPlayers.size()));
		return Bukkit.getPlayer(playerChosenUUID);
	}
	
	private Chunk getRandomChunk() {
		World overworld = Bukkit.getServer().getWorlds().get(0);
		Chunk chosenChunk = overworld.getLoadedChunks()[random.nextInt(overworld.getLoadedChunks().length)];
		return chosenChunk;
	}
	
	private Location getRandomLocation(Chunk chunk) {
		int x = random.nextInt(16);
		int z = random.nextInt(16);
		Location loc = new Location(chunk.getWorld(), chunk.getX() * 16 + x, 160, chunk.getZ() * 16 + z);
		return loc;
	}
}
