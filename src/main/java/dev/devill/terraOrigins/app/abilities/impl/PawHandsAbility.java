package dev.devill.terraOrigins.app.abilities.impl;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import dev.devill.terraOrigins.app.abilities.Ability;

import dev.devill.terraOrigins.app.abilities.annotations.*;

@AbilityInfo(id = "paw_hands")
public class PawHandsAbility extends Ability {
    @Override
    public void onRaceSelect(Player player) {}
    
    @Override
    public void onRaceDeselect(Player player) {}

    @Override
    public void onAbilityLoad() {}

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if (!hasAbility(event.getPlayer())) return;
        ItemStack item = event.getItem();
        if (item != null && item.getType() == Material.SHIELD) {
            event.setCancelled(true);
        }
    }
} 