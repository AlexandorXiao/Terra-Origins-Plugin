package dev.devill.terraOrigins.app.abilities.impl;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import dev.devill.terraOrigins.app.abilities.Ability;

import java.util.EnumSet;
import java.util.Set;

import dev.devill.terraOrigins.app.abilities.annotations.*;

@AbilityInfo(id = "fire_immunity")
public class FireImmunityAbility extends Ability {
    @ConfigKey("damage_causes")
    private static final Set<DamageCause> DAMAGE_CAUSES = EnumSet.of(
        DamageCause.FIRE,
        DamageCause.FIRE_TICK,
        DamageCause.LAVA
    );

    @Override
    public void onRaceSelect(Player player) {}

    @Override
    public void onRaceDeselect(Player player) {}

    @Override
    public void onAbilityLoad() {}

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player player) || !hasAbility(player)) return;
        
        if (DAMAGE_CAUSES.contains(event.getCause())) {
            event.setCancelled(true);
        }
    }
} 