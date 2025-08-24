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

package me.kebluk.tidefly.config;

public record LocaleConfig(String reloading, String reloaded, String noPermission, String playerOnly,

                           String flyEnabled, String flyDisabled, String flyEnabledOther, String flyDisabledOther,
                           String flyAlreadyEnabled, String flyAlreadyDisabled) {
}