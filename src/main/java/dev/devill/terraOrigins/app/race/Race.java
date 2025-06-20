package dev.devill.terraOrigins.app.race;

import dev.devill.terraOrigins.app.abilities.Ability;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class Race {
    private final String id;
    private final String name;
    private final String description;
    private final List<Ability> abilities;
    private double height;
    private double health;

    public Race(String id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.abilities = new ArrayList<>();
        this.height = 1.0;
        this.health = 20.0;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public List<Ability> getAbilities() {
        return abilities;
    }

    public void addAbility(Ability ability) {
        abilities.add(ability);
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }
    
    public double getHealth() {
        return health;
    }

    public void setHealth(double health) {
        this.health = health;
    }

    public void onSelect(Player player) {
        for (Ability ability : abilities) {
            ability.onRaceSelect(player);
        }
    }

    public void onDeselect(Player player) {
        for (Ability ability : abilities) {
            ability.onRaceDeselect(player);
        }
    }
} 