package dev.devill.terraOrigins.app.commands;

import dev.devill.terraOrigins.app.race.Race;
import dev.devill.terraOrigins.app.race.RaceManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.audience.Audience;

public class RaceCommand implements CommandExecutor {
    private final RaceManager raceManager;
    private final MiniMessage mm = MiniMessage.miniMessage();

    public RaceCommand(RaceManager raceManager) {
        this.raceManager = raceManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Audience audience = (sender instanceof Player p) ? p : Bukkit.getConsoleSender();
        if (args.length == 0) {
            audience.sendMessage(mm.deserialize("<yellow>/race list, /race get [ник], /race set <ник> <раса>"));
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "list":
                audience.sendMessage(mm.deserialize("<green>Доступные расы:"));
                for (Race race : raceManager.getAllRaces()) {
                    audience.sendMessage(mm.deserialize("<aqua>- " + race.getId() + "<gray>: " + race.getName()));
                }
                return true;
            case "get": {
                Player target;
                if (args.length >= 2) {
                    target = Bukkit.getPlayer(args[1]);
                    if (target == null) {
                        audience.sendMessage(mm.deserialize("<red>Игрок не найден!"));
                        return true;
                    }
                } else {
                    if (!(sender instanceof Player)) {
                        audience.sendMessage(mm.deserialize("<red>Только игрок может использовать без ника!"));
                        return true;
                    }
                    target = (Player) sender;
                }
                Race race = raceManager.getPlayerRace(target);
                if (race == null) {
                    audience.sendMessage(mm.deserialize("<yellow>" + target.getName() + " не имеет выбранной расы."));
                } else {
                    audience.sendMessage(mm.deserialize("<green>" + target.getName() + " — <aqua>" + race.getName() + " <gray>(" + race.getId() + ")"));
                }
                return true;
            }
            case "set": {
                if (args.length < 3) {
                    audience.sendMessage(mm.deserialize("<red>/race set <ник> <раса>"));
                    return true;
                }
                Player target = Bukkit.getPlayer(args[1]);
                if (target == null) {
                    audience.sendMessage(mm.deserialize("<red>Игрок не найден!"));
                    return true;
                }
                Race race = raceManager.getRace(args[2]);
                if (race == null) {
                    audience.sendMessage(mm.deserialize("<red>Раса не найдена!"));
                    return true;
                }
                raceManager.selectRace(target, race.getId());
                audience.sendMessage(mm.deserialize("<green>Раса " + race.getName() + " установлена для " + target.getName()));
                target.sendMessage(mm.deserialize("<yellow>Вам установлена раса: <aqua>" + race.getName()));
                return true;
            }
            default:
                audience.sendMessage(mm.deserialize("<yellow>/race list, /race get [ник], /race set <ник> <раса>"));
                return true;
        }
    }
} 