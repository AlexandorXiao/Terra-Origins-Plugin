package dev.devill.terraOrigins.app.abilities.impl;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.inventory.ItemStack;

import dev.devill.terraOrigins.app.abilities.Ability;

import java.util.Map;
import java.util.Random;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

import dev.devill.terraOrigins.app.abilities.annotations.*;

@AbilityInfo(id = "enchantment_master")
public class EnchantmentMasterAbility extends Ability {
    @ConfigKey("upgrade_chance")
    private static final double UPGRADE_CHANCE = 0.25;

    @ConfigKey("min_enchantments")
    private static final int MIN_ENCHANTMENTS = 1;

    @Override
    public void onRaceSelect(Player player) {}

    @Override
    public void onRaceDeselect(Player player) {}

    @Override
    public void onAbilityLoad() {}

    @EventHandler
    public void onEnchantItem(EnchantItemEvent event) {
        if (!hasAbility(event.getEnchanter())) return;

        ItemStack item = event.getItem();
        Map<Enchantment, Integer> enchants = item.getEnchantments();

        if (enchants.isEmpty()) return;

        Random random = new Random();

        if (random.nextDouble() <= UPGRADE_CHANCE) {
            List<Enchantment> keys = new ArrayList<>(enchants.keySet());

            int enchantmentsToUpgrade = MIN_ENCHANTMENTS + random.nextInt(keys.size());

            Collections.shuffle(keys);
            for (int i = 0; i < enchantmentsToUpgrade; i++) {
                Enchantment ench = keys.get(i);
                int currentLevel = enchants.get(ench);
                int maxLevel = ench.getMaxLevel();

                if (currentLevel < maxLevel) {
                    item.addUnsafeEnchantment(ench, currentLevel + 1);
                }
            }
        }
    }
} 