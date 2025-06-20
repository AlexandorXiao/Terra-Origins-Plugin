package dev.devill.terraOrigins.app.utils;

import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Map;

public class EffectUtils {
    public static void applyEffects(Player player, Map<PotionEffectType, EffectData> effects) {
        effects.forEach((effectType, data) -> {
            player.addPotionEffect(new PotionEffect(
                effectType,
                data.duration,
                data.level,
                false,
                false
            ));
        });
    }

    public static void removeEffects(Player player, Map<PotionEffectType, EffectData> effects) {
        effects.keySet().forEach(player::removePotionEffect);
    }
} 