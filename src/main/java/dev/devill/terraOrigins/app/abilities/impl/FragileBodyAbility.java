package dev.devill.terraOrigins.app.abilities.impl;

import org.bukkit.entity.Player;

import dev.devill.terraOrigins.app.abilities.Ability;
import dev.devill.terraOrigins.app.race.Race;
import dev.devill.terraOrigins.app.race.RaceManager;
import dev.devill.terraOrigins.TerraOrigins;

import dev.devill.terraOrigins.app.abilities.annotations.*;

@AbilityInfo(id = "fragile_body")
public class FragileBodyAbility extends Ability {
    @ConfigKey("default_health")
    private static final double DEFAULT_HEALTH = 20.0;

    @Override
    public void onRaceSelect(Player player) {
        RaceManager raceManager = TerraOrigins.getRaceManager();
        Race race = raceManager.getPlayerRace(player);
        double raceHealth = race.getHealth();
    
        if (raceHealth == DEFAULT_HEALTH) {
            return;
        }
            
        player.setHealthScale(raceHealth);
        player.setHealth(raceHealth);
    }
    
    @Override
    public void onRaceDeselect(Player player) {
        player.setHealthScale(DEFAULT_HEALTH);
        player.setHealth(DEFAULT_HEALTH);
    }

    @Override
    public void onAbilityLoad() {}
}
