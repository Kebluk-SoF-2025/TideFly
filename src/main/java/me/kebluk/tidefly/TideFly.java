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

import me.kebluk.tidefly.config.ConfigManager;
import me.kebluk.tidefly.config.LocaleConfig;
import me.kebluk.tidefly.config.MainConfig;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;

public class TideFly extends JavaPlugin {
    private ConfigManager configManager;

    public static TideFly getInst() {
        return getPlugin(TideFly.class);
    }

    @Override
    public void onLoad() {
        getLogger().info("TideFly is loading...");

        // Initialize configuration and locale
        configManager = new ConfigManager(this);
        configManager.loadConfigs();
    }

    @Override
    public void onEnable() {
        getLogger().info("TideFly has been enabled!");
    }

    @Override
    public void onDisable() {
        getLogger().info("TideFly has been disabled!");
    }

    public @NotNull MainConfig getMainConfig() {
        return configManager.getMainConfig();
    }

    public LocaleConfig getLocale() {
        return configManager.getLocale(getMainConfig().lang());
    }

    public LocaleConfig getLocale(String locale) {
        return configManager.getLocale(locale);
    }

    public LocaleConfig getLocale(Player player) {
        if (!getMainConfig().localization()) {
            return getLocale();
        }

        Locale playerLocale = player.locale();
        return (playerLocale.hasExtensions() ? getLocale(playerLocale.toString()) : getLocale());
    }
}