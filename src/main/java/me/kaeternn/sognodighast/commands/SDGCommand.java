package me.kaeternn.sognodighast.commands;

import java.util.ArrayList;
import java.util.List;

import io.papermc.paper.command.brigadier.BasicCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;

import me.kaeternn.sognodighast.SognoDiGhast;

public class SDGCommand implements BasicCommand  {
    private SognoDiGhast plugin;

    public SDGCommand(SognoDiGhast plugin) { this.plugin = plugin; }

    @Override
    public void execute(CommandSourceStack source, String[] args) {
        if (args.length == 0) {
            source.getSender().sendMessage("Usage : /sdg [reload/version]");
            return;
        }

        switch (args[0]) {
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

        List<String> options = List.of("reload", "version");

        switch (args.length) {
            case 0:
                return options;
            case 1:
                String prefix = args[0];
                List<String> fetchedOptions = new ArrayList<>();

                for (String option : options)
                    if (option.startsWith(prefix)) fetchedOptions.add(option);

                return fetchedOptions;
            default:
                return List.of();
        }
    }
    
    @Override
    public String permission() {
        return "sognodighast.admin";
    }
}
