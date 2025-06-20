package dev.devill.terraOrigins.app.abilities.impl;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;

import dev.devill.terraOrigins.app.abilities.Ability;

import dev.devill.terraOrigins.app.abilities.annotations.*;

@AbilityInfo(id = "soft_paws")
public class SoftPawsAbility extends Ability {
    @ConfigKey("soft_paws.fall_damage_multiplier")
    private static final double FALL_DAMAGE_MULTIPLIER = 0.35;

    @Override
    public void onRaceSelect(Player player) {}
    
    @Override
    public void onRaceDeselect(Player player) {}

    @Override
    public void onAbilityLoad() {}

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player player) || !hasAbility(player)) return;
        if (event.getCause() == EntityDamageEvent.DamageCause.FALL) {
            double damage = event.getDamage();
            event.setDamage(damage * FALL_DAMAGE_MULTIPLIER);
        }
    }
} 