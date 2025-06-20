package dev.devill.terraOrigins.app.abilities.impl;

import dev.devill.terraOrigins.app.abilities.Ability;
import dev.devill.terraOrigins.app.race.Race;
import dev.devill.terraOrigins.app.race.RaceManager;
import dev.devill.terraOrigins.app.utils.AttributeData;
import dev.devill.terraOrigins.app.utils.AttributeUtils;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier.Operation;
import org.bukkit.entity.Player;
import dev.devill.terraOrigins.TerraOrigins;

import java.util.Map;

import dev.devill.terraOrigins.app.abilities.annotations.*;

@AbilityInfo(id = "height")
public class HeightAbility extends Ability {
    private static final String HEIGHT_MODIFIER_KEY = "height_scale";

    @ConfigKey("default_height")
    private static final double DEFAULT_HEIGHT = 1.0;

    private final Map<Attribute, AttributeData> heightModifiers = null;

    @Override
    public void onRaceSelect(Player player) {
        RaceManager raceManager = TerraOrigins.getRaceManager();
        Race race = raceManager.getPlayerRace(player);
        double raceHeight = race.getHeight();

        if (raceHeight == DEFAULT_HEIGHT) {
            return;
        }

        heightModifiers.clear();
        heightModifiers.put(
            Attribute.SCALE,
            new AttributeData(raceHeight - DEFAULT_HEIGHT, Operation.ADD_SCALAR, HEIGHT_MODIFIER_KEY)
        );

        AttributeUtils.applyAttributes(player, heightModifiers);
    }

    @Override
    public void onRaceDeselect(Player player) {
        if (AttributeUtils.hasAttribute(player, Attribute.SCALE, HEIGHT_MODIFIER_KEY)) {
            AttributeUtils.removeAttribute(player, Attribute.SCALE, HEIGHT_MODIFIER_KEY);
        }
    }

    @Override
    public void onAbilityLoad() {}
} 