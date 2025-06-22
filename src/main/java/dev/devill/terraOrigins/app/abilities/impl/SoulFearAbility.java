package dev.devill.terraOrigins.app.abilities.impl;

import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.Material;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.Bukkit;

import dev.devill.terraOrigins.app.abilities.Ability;
import dev.devill.terraOrigins.app.utils.EffectData;
import dev.devill.terraOrigins.app.utils.EffectUtils;

import java.util.Map;
import java.util.Set;

import dev.devill.terraOrigins.app.abilities.annotations.*;

@AbilityInfo(id = "soul_fear")
public class SoulFearAbility extends Ability {
    @ConfigKey("effect_radius")
    private static final int EFFECT_RADIUS = 6;

    @ConfigKey("effect_update_ticks")
    private static final long EFFECT_UPDATE_TICKS = 20L;

    @ConfigKey("fear_effects")
    private static final Map<PotionEffectType, EffectData> SOUL_FEAR_EFFECTS = Map.of(
        PotionEffectType.WEAKNESS, new EffectData(60, 1),
        PotionEffectType.DARKNESS, new EffectData(60, 1)
    );

    @ConfigKey("soul_blocks")
    private static final Set<Material> SOUL_BLOCKS = Set.of(
        Material.SOUL_FIRE,
        Material.SOUL_TORCH,
        Material.SOUL_WALL_TORCH,
        Material.SOUL_LANTERN,
        Material.SOUL_CAMPFIRE
    );

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
                    if (!hasAbility(player)) continue;
                    if (isNearSoulLight(player)) {
                        EffectUtils.applyEffects(player, SOUL_FEAR_EFFECTS);
                    }
                }
            }
        }.runTaskTimer(Bukkit.getPluginManager().getPlugin("TerraOrigins"), 0L, EFFECT_UPDATE_TICKS);
    }

    private boolean isNearSoulLight(Player player) {
        for (int x = -EFFECT_RADIUS; x <= EFFECT_RADIUS; x++) {
            for (int y = -EFFECT_RADIUS; y <= EFFECT_RADIUS; y++) {
                for (int z = -EFFECT_RADIUS; z <= EFFECT_RADIUS; z++) {
                    if (x*x + y*y + z*z > EFFECT_RADIUS*EFFECT_RADIUS) continue;
                    
                    Material material = player.getLocation().clone().add(x, y, z).getBlock().getType();
                    if (SOUL_BLOCKS.contains(material)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
} 