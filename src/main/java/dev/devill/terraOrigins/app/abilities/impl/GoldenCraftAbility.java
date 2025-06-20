package dev.devill.terraOrigins.app.abilities.impl;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;

import dev.devill.terraOrigins.app.abilities.Ability;

import java.util.Set;
import java.util.EnumSet;

import dev.devill.terraOrigins.app.abilities.annotations.*;

@AbilityInfo(id = "golden_craft")
public class GoldenCraftAbility extends Ability {
    @ConfigKey("durability_multiplier")
    private static final double DURABILITY_MULTIPLIER = 2.0;

    @ConfigKey("golden_items")
    private static final Set<Material> GOLDEN_ITEMS = EnumSet.of(
        Material.GOLDEN_HOE,
        Material.GOLDEN_PICKAXE,
        Material.GOLDEN_SWORD,
        Material.GOLDEN_SHOVEL,
        Material.GOLDEN_CHESTPLATE,
        Material.GOLDEN_BOOTS,
        Material.GOLDEN_LEGGINGS,
        Material.GOLDEN_HELMET
    );

    @Override
    public void onRaceSelect(Player player) {}
    
    @Override
    public void onRaceDeselect(Player player) {}

    @Override
    public void onAbilityLoad() {}

    @EventHandler
    public void onCraft(CraftItemEvent event) {
        if (!(event.getWhoClicked() instanceof Player player) || !hasAbility(player)) return;
        
        ItemStack result = event.getRecipe().getResult();
        if (GOLDEN_ITEMS.contains(result.getType())) {
            Damageable meta = (Damageable) result.getItemMeta();
            meta.setMaxDamage((int) (result.getType().getMaxDurability() * DURABILITY_MULTIPLIER));
            result.setItemMeta(meta);
            event.setCurrentItem(result);
        }
    }
} 