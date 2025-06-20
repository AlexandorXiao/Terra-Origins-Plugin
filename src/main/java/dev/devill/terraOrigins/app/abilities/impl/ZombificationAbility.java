package dev.devill.terraOrigins.app.abilities.impl;

import org.bukkit.Bukkit;
import org.bukkit.World.Environment;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import dev.devill.terraOrigins.app.abilities.Ability;
import dev.devill.terraOrigins.app.utils.EffectData;
import dev.devill.terraOrigins.app.utils.EffectUtils;
import dev.devill.terraOrigins.TerraOrigins;

import java.util.Map;
import java.util.Set;
import java.util.EnumSet;

import dev.devill.terraOrigins.app.abilities.annotations.*;

@AbilityInfo(id = "zombification")
public class ZombificationAbility extends Ability {
    @ConfigKey("zombification_effects")
    private static final Map<PotionEffectType, EffectData> ZOMBIFICATION_EFFECTS = Map.of(
        PotionEffectType.SLOWNESS, new EffectData(60, 2),
        PotionEffectType.FIRE_RESISTANCE, new EffectData(60, 0)
    );

    @ConfigKey("zombification_worlds")
    private static final Set<Environment> ZOMBIFICATION_WORLDS = EnumSet.of(
        Environment.NORMAL,
        Environment.THE_END
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
                    if (hasAbility(player)) {
                        Environment playerIn = player.getWorld().getEnvironment();
                        if (ZOMBIFICATION_WORLDS.contains(playerIn)) {
                            EffectUtils.applyEffects(player, ZOMBIFICATION_EFFECTS);
                        }
                    }
                }
            }
        }.runTaskTimer(TerraOrigins.getPlugin(), 0L, 20L);
    }
} 