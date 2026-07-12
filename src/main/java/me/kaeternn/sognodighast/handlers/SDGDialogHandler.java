package me.kaeternn.sognodighast.handlers;

import java.util.List;

import org.bukkit.entity.Player;

import org.jetbrains.annotations.NotNull;

import io.papermc.paper.dialog.Dialog;
import io.papermc.paper.registry.data.dialog.ActionButton;
import io.papermc.paper.registry.data.dialog.DialogBase;
import io.papermc.paper.registry.data.dialog.action.DialogAction;
import io.papermc.paper.registry.data.dialog.input.DialogInput;
import io.papermc.paper.registry.data.dialog.input.SingleOptionDialogInput.OptionEntry;
import io.papermc.paper.registry.data.dialog.type.DialogType;

import me.kaeternn.sognodighast.SognoDiGhast;
import me.kaeternn.sognodighast.entities.SDGConfig;
import me.kaeternn.sognodighast.entities.SDGEnvironment;
import me.kaeternn.sognodighast.entities.SDGWorld;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.nbt.api.BinaryTagHolder;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

public class SDGDialogHandler {
    public static SognoDiGhast plugin;
    
    public static void generalDialog(@NotNull Player player, @NotNull SDGConfig config) {
        DialogBase base = DialogBase.builder(Component.text("Sogno Di Ghast - v" + plugin.getPluginMeta().getVersion()))
            .canCloseWithEscape(false)
            .externalTitle(Component.text("Sogno Di Ghast configuration"))
            .inputs(List.of(
                DialogInput.bool("debug", Component.text("Debug mode"))
                    .initial(config.isDebugEnabled())
                    .build(),
                DialogInput.bool("only_happy_ghast", Component.text("Happy ghasts only"))
                    .initial(config.isOnlyHappyGhast())
                    .build(),
                DialogInput.bool("only_when_ridden", Component.text("Ridden happy ghasts only"))
                    .initial(config.isOnlyWhenRidden())
                    .build(),
                DialogInput.text("speed_multiplier", Component.text("Flying speed multiplier"))
                    .initial(String.valueOf(config.getSpeedMultiplier()))
                    .build()
            )).build();

        Dialog dialog = Dialog.create(builder -> builder.empty()
            .base(base)
            .type(DialogType.confirmation(
                ActionButton.create(
                    Component.text("Save & reload", NamedTextColor.GREEN),
                    Component.text("Save this configuration and reload the plugin to apply it."),
                    128,
                    DialogAction.customClick(Key.key("general_dialog_confirmation"), BinaryTagHolder.binaryTagHolder("{player:\""+ player.getName() + "\"}"))
                ),
                ActionButton.create(
                    Component.text("Cancel", NamedTextColor.RED),
                    Component.text("Cancel this configuration, all changes will be lost."),
                    128,
                    null
                )
            ))
        );

        player.showDialog(dialog);
    }

    public static void environmentDialog(@NotNull Player player, @NotNull SDGEnvironment environment) {
        boolean overDefault = false;
        boolean nethDefault = false;
        boolean endDefault = false;

        switch (environment.getEnvironment()) {
            case NORMAL:
                overDefault = true;
                break;
            case NETHER:
                nethDefault = true;
                break;
            case THE_END:
                endDefault = true;
                break;
            default:
                return;
        }

        DialogBase base = DialogBase.builder(Component.text("Sogno Di Ghast - v" + plugin.getPluginMeta().getVersion()))
            .canCloseWithEscape(false)
            .externalTitle(Component.text("Sogno Di Ghast configuration"))
            .inputs(List.of(
                DialogInput.singleOption(
                    "environment",
                    Component.text("Dimension"),
                    List.of(
                        OptionEntry.create("overworld", Component.text("overworld"), overDefault),
                        OptionEntry.create("nether", Component.text("nether"), nethDefault),
                        OptionEntry.create("end", Component.text("end"), endDefault)
                    )
                ).build(),
                DialogInput.text("min", Component.text("Minimum height (to disable this limit put \"infinity\")"))
                    .initial(environment.getMin() != null ? String.valueOf(environment.getMin()) : "infinity")
                    .build(),
                DialogInput.text("max", Component.text("Maximum height (to disable this limit put \"infinity\")"))
                    .initial(environment.getMax() != null ? String.valueOf(environment.getMax()) : "infinity")
                    .build()
            )).build();

        Dialog dialog = Dialog.create(builder -> builder.empty()
            .base(base)
            .type(DialogType.confirmation(
                ActionButton.create(
                    Component.text("Save & reload", NamedTextColor.GREEN),
                    Component.text("Save this configuration and reload the plugin to apply it."),
                    128,
                    DialogAction.customClick(Key.key("environment_dialog_confirmation"), BinaryTagHolder.binaryTagHolder("{player:\""+ player.getName() + "\"}"))
                ),
                ActionButton.create(
                    Component.text("Cancel", NamedTextColor.RED),
                    Component.text("Cancel this configuration, all changes will be lost."),
                    128,
                    null
                )
            ))
        );

        player.showDialog(dialog);
    }

