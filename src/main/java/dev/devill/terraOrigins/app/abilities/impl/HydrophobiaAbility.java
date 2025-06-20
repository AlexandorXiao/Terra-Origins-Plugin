package dev.devill.terraOrigins.app.abilities.impl;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.entity.PotionSplashEvent;
import org.bukkit.event.entity.AreaEffectCloudApplyEvent;
import org.bukkit.Material;
import org.bukkit.damage.DamageType;
import org.bukkit.damage.DamageSource;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.Bukkit;
import dev.devill.terraOrigins.TerraOrigins;
import dev.devill.terraOrigins.app.abilities.Ability;

import dev.devill.terraOrigins.app.abilities.annotations.*;

@AbilityInfo(id = "hydrophobia")
public class HydrophobiaAbility extends Ability {
    @ConfigKey("water_damage")
    private static final double WATER_DAMAGE = 2.0;

    @ConfigKey("water_damage_check_interval")
    private static final long WATER_DAMAGE_CHECK_INTERVAL = 20L;

    @ConfigKey("damage_type")
    private static final DamageType DAMAGE_TYPE = DamageType.DRY_OUT;

    private static final DamageSource DAMAGE_SOURCE = DamageSource.builder(DAMAGE_TYPE).build();

    @Override
    public void onRaceSelect(Player player) {}

    @Override
    public void onRaceDeselect(Player player) {}

    @Override
    public void onAbilityLoad() {
        new BukkitRunnable() {
            @Override
            public void run() {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    if (hasAbility(player) && player.isInWaterOrRainOrBubbleColumn()) {
                        player.damage(WATER_DAMAGE, DAMAGE_SOURCE);
                    }
                }
            }
        }.runTaskTimer(TerraOrigins.getPlugin(), 0L, WATER_DAMAGE_CHECK_INTERVAL);
    }

    @EventHandler
    public void onPotionSplash(PotionSplashEvent event) {
        event.getAffectedEntities().stream()
            .filter(entity -> entity instanceof Player player && hasAbility(player))
            .forEach(entity -> entity.damage(WATER_DAMAGE, DAMAGE_SOURCE));
    }

    @EventHandler
    public void onAreaEffectCloud(AreaEffectCloudApplyEvent event) {
        event.getAffectedEntities().stream()
            .filter(entity -> entity instanceof Player player && hasAbility(player))
            .forEach(entity -> entity.damage(WATER_DAMAGE, DAMAGE_SOURCE));
    }

    @EventHandler
    public void onItemConsume(PlayerItemConsumeEvent event) {
        if (!hasAbility(event.getPlayer())) return;
        if (event.getItem().getType() == Material.POTION) {
            event.getPlayer().damage(WATER_DAMAGE);
        }
    }
} 