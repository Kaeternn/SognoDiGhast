package me.kaeternn.sognodighast.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.World.Environment;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import io.papermc.paper.command.brigadier.BasicCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;

import me.kaeternn.sognodighast.SognoDiGhast;
import me.kaeternn.sognodighast.entities.SDGWorld;
import me.kaeternn.sognodighast.handlers.SDGDialogHandler;

public class SDGCommand implements BasicCommand  {
    private SognoDiGhast plugin;

    public SDGCommand(@NotNull SognoDiGhast plugin) { this.plugin = plugin; }

    @Override
    public void execute(CommandSourceStack source, String[] args) {
        if (args.length == 0) {
            source.getSender().sendMessage("Usage : /sdg [menu [general|dimension|world]|reload|version]");
            return;
        }

        switch (args[0]) {
            case "menu":
                if (source.getSender() instanceof Player player)
                    switch (args[1]) {
                        case "general":
                            SDGDialogHandler.generalDialog(player, plugin.cachedConfig);
                            break;
                        case "dimension":
                            if (args.length < 3) {
                                player.sendMessage("You need to specify a dimension.");
                                return;
                            }

                            switch (args[2]) {
                                case "overworld":
                                    SDGDialogHandler.environmentDialog(player, plugin.toSDGEnvironment(Environment.NORMAL));
                                    break;
                                case "nether":
                                    SDGDialogHandler.environmentDialog(player, plugin.toSDGEnvironment(Environment.NETHER));
                                    break;
                                case "end":
                                    SDGDialogHandler.environmentDialog(player, plugin.toSDGEnvironment(Environment.THE_END));
                                    break;
                                default:
                                    player.sendMessage("Invalid dimension.");
                                    break;
                            }

                            break;
                        case "world":
                            if (args.length < 3) {
                                player.sendMessage("You need to specify a world name.");
                                return;
                            }

                            if (Bukkit.getWorld(args[2]) != null) {
                                SDGWorld world = plugin.toSDGWorld(Bukkit.getWorld(args[2]));

                                if (world != null)
                                    SDGDialogHandler.worldDialog(player, world);
                                else
                                    SDGDialogHandler.worldDialog(player, args[2]);
                            }
                            else
                                player.sendMessage("Invalid world name.");
                            
                            break;
                        default:
                            break;
                    }
                else
                    source.getSender().sendMessage("This command can only be executed in game.");
                break;
            case "reload":
                plugin.loadConfig();
                source.getSender().sendMessage("SognoDiGhast reloaded.");
                break;
            case "version":
                source.getSender().sendMessage("SognoDiGhast version " + plugin.getPluginMeta().getVersion());
                break;
        }
    }

    @Override
    public List<String> suggest(CommandSourceStack source, String[] args) {

        List<String> options = List.of("menu","reload", "version");
        List<String> menuOptions = List.of("general", "dimension", "world");
        List<String> dimensionOptions = List.of("overworld", "nether", "end");
        List<String> worldOptions = new ArrayList<>();

        for (org.bukkit.World world : Bukkit.getWorlds())
            worldOptions.add(world.getName());

        switch (args.length) {
            case 0:
                return options;
            case 1:
                String prefix = args[0];
                List<String> fetchedOptions = new ArrayList<>();

                for (String option : options)
                    if (option.startsWith(prefix)) fetchedOptions.add(option);

                return fetchedOptions;
            case 2:
                if (args[0].equals("menu")) {
                    String prefix2 = args[1];
                    List<String> fetchedOptions2 = new ArrayList<>();

                    for (String option : menuOptions)
                        if (option.startsWith(prefix2)) fetchedOptions2.add(option);

                    return fetchedOptions2;
                }
            case 3:
                if (args[0].equals("menu") && args[1].equals("dimension")) {
                    String prefix3 = args[2];
                    List<String> fetchedOptions3 = new ArrayList<>();

                    for (String option : dimensionOptions)
                        if (option.startsWith(prefix3)) fetchedOptions3.add(option);

                    return fetchedOptions3;
                } else if (args[0].equals("menu") && args[1].equals("world")) {
                    String prefix4 = args[2];
                    List<String> fetchedOptions4 = new ArrayList<>();

                    for (String option : worldOptions)
                        if (option.startsWith(prefix4)) fetchedOptions4.add(option);

                    return fetchedOptions4;
                }
            default:
                return List.of();
        }
    }
    
    @Override
    public String permission() {
        return "sognodighast.admin";
    }
}
