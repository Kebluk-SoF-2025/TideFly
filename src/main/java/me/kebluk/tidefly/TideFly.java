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

import org.bukkit.plugin.java.JavaPlugin;

public class TideFly extends JavaPlugin {
    @Override
    public void onLoad() {
        getLogger().info("TideFly is loading...");
    }

    @Override
    public void onEnable() {
        getLogger().info("TideFly has been enabled!");
    }

    @Override
    public void onDisable() {
        getLogger().info("TideFly has been disabled!");
    }
}