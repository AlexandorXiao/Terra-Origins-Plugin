package dev.devill.terraOrigins.app.abilities.impl;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityPotionEffectEvent;
import org.bukkit.potion.PotionEffectType;

import dev.devill.terraOrigins.app.abilities.Ability;

import java.util.Set;

import dev.devill.terraOrigins.app.abilities.annotations.*;

@AbilityInfo(id = "magma_blood")
public class MagmaBloodAbility extends Ability {
    @ConfigKey("immune_effects")
    private static final Set<PotionEffectType> IMMUNE_EFFECTS = Set.of(
        PotionEffectType.POISON,
        PotionEffectType.HUNGER
    );

    @Override
    public void onRaceSelect(Player player) {}

    @Override
    public void onRaceDeselect(Player player) {}

    @Override
    public void onAbilityLoad() {}

    @EventHandler
    public void onPotionEffect(EntityPotionEffectEvent event) {
        if (!(event.getEntity() instanceof Player player) || !hasAbility(player)) return;
        if (IMMUNE_EFFECTS.contains(event.getModifiedType())) {
            event.setCancelled(true);
        }
    }
} 