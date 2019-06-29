package net.enchantedoasis.mining;

import com.google.gson.annotations.Expose;
import java.util.ArrayList;
import java.util.List;
import net.enchantedoasis.mining.ability.AutoSmelt;
import net.enchantedoasis.mining.ability.LuckyChest;
import net.enchantedoasis.mining.ability.Spawners;

public class Config {
    @Expose
    public List<LuckyChest> luckyChests 
            = new ArrayList<>();
    @Expose
    public AutoSmelt automSmelt;
    @Expose
    public Spawners spawners;
    
    public Config(List<LuckyChest> luckyChests, AutoSmelt autoSmelt,
            Spawners spawners ){
        this.luckyChests = luckyChests;
        this.automSmelt = autoSmelt;
        this.spawners = spawners;
    }
    
    public List<Ability> getAbilities(){
        List<Ability> abilities = new ArrayList<>();
        luckyChests.stream().forEach(s->abilities.add(s));
        abilities.add(automSmelt);
        abilities.add(spawners);
        return abilities;
    }
    
    public LuckyChest getLuckyChest(String name){
        return luckyChests.stream()
                .filter(s->s.getName()
                        .equals(name))
                .findFirst()
                .get();
    }
    
}
