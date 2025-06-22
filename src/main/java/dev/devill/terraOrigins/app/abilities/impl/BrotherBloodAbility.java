package dev.devill.terraOrigins.app.abilities.impl;

import org.bukkit.entity.Player;
import org.bukkit.entity.PiglinBrute;
import org.bukkit.entity.PiglinAbstract;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.entity.EntityType;

import dev.devill.terraOrigins.app.abilities.Ability;

import java.util.Set;
import java.util.UUID;
import java.util.HashSet;

import dev.devill.terraOrigins.app.abilities.annotations.*;

@AbilityInfo(id = "brother_blood")
public class BrotherBloodAbility extends Ability {
    private static final Set<UUID> provokedPiglins = new HashSet<>();

    @ConfigKey("brother_mobs")
    private static final Set<EntityType> peacefulMobs = Set.of(
        EntityType.PIGLIN_BRUTE
    );

    @Override
    public void onRaceSelect(Player player) {}

    @Override
    public void onRaceDeselect(Player player) {}

    @Override
    public void onAbilityLoad() {}

    @EventHandler
    public void onPlayerAttack(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player player) || !hasAbility(player)) {
            return;
        }

        if (event.getEntity() instanceof PiglinAbstract piglin && !(piglin instanceof PiglinBrute)) {
            provokedPiglins.add(piglin.getUniqueId());
        }
    }

    @EventHandler
    public void onEntityTarget(EntityTargetEvent event) {
        if (!(event.getTarget() instanceof Player player) || !hasAbility(player)) {
            return;
        }

        EntityType type = event.getEntity().getType();
        if (peacefulMobs.contains(type)) {
            if (event.getEntity() instanceof PiglinAbstract piglin && !(piglin instanceof PiglinBrute)) {
                if (!provokedPiglins.contains(piglin.getUniqueId())) {
                    event.setCancelled(true);
                }
            } else if (!provokedPiglins.contains(event.getEntity().getUniqueId())) {
                event.setCancelled(true);
            }
        }
    }
} 