    public static void worldDialog(@NotNull Player player, @NotNull SDGWorld world) {
        DialogBase base = DialogBase.builder(Component.text("Sogno Di Ghast - v" + plugin.getPluginMeta().getVersion()))
            .canCloseWithEscape(false)
            .externalTitle(Component.text("Sogno Di Ghast configuration"))
            .inputs(List.of(
                DialogInput.text("world", Component.text("World name (this won't change the world's name)"))
                    .initial(String.valueOf(world.getWorld().getName()))
                    .build(),
                DialogInput.text("min", Component.text("Minimum height (to disable this limit put \"infinity\")"))
                    .initial(world.getMin() != null ? String.valueOf(world.getMin()) : "infinity")
                    .build(),
                DialogInput.text("max", Component.text("Maximum height (to disable this limit put \"infinity\")"))
                    .initial(world.getMax() != null ? String.valueOf(world.getMax()) : "infinity")
                    .build()
            )).build();

        Dialog dialog = Dialog.create(builder -> builder.empty()
            .base(base)
            .type(DialogType.confirmation(
                ActionButton.create(
                    Component.text("Save & reload", NamedTextColor.GREEN),
                    Component.text("Save this configuration and reload the plugin to apply it."),
                    128,
                    DialogAction.customClick(Key.key("world_dialog_confirmation"), BinaryTagHolder.binaryTagHolder("{player:\""+ player.getName() + "\"}"))
                ),
                ActionButton.create(
                    Component.text("Cancel", NamedTextColor.RED),
                    Component.text("Cancel this configuration, all changes will be lost."),
                    128,
                    null
                )
            ))
        );

        player.showDialog(dialog);
    }

    public static void worldDialog(@NotNull Player player, @NotNull String world) {
        DialogBase base = DialogBase.builder(Component.text("Sogno Di Ghast - v" + plugin.getPluginMeta().getVersion()))
            .canCloseWithEscape(false)
            .externalTitle(Component.text("Sogno Di Ghast configuration"))
            .inputs(List.of(
                DialogInput.text("world", Component.text("World name (this won't change the world's name)"))
                    .initial(world)
                    .build(),
                DialogInput.text("min", Component.text("Minimum height (to disable this limit put \"infinity\")"))
                    .initial("0")
                    .build(),
                DialogInput.text("max", Component.text("Maximum height (to disable this limit put \"infinity\")"))
                    .initial("infinity")
                    .build()
            )).build();

        Dialog dialog = Dialog.create(builder -> builder.empty()
            .base(base)
            .type(DialogType.confirmation(
                ActionButton.create(
                    Component.text("Save & reload", NamedTextColor.GREEN),
                    Component.text("Save this configuration and reload the plugin to apply it."),
                    128,
                    DialogAction.customClick(Key.key("world_dialog_confirmation"), BinaryTagHolder.binaryTagHolder("{player:\""+ player.getName() + "\"}"))
                ),
                ActionButton.create(
                    Component.text("Cancel", NamedTextColor.RED),
                    Component.text("Cancel this configuration, all changes will be lost."),
                    128,
                    null
                )
            ))
        );

        player.showDialog(dialog);
    }
}
