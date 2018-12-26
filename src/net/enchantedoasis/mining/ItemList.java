package net.enchantedoasis.mining;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class ItemList {

    Material material;
    Short durability;
    Short quantity;

    public ItemList(Material m, Short d, Short quantity) {
        this.material = m;
        this.durability = d;
        this.quantity = quantity;
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

        if (!(o instanceof ItemList)) {
            return false;
        }

        ItemList itemList = (ItemList) o;

        return itemList.material.equals(material);
    }

    public String toString() {
        return "Material: " + this.material + " Data: " + this.durability;
    }
}
