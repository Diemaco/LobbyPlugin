package com.gmail.mariodeu2.ffa.commands;

import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface CommandInterface {
    @NotNull
    String getName();

    @NotNull
    String getUsage();

    @NotNull
    String getDescription();

    boolean execute(CommandSender commandSender, String s, String[] args);

    @Nullable
    default List<String> tabComplete(CommandSender commandSender, String s, String[] args) {
        return null;
    }
}
