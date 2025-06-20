package dev.devill.terraOrigins.app.abilities.impl;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.entity.Player;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier.Operation;

import dev.devill.terraOrigins.app.abilities.Ability;
import dev.devill.terraOrigins.app.utils.AttributeData;
import dev.devill.terraOrigins.app.utils.AttributeUtils;
import dev.devill.terraOrigins.TerraOrigins;

import dev.devill.terraOrigins.app.abilities.annotations.*;

import java.util.Map;

@AbilityInfo(id = "burning_rage")
public class BurningRageAbility extends Ability {
    @ConfigKey("damage_boost")
    private static final double DAMAGE_BOOST_VALUE = 2.0;

    @ConfigKey("operation")
    private static final Operation DAMAGE_BOOST_OPERATION = Operation.ADD_NUMBER;

    @ConfigKey("check_interval")
    private static final long CHECK_INTERVAL = 20L;

    private static final Map<Attribute, AttributeData> DAMAGE_BOOST = Map.of(
        Attribute.ATTACK_DAMAGE, new AttributeData(DAMAGE_BOOST_VALUE, DAMAGE_BOOST_OPERATION, "burning_rage_damage")
    );

    @Override
    public void onRaceSelect(Player player) {}

    @Override
    public void onRaceDeselect(Player player) {
        AttributeUtils.removeAttributes(player, DAMAGE_BOOST);
    }

    @Override
    public void onAbilityLoad() {
        new BukkitRunnable() {
            @Override
            public void run() {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    if (hasAbility(player) && player.getFireTicks() > 0) {
                        if (!AttributeUtils.hasAttributes(player, DAMAGE_BOOST)) {
                            AttributeUtils.applyAttributes(player, DAMAGE_BOOST);
                        }
                    } else {
                        if (AttributeUtils.hasAttributes(player, DAMAGE_BOOST)) {
                            AttributeUtils.removeAttributes(player, DAMAGE_BOOST);
                        }
                    }
                }
            }
        }.runTaskTimer(TerraOrigins.getPlugin(), 0L, CHECK_INTERVAL);
    }
}