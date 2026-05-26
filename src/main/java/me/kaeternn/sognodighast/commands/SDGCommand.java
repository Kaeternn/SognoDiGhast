package me.kaeternn.sognodighast.commands;

import java.util.List;

import io.papermc.paper.command.brigadier.BasicCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;

import me.kaeternn.sognodighast.SognoDiGhast;

public class SDGCommand implements BasicCommand  {
    private SognoDiGhast plugin;

    public SDGCommand(SognoDiGhast plugin) { this.plugin = plugin; }

    @Override
    public void execute(CommandSourceStack source, String[] args) {
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
        switch (args.length) {
            case 0:
                return List.of("reload", "version");
            default:
                return List.of();
        }
    }
}
