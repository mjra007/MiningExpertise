package net.enchantedoasis.mining;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class WeightedItemStack {

    Material material;
    Short durability;
    Short quantity;
    Integer weight;

    public WeightedItemStack(Material m, Short d, Short quantity) {
        this.material = m;
        this.durability = d;
        this.quantity = quantity;
        this.weight = 1;
    }
    public WeightedItemStack(Material m, Short d, Short quantity, Integer weight ) {
        this.material = m;
        this.durability = d;
        this.quantity = quantity;
        this.weight = weight;
    }
    
    public Integer getWeight() {
        return weight;
    }
    
    public Material getMaterial() {
        return this.material;
    }

    public Short getDurability() {
        return this.durability;
    }
    
    public void setQuantity(short quantity){
        this.quantity=quantity;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }
    
    public Short getQuantity(){
        return this.quantity;
    }
    
    public ItemStack toItemStack(){
        return new ItemStack(getMaterial(),getQuantity(),getDurability());
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

        return itemList.material.equals(material);
    }

    public String toString() {
        return "Material: " + this.material + " Data: " + this.durability;
    }
}
