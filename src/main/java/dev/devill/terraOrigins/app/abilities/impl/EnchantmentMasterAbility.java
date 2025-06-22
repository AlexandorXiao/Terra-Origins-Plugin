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
    public void onEnchantItem(EnchantItemEvent event) {event.getEnchanter();
        if (!hasAbility(event.getEnchanter())) return;

        ItemStack item = event.getItem();
        Map<Enchantment, Integer> enchants = item.getEnchantments();

        if (enchants.isEmpty()) {
            return;
        }

        Random random = new Random();

        if (random.nextDouble() > UPGRADE_CHANCE) {
            return;
        }

        List<Enchantment> enchantmentList = new ArrayList<>(enchants.keySet());
        Collections.shuffle(enchantmentList);

        int enchantmentsToUpgrade = Math.min(
            MIN_ENCHANTMENTS + random.nextInt(enchantmentList.size() + 1),
            enchantmentList.size()
        );

        for (int i = 0; i < enchantmentsToUpgrade; i++) {
            Enchantment enchantment = enchantmentList.get(i);
            int currentLevel = enchants.get(enchantment);

            if (currentLevel < enchantment.getMaxLevel()) {
                item.addUnsafeEnchantment(enchantment, currentLevel + 1);
            }
        }
    }
} 