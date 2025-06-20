package dev.devill.terraOrigins.app.abilities.impl;

import org.bukkit.entity.Player;
import org.bukkit.entity.PiglinBrute;
import org.bukkit.entity.PiglinAbstract;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityTargetEvent;

import dev.devill.terraOrigins.app.abilities.Ability;

import java.util.Set;
import java.util.UUID;
import java.util.HashSet;

import dev.devill.terraOrigins.app.abilities.annotations.*;

@AbilityInfo(id = "brother_blood")
public class BrotherBloodAbility extends Ability {
    private static final Set<UUID> provokedPiglins = new HashSet<>();

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

        if (event.getEntity() instanceof PiglinAbstract piglin && !(piglin instanceof PiglinBrute)) {
            if (!provokedPiglins.contains(piglin.getUniqueId())) {
                event.setCancelled(true);
            }
        }
    }
} 