package net.enchantedoasis.mining;

import com.google.gson.annotations.Expose;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class WeightedItemStack {

    @Expose
    String materialName;
    @Expose
    Short dataValue;
    @Expose
    Short size_of_stack;
    @Expose
    Integer chance_of_spawning;

    public WeightedItemStack(String m, Short d, Short quantity) {
        this.materialName = m;
        this.dataValue = d;
        this.size_of_stack = quantity;
        this.chance_of_spawning = 1;
    }

    public WeightedItemStack(String m, Short d, Short quantity, Integer weight) {
        this.materialName = m;
        this.dataValue = d;
        this.size_of_stack = quantity;
        this.chance_of_spawning = weight;
    }

    public Integer getWeight() {
        return chance_of_spawning;
    }

    public Material getMaterial() {
        return Material.getMaterial(materialName);
    }

    public Short getDurability() {
        return this.dataValue;
    }
    
    public void setQuantity(short quantity) {
        this.size_of_stack = quantity;
    }

    public void setWeight(Integer weight) {
        this.chance_of_spawning = weight;
    }

    public void increaseWeight(){
        this.chance_of_spawning++;
    }
    
    public void decreaseWeight(){
        this.chance_of_spawning--;
    }
    
    public Short getQuantity() {
        return this.size_of_stack;
    }
    
    public void increaseItemStack(){
        this.size_of_stack++;
    }
    public void decreaseItemStack(){
        this.size_of_stack--;
    }
    public ItemStack toItemStack() {
        return new ItemStack(getMaterial(), getQuantity(), getDurability());
    }

    @Override
    public boolean equals(Object o) {
        // If the object is compared with itself then return true  
        if (o == this) {
            return true;
        }

        if (!(o instanceof WeightedItemStack)) {
            return false;
        }

        WeightedItemStack itemList = (WeightedItemStack) o;

        return itemList.materialName.equals(materialName);
    }

    @Override
    public String toString() {
        return "§3Tickets: §7"+chance_of_spawning+"\n§3Size of stack: §7"+this.size_of_stack ;
    }
}
