package dev.devill.terraOrigins.app.abilities;

import dev.devill.terraOrigins.app.race.RaceManager;
import org.bukkit.event.Listener;
import org.bukkit.entity.Player;

import dev.devill.terraOrigins.app.abilities.annotations.*;

public abstract class Ability implements Listener {
    private String id;
    private static RaceManager raceManager;

    public Ability() {
        AbilityInfo abilityInfo = this.getClass().getAnnotation(AbilityInfo.class);
        if (abilityInfo != null) {
            this.id = abilityInfo.id();
        } else {
            this.id = "unknown";
        }
    }

    public String getId() {
        return id;
    }

    public abstract void onRaceSelect(Player player);
    public abstract void onRaceDeselect(Player player);
    public abstract void onAbilityLoad();

    public static void setRaceManager(RaceManager manager) { raceManager = manager; }

    protected boolean hasAbility(Player player) {
        if (raceManager == null) {
            throw new IllegalStateException("RaceManager not initialized! Call Ability.setRaceManager() first.");
        }
        return raceManager.hasAbility(player, this.id);
    }
} 