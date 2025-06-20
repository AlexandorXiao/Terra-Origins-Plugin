package dev.devill.terraOrigins.app.abilities.impl;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerExpChangeEvent;

import dev.devill.terraOrigins.app.abilities.Ability;

import dev.devill.terraOrigins.app.abilities.annotations.*;

@AbilityInfo(id = "curious")
public class CuriousAbility extends Ability {
    @ConfigKey("experience_multiplier")
    private static final double EXPERIENCE_MULTIPLIER = 1.5;

    @Override
    public void onRaceSelect(Player player) {}

    @Override
    public void onRaceDeselect(Player player) {}
    
    @Override
    public void onAbilityLoad() {}

    @EventHandler
    public void onExpChange(PlayerExpChangeEvent event) {
        if (!hasAbility(event.getPlayer())) return;
        int originalExp = event.getAmount();
        event.setAmount((int) (originalExp * EXPERIENCE_MULTIPLIER));
    }
}
