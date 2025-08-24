/*
 * This file is part of TideFly.
 * Copyright (C) 2025 Kebluk
 *
 * TideFly is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * TideFly is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 *
 * See the LICENSE file or https://www.gnu.org/licenses/ for details.
 */

package me.kebluk.tidefly;

import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import me.kebluk.tidefly.commands.FlyCommand;
import me.kebluk.tidefly.config.ConfigManager;
import me.kebluk.tidefly.config.LocaleConfig;
import me.kebluk.tidefly.config.MainConfig;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class TideFly extends JavaPlugin {
    private ConfigManager configManager;

    public static TideFly getInst() {
        return getPlugin(TideFly.class);
    }

    @Override
    public void onLoad() {
        // Initialize configuration and locale
        configManager = new ConfigManager(this);
        configManager.loadConfigs();

        getLogger().info("TideFly has been loaded!");
    }

    @SuppressWarnings("UnstableApiUsage")
    @Override
    public void onEnable() {
        // Register commands
        getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS, commands -> {
            commands.registrar().register(FlyCommand.create(), getMainConfig().commandAliases());
        });
        getLogger().info("Successfully registered commands!");

        getLogger().info("TideFly has been enabled!");
    }

    @Override
    public void onDisable() {
        getLogger().info("TideFly has been disabled!");
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    public MainConfig getMainConfig() {
        return configManager.getMainConfig();
    }

    public LocaleConfig getLocale() {
        return configManager.getLocale(getMainConfig().lang());
    }

    public LocaleConfig getLocale(final String locale) {
        return configManager.getLocale(locale);
    }

    public LocaleConfig getLocale(final Player player) {
        if (!getMainConfig().localization()) {
            return getLocale();
        }

        return getLocale(player.locale().toString());
    }
}