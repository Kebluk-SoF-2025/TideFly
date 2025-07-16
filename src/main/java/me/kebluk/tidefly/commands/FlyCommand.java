package me.kebluk.tidefly.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.command.brigadier.argument.resolvers.selector.PlayerSelectorArgumentResolver;
import me.kebluk.tidefly.TideFly;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("UnstableApiUsage")
public class FlyCommand {
    private static final TideFly plugin = TideFly.getInst();

    public static LiteralCommandNode<CommandSourceStack> create() {
        return Commands.literal("tidefly")
                .requires(source -> source.getSender().hasPermission("tidefly.cmd.fly")) //TODO
                .then(Commands.literal("on")
                        .requires(source -> source.getSender().hasPermission("tidefly.cmd.fly.on"))
                        .executes(ctx -> {
                            // Sender   = original command sender, or executor of /execute
                            // Executor = player who executed the command or who was targeted by /execute
                            if (!(ctx.getSource().getExecutor() instanceof final Player p)) {
                                ctx.getSource().getSender().sendRichMessage(plugin.getLocale().playerOnly());
                                return Command.SINGLE_SUCCESS;
                            }

                            return flyOn(ctx);
                        })
                        .then(Commands.argument("player", ArgumentTypes.player())
                                .requires(source -> source.getSender().hasPermission("tidefly.cmd.fly.on.others"))
                                .executes(ctx -> {
                                    final PlayerSelectorArgumentResolver playerResolver = ctx.getArgument("player", PlayerSelectorArgumentResolver.class);
                                    final Player player = playerResolver.resolve(ctx.getSource()).getFirst();

                                    // Executes the logic just as the player argument would be the executor
                                    return flyOn(ctx.copyFor(copySourceForExecutor(ctx.getSource(), player)));
                                })
                        )
                )
                .then(Commands.literal("off")
                        .requires(source -> source.getSender().hasPermission("tidefly.cmd.fly.off"))
                        .executes(ctx -> {
                            if (!(ctx.getSource().getExecutor() instanceof final Player p)) {
                                ctx.getSource().getSender().sendRichMessage(plugin.getLocale().playerOnly());
                                return Command.SINGLE_SUCCESS;
                            }

                            return flyOff(ctx);
                        })
                        .then(Commands.argument("player", ArgumentTypes.player())
                                .requires(source -> source.getSender().hasPermission("tidefly.cmd.fly.off.others"))
                                .executes(ctx -> {
                                    final PlayerSelectorArgumentResolver playerResolver = ctx.getArgument("player", PlayerSelectorArgumentResolver.class);
                                    final Player player = playerResolver.resolve(ctx.getSource()).getFirst();

                                    // Executes the logic just as the player argument would be the executor
                                    return flyOff(ctx.copyFor(copySourceForExecutor(ctx.getSource(), player)));
                                })
                        )
                )
                .then(Commands.literal("toggle")
                        .requires(source -> source.getSender().hasPermission("tidefly.cmd.fly.toggle"))
                        .executes(ctx -> {
                            if (!(ctx.getSource().getExecutor() instanceof final Player p)) {
                                ctx.getSource().getSender().sendRichMessage(plugin.getLocale().playerOnly());
                                return Command.SINGLE_SUCCESS;
                            }

                            return flyToggle(ctx);
                        })
                        .then(Commands.argument("player", ArgumentTypes.player())
                                .requires(source -> source.getSender().hasPermission("tidefly.cmd.fly.toggle.others"))
                                .executes(ctx -> {
                                    final PlayerSelectorArgumentResolver playerResolver = ctx.getArgument("player", PlayerSelectorArgumentResolver.class);
                                    final Player player = playerResolver.resolve(ctx.getSource()).getFirst();

                                    // Executes the logic just as the player argument would be the executor
                                    return flyToggle(ctx.copyFor(copySourceForExecutor(ctx.getSource(), player)));
                                })
                        )
                )
                .then(Commands.literal("help")
                        .requires(source -> source.getSender().hasPermission("tidefly.cmd.fly.help"))
                        .executes(ctx -> {
                            return Command.SINGLE_SUCCESS;
                        })
                        .then(Commands.argument("player", ArgumentTypes.player())
                                .requires(source -> source.getSender().hasPermission("tidefly.cmd.fly.help.others"))
                                .executes(ctx -> {
                                    return Command.SINGLE_SUCCESS;
                                })
                        )
                )
                .then(Commands.argument("speed", StringArgumentType.word())
                        .requires(source -> source.getSender().hasPermission("tidefly.cmd.fly.speed"))
                        .executes(ctx -> {
                            return Command.SINGLE_SUCCESS;
                        })
                        .then(Commands.argument("player", ArgumentTypes.player())
                                .requires(source -> source.getSender().hasPermission("tidefly.cmd.fly.speed.others"))
                                .executes(ctx -> {
                                    return Command.SINGLE_SUCCESS;
                                })
                        )
                )
                .then(Commands.argument("pay", StringArgumentType.greedyString())
                        .requires(source -> source.getSender().hasPermission("tidefly.cmd.fly.pay"))
                        .executes(ctx -> {
                            return Command.SINGLE_SUCCESS;
                        })
                        .then(Commands.argument("player", ArgumentTypes.player())
                                .requires(source -> source.getSender().hasPermission("tidefly.cmd.fly.pay.others"))
                                .executes(ctx -> {
                                    return Command.SINGLE_SUCCESS;
                                })
                        )
                )
                .then(Commands.argument("add", StringArgumentType.greedyString())
                        .requires(source -> source.getSender().hasPermission("tidefly.admin.cmd.fly.add"))
                        .executes(ctx -> {
                            return Command.SINGLE_SUCCESS;
                        })
                        .then(Commands.argument("player", ArgumentTypes.player())
                                .requires(source -> source.getSender().hasPermission("tidefly.admin.cmd.fly.add.others"))
                                .executes(ctx -> {
                                    return Command.SINGLE_SUCCESS;
                                })
                        )
                )
                .then(Commands.argument("remove", StringArgumentType.greedyString())
                        .requires(source -> source.getSender().hasPermission("tidefly.admin.cmd.fly.remove"))
                        .executes(ctx -> {
                            return Command.SINGLE_SUCCESS;
                        })
                        .then(Commands.argument("player", ArgumentTypes.player())
                                .requires(source -> source.getSender().hasPermission("tidefly.admin.cmd.fly.remove.others"))
                                .executes(ctx -> {
                                    return Command.SINGLE_SUCCESS;
                                })
                        )
                )
                .then(Commands.argument("set", StringArgumentType.greedyString())
                        .requires(source -> source.getSender().hasPermission("tidefly.admin.cmd.fly.set"))
                        .executes(ctx -> {
                            return Command.SINGLE_SUCCESS;
                        })
                        .then(Commands.argument("player", ArgumentTypes.player())
                                .requires(source -> source.getSender().hasPermission("tidefly.admin.cmd.fly.set.others"))
                                .executes(ctx -> {
                                    return Command.SINGLE_SUCCESS;
                                })
                        )
                )
                .then(Commands.argument("reset", StringArgumentType.greedyString())
                        .requires(source -> source.getSender().hasPermission("tidefly.admin.cmd.fly.reset"))
                        .executes(ctx -> {
                            return Command.SINGLE_SUCCESS;
                        })
                        .then(Commands.argument("player", ArgumentTypes.player())
                                .requires(source -> source.getSender().hasPermission("tidefly.admin.cmd.fly.reset.others"))
                                .executes(ctx -> {
                                    return Command.SINGLE_SUCCESS;
                                })
                        )
                )
                .build();
    }

