package dev.devill.terraOrigins;

import org.bukkit.plugin.java.JavaPlugin;

import dev.devill.terraOrigins.app.race.Race;
import dev.devill.terraOrigins.app.race.RaceManager;
import dev.devill.terraOrigins.app.abilities.*;
import dev.devill.terraOrigins.app.abilities.impl.BrotherBloodAbility;
import dev.devill.terraOrigins.app.abilities.impl.BurningRageAbility;
import dev.devill.terraOrigins.app.abilities.impl.CuriousAbility;
import dev.devill.terraOrigins.app.abilities.impl.FireImmunityAbility;
import dev.devill.terraOrigins.app.abilities.impl.FragileBodyAbility;
import dev.devill.terraOrigins.app.abilities.impl.GoldenCraftAbility;
import dev.devill.terraOrigins.app.abilities.impl.HydrophobiaAbility;
import dev.devill.terraOrigins.app.abilities.impl.MagmaBloodAbility;
import dev.devill.terraOrigins.app.abilities.impl.PawHandsAbility;
import dev.devill.terraOrigins.app.abilities.impl.SoftPawsAbility;
import dev.devill.terraOrigins.app.abilities.impl.SoulFearAbility;
import dev.devill.terraOrigins.app.abilities.impl.SturdyAbility;
import dev.devill.terraOrigins.app.abilities.impl.SugarLoverAbility;
import dev.devill.terraOrigins.app.abilities.impl.WandererAbility;
import dev.devill.terraOrigins.app.abilities.impl.ZombificationAbility;
import dev.devill.terraOrigins.app.commands.RaceCommand;

public final class TerraOrigins extends JavaPlugin {
    private static TerraOrigins plugin;
    private static RaceManager raceManager;

    @Override
    public void onEnable() {
        plugin = this;

        raceManager = new RaceManager(this);
        raceManager.initialize();
        Ability.setRaceManager(raceManager);

        Race human = new Race(
            "human",
            "Человек",
            "Стандартная раса, обладающая любознательностью и навыками выживания"
        );
        human.addAbility(new CuriousAbility());
        human.addAbility(new WandererAbility());
        raceManager.registerRace(human);
        
        // Зверолюд
        Race beastfolk = new Race(
            "beastfolk",
            "Зверолюд",
            "Раса с чертами зверей, проворная, но хрупкая"
        );
        beastfolk.addAbility(new FragileBodyAbility());
        beastfolk.addAbility(new SugarLoverAbility());
        beastfolk.addAbility(new PawHandsAbility());
        beastfolk.addAbility(new SoftPawsAbility());
        raceManager.registerRace(beastfolk);
        
        // Пиглин
        Race piglin = new Race(
            "piglin",
            "Пиглин",
            "Житель Багрового леса, искусный в золотых делах, но уязвимый в Верхнем мире"
        );
        piglin.addAbility(new GoldenCraftAbility());
        piglin.addAbility(new ZombificationAbility());
        piglin.addAbility(new BrotherBloodAbility());
        piglin.addAbility(new SoulFearAbility());
        raceManager.registerRace(piglin);
        
        // Дворф
        Race dwarf = new Race(
            "dwarf",
            "Дворф",
            "Крепкий и низкорослый гуманоид, устойчивый к ядам"
        );
        dwarf.addAbility(new SturdyAbility());
        raceManager.registerRace(dwarf);
        
        // Огнерождённый
        Race netherborn = new Race(
            "netherborn",
            "Огнерождённый",
            "Существо из Нижнего мира, невосприимчивое к огню, но страдающее от воды"
        );
        netherborn.addAbility(new MagmaBloodAbility());
        netherborn.addAbility(new BurningRageAbility());
        netherborn.addAbility(new HydrophobiaAbility());
        netherborn.addAbility(new FireImmunityAbility());
        raceManager.registerRace(netherborn);

        getCommand("race").setExecutor(new RaceCommand(raceManager));
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static TerraOrigins getPlugin() {
        return plugin;
    }

    public static RaceManager getRaceManager() {
        return raceManager;
    }


}
