package dev.devill.terraOrigins.app.abilities.impl;

import org.bukkit.entity.Player;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import dev.devill.terraOrigins.app.abilities.Ability;

import java.util.Map;

import dev.devill.terraOrigins.app.abilities.annotations.*;

@AbilityInfo(id = "wanderer")
public class WandererAbility extends Ability {
    @ConfigKey("starting_items")
    private static final Map<Material, Integer> STARTING_ITEMS = Map.of(
        Material.BUNDLE, 1,
        Material.CLOCK, 1,
        Material.COMPASS, 1,
        Material.SPYGLASS, 1,
        Material.COOKED_PORKCHOP, 8
    );

    @Override
    public void onRaceSelect(Player player) {
        PlayerInventory inventory = player.getInventory();
        STARTING_ITEMS.forEach((material, amount) -> 
            inventory.addItem(new ItemStack(material, amount))
        );
    }

    @Override
    public void onRaceDeselect(Player player) {}

    @Override
    public void onAbilityLoad() {}
}
