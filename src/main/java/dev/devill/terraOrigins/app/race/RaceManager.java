package dev.devill.terraOrigins.app.race;

import dev.devill.terraOrigins.app.abilities.*;
import dev.devill.terraOrigins.app.abilities.impl.BrotherBloodAbility;
import dev.devill.terraOrigins.app.abilities.impl.BurningRageAbility;
import dev.devill.terraOrigins.app.abilities.impl.CuriousAbility;
import dev.devill.terraOrigins.app.abilities.impl.EnchantmentMasterAbility;
import dev.devill.terraOrigins.app.abilities.impl.FireImmunityAbility;
import dev.devill.terraOrigins.app.abilities.impl.FragileBodyAbility;
import dev.devill.terraOrigins.app.abilities.impl.GoldenCraftAbility;
import dev.devill.terraOrigins.app.abilities.impl.HeightAbility;
import dev.devill.terraOrigins.app.abilities.impl.HydrophobiaAbility;
import dev.devill.terraOrigins.app.abilities.impl.MagmaBloodAbility;
import dev.devill.terraOrigins.app.abilities.impl.PawHandsAbility;
import dev.devill.terraOrigins.app.abilities.impl.SoftPawsAbility;
import dev.devill.terraOrigins.app.abilities.impl.SoulFearAbility;
import dev.devill.terraOrigins.app.abilities.impl.SturdyAbility;
import dev.devill.terraOrigins.app.abilities.impl.SugarLoverAbility;
import dev.devill.terraOrigins.app.abilities.impl.WandererAbility;
import dev.devill.terraOrigins.app.abilities.impl.ZombificationAbility;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;

public class RaceManager {
    private final Plugin plugin;
    private final Map<String, Race> races;
    private final Map<UUID, Race> playerRaces;
    private final Map<UUID, Race> playerAdditionalRaces;
    private final PluginManager pluginManager;
    private final Map<String, Ability> registeredAbilities;

    public RaceManager(Plugin plugin) {
        this.plugin = plugin;
        this.races = new HashMap<>();
        this.playerRaces = new ConcurrentHashMap<>();
        this.playerAdditionalRaces = new ConcurrentHashMap<>();
        this.pluginManager = Bukkit.getPluginManager();
        this.registeredAbilities = new HashMap<>();
    }

    public void initialize() {
        List<Ability> abilities = Arrays.asList(
            new BrotherBloodAbility(),
            new BurningRageAbility(),
            new CuriousAbility(),
            new EnchantmentMasterAbility(),
            new FireImmunityAbility(),
            new FragileBodyAbility(),
            new GoldenCraftAbility(),
            new HeightAbility(),
            new HydrophobiaAbility(),
            new MagmaBloodAbility(),
            new PawHandsAbility(),
            new SoftPawsAbility(),
            new SoulFearAbility(),
            new SturdyAbility(),
            new SugarLoverAbility(),
            new WandererAbility(),
            new ZombificationAbility()
        );
        
        for (Ability ability : abilities) {
            try {
                registerAbility(ability);
                plugin.getLogger().info("Загружена способность: " + ability.getClass().getSimpleName());
            } catch (Exception e) {
                plugin.getLogger().log(Level.SEVERE, 
                    "Ошибка при инициализации способности: " + ability.getClass().getName(), e);
            }
        }
    }

    private void registerAbility(Ability ability) {
        pluginManager.registerEvents(ability, plugin);
        registeredAbilities.put(ability.getId(), ability);
        ability.onAbilityLoad();
    }

    public void registerRace(Race race) {
        races.put(race.getId(), race);
    }

    public void selectRace(Player player, String raceId) {
        Race race = races.get(raceId);
        if (race == null) {
            plugin.getLogger().warning("Раса не найдена: " + raceId);
            return;
        }

        deselectRace(player);
        playerRaces.put(player.getUniqueId(), race);
        race.onSelect(player);
    }

    public void selectAdditionalRace(Player player, String raceId) {
        Race race = races.get(raceId);
        if (race == null) {
            plugin.getLogger().warning("Дополнительная раса не найдена: " + raceId);
            return;
        }

        deselectAdditionalRace(player);
        playerAdditionalRaces.put(player.getUniqueId(), race);
        race.onSelect(player);
    }

    public void deselectRace(Player player) {
        Race currentRace = playerRaces.remove(player.getUniqueId());
        if (currentRace != null) {
            currentRace.onDeselect(player);
        }
    }

    public void deselectAdditionalRace(Player player) {
        Race currentRace = playerAdditionalRaces.remove(player.getUniqueId());
        if (currentRace != null) {
            currentRace.onDeselect(player);
        }
    }

    public boolean hasAbility(Player player, String abilityId) {
        Race mainRace = playerRaces.get(player.getUniqueId());
        Race additionalRace = playerAdditionalRaces.get(player.getUniqueId());
        
        if (mainRace != null && mainRace.getAbilities().stream()
                .anyMatch(ability -> ability.getId().equals(abilityId))) {
            return true;
        }
        
        return additionalRace != null && additionalRace.getAbilities().stream()
                .anyMatch(ability -> ability.getId().equals(abilityId));
    }

    public Race getPlayerRace(Player player) {
        return playerRaces.get(player.getUniqueId());
    }

    public Race getPlayerAdditionalRace(Player player) {
        return playerAdditionalRaces.get(player.getUniqueId());
    }

    public Collection<Race> getAllRaces() {
        return races.values();
    }

    public Race getRace(String raceId) {
        return races.get(raceId);
    }
} 