    private static byte flyOn(final CommandContext<CommandSourceStack> ctx) {
        final CommandSender sender = ctx.getSource().getSender();
        final Entity executor = ctx.getSource().getExecutor();

        if (!(executor instanceof final Player player)) {
            // If a non-player tried to set their own flight speed
            sender.sendPlainMessage("Only players can fly!");
            return Command.SINGLE_SUCCESS;
        }

        if (player.getAllowFlight()) {
            player.sendRichMessage(plugin.getLocale(player).flyAlreadyEnabled());
        } else {
            player.setAllowFlight(true);
            player.sendRichMessage(plugin.getLocale(player).flyEnabled());
        }

        if (player != sender) {
            sender.sendRichMessage(plugin.getLocale((Player) sender).flyEnabledOther());
        }

        return Command.SINGLE_SUCCESS;
    }

    private static byte flyOff(final CommandContext<CommandSourceStack> ctx) {
        final CommandSender sender = ctx.getSource().getSender();
        final Player player = (Player) ctx.getSource().getExecutor();

        if (player.getAllowFlight()) {
            player.setAllowFlight(false);
            player.sendRichMessage(plugin.getLocale(player).flyDisabled());
        } else {
            player.sendRichMessage(plugin.getLocale(player).flyAlreadyDisabled());
        }

        if (player != sender) {
            sender.sendRichMessage(plugin.getLocale((Player) sender).flyDisabledOther());
        }

        return Command.SINGLE_SUCCESS;
    }

    private static byte flyToggle(final CommandContext<CommandSourceStack> ctx) {
        final CommandSender sender = ctx.getSource().getSender();
        final Player player = (Player) ctx.getSource().getExecutor();

        if (player.getAllowFlight()) {
            player.setAllowFlight(false);
            player.sendRichMessage(plugin.getLocale(player).flyDisabled());
        } else {
            player.setAllowFlight(true);
            player.sendRichMessage(plugin.getLocale(player).flyEnabled());
        }

        if (player != sender) {
            sender.sendRichMessage(player.getAllowFlight() ? plugin.getLocale((Player) sender).flyDisabledOther() : plugin.getLocale((Player) sender).flyEnabledOther());
        }

        return Command.SINGLE_SUCCESS;
    }

    private static void literalHelp() {

    }

    private static void argumentSpeed() {

    }

    private static void argumentPay() {

    }

    private static void argumentAdd() {

    }

    private static void argumentRemove() {

    }

    private static void argumentSet() {

    }

    private static void argumentReset() {

    }

    private static CommandSourceStack copySourceForExecutor(final CommandSourceStack source, final Player executor) {
        return new CommandSourceStack() {
            @Override
            public @NotNull Location getLocation() {
                return source.getLocation();
            }

            @Override
            public @NotNull CommandSender getSender() {
                return source.getSender();
            }

            @Override
            public @Nullable Entity getExecutor() {
                return executor;
            }
        };
    }
}
