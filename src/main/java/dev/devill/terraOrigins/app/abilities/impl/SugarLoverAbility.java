package dev.devill.terraOrigins.app.abilities.impl;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.Material;

import dev.devill.terraOrigins.app.abilities.Ability;
import dev.devill.terraOrigins.app.utils.EffectData;
import dev.devill.terraOrigins.app.utils.EffectUtils;

import java.util.Map;

import dev.devill.terraOrigins.app.abilities.annotations.*;

@AbilityInfo(id = "sugar_lover")
public class SugarLoverAbility extends Ability {
    @ConfigKey("sugar_effects")
    private static final Map<PotionEffectType, EffectData> SUGAR_EFFECTS = Map.of(
        PotionEffectType.HASTE, new EffectData(720, 0)
    );

    @Override
    public void onRaceSelect(Player player) {}

    @Override
    public void onRaceDeselect(Player player) {}

    @Override
    public void onAbilityLoad() {}

    @EventHandler
    public void onConsume(PlayerItemConsumeEvent event) {
        if (!hasAbility(event.getPlayer())) return;
        if (event.getItem().getType() == Material.HONEY_BOTTLE) {
            Player player = event.getPlayer();
            EffectUtils.applyEffects(player, SUGAR_EFFECTS);
        }
    }
}
