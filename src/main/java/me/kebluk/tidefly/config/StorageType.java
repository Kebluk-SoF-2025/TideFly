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

import java.util.Arrays;

/**
 * Represents supported storage types for configuration and their associated default ports.
 * Each storage type has a string identifier and a default port number.
 */
public enum StorageType {
    MYSQL("mysql", 3306),
    MARIADB("mariadb", 3306),
    POSTGRESQL("postrgresql", 5432),
    MONGODB("mongodb", 27017),
    SQLITE("sqlite", 0),
    JSON("json", 0);

    private final String value;
    private final int defaultPort;

    StorageType(final String value, final int defaultPort) {
        this.value = value;
        this.defaultPort = defaultPort;
    }

    public static StorageType fromValue(final String value) {
        for (final StorageType type : values()) {
            if (type.value.equalsIgnoreCase(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Invalid storage type: " + value + ". Valid types are: " + String.join(", ", Arrays.stream(values()).map(StorageType::getConfigValue).toArray(String[]::new)));
    }

    public String getConfigValue() {
        return value;
    }

    public int getDefaultPort() {
        return defaultPort;
    }

    public boolean isRemote() {
        return this != SQLITE && this != JSON;
    }
}