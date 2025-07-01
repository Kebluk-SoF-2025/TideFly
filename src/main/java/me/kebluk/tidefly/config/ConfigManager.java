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

import me.kebluk.tidefly.TideFly;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * The {@link ConfigManager} class is responsible for managing the configuration files
 * for the TideFly plugin. This includes loading, updating, and validating configuration
 * files and locale files. The class ensures that the necessary defaults are extracted
 * and keeps the configuration files up to date with the latest structure.
 */
public class ConfigManager {
    private final TideFly plugin;
    private final Map<String, LocaleConfig> locales = new HashMap<>();
    private final Path MAIN_CONFIG_PATH;
    private final Path LOCALE_CONFIG_PATH;
    private final int MAIN_CONFIG_VERSION = 1;
    private final int LOCALE_CONFIG_VERSION = 1;
    private MainConfig mainConfig;

    public ConfigManager(final TideFly plugin) {
        this.plugin = plugin;
        MAIN_CONFIG_PATH = plugin.getDataPath().resolve("config.yml");
        LOCALE_CONFIG_PATH = plugin.getDataPath().resolve("locale");
    }

    public void loadConfigs() {
        loadMainConfig();
        loadLocales();
    }

    /**
     * Loads the main configuration file for the plugin. If the configuration file does not exist,
     * extracts the default configuration file from the plugin JAR and places it in the plugin's data folder.
     * <p>
     * The method validates the configuration file's version, logs appropriate messages, and updates it
     * if necessary. It parses all the settings from the configuration file to populate a {@link MainConfig} instance.
     *
     * @throws RuntimeException         If the plugin folder cannot be created, or if the default configuration file cannot be extracted.
     * @throws NullPointerException     If required configuration fields are missing.
     * @throws IllegalArgumentException If an invalid storage type is specified.
     */
    public void loadMainConfig() {
        ensureExists(MAIN_CONFIG_PATH);

        final YamlConfiguration yml = YamlConfiguration.loadConfiguration(MAIN_CONFIG_PATH.toFile());
        checkConfigVersion(yml, MAIN_CONFIG_PATH, MAIN_CONFIG_VERSION);

        final String typeStr = Objects.requireNonNull(yml.getString("storage.type"));
        final StorageType type = StorageType.fromValue(typeStr);
        String host = Objects.requireNonNull(yml.getString("storage.remote.host"));

        if (type.isRemote() && !host.contains(":")) {
            host = host + ":" + type.getDefaultPort();
        }

        mainConfig = new MainConfig(yml.getString("general.lang"), yml.getBoolean("general.localization"),

                typeStr, host, yml.getString("storage.remote.database"), yml.getString("storage.remote.username"), yml.getString("storage.remote.password"), yml.getString("storage.local.file"),

                yml.getString("messaging.type"));

        plugin.getSLF4JLogger().info("Loaded main config: {}", MAIN_CONFIG_PATH);
    }

    /**
     * Loads a locale configuration from the specified path.
     * This method extracts the locale name from the file name, validates the configuration's version,
     * and stores the locale-specific messages in the {@link #locales} map.
     *
     * @param path The path of a file containing the locale configuration in YAML format.
     */
    public void loadLocale(final Path path) {
        final String localeName = path.getFileName().toString().replace(".yml", "");
        final YamlConfiguration yml = YamlConfiguration.loadConfiguration(path.toFile());
        checkConfigVersion(yml, path, LOCALE_CONFIG_VERSION);
        locales.put(localeName, new LocaleConfig(yml.getString("reloading"), yml.getString("reloaded"), yml.getString("no-permission")));
    }

    /**
     * Loads all locale configuration files from the locale path.
     * If the locale directory does not exist, it will create the directory and extract a default
     * locale file from the plugin's resources.
     * <p>
     * This method searches for all files with a `.yml` extension in the locale directory and
     * processes them in parallel using {@link #loadLocale(Path)}. Validated locale configurations are
     * added to the {@link #locales} map. Logs will indicate the success or failure of these actions.
     *
     * @throws RuntimeException If the locale directory cannot be created.
     */
    public void loadLocales() {
        ensureExists(LOCALE_CONFIG_PATH.resolve("en_US.yml"));

        try (final Stream<Path> stream = Files.list(LOCALE_CONFIG_PATH)) {
            // Load all locale files in parallel
            stream.filter(Files::isRegularFile)
                    .filter(path -> path.toString().endsWith(".yml"))
                    .parallel()
                    .forEach(this::loadLocale);
        } catch (final Exception e) {
            plugin.getSLF4JLogger().warn("No locale files found in directory: {}", LOCALE_CONFIG_PATH);
            return;
        }

        plugin.getSLF4JLogger().info("Loaded locales: {}", String.join(", ", locales.keySet()));
    }

    /**
     * Retrieves a resource file as an {@link InputStream} from the plugin's JAR.
     * If the specified resource cannot be found, a {@link RuntimeException} is thrown.
     *
     * @param resourcePath The path to the resource file within the plugin JAR.
     *                     This can be a relative or full path.
     * @return An {@link InputStream} for the specified resource.
     * @throws RuntimeException if the specified resource is not found within the JAR.
     */
    private InputStream getResource(final Path resourcePath) {
        final String fileName = resourcePath.getFileName().toString();
        final InputStream input = plugin.getResource(fileName);

        if (input == null) {
            throw new RuntimeException("Default resource file '" + resourcePath + "' not found in JAR.");
        }
        return input;
    }

    /**
     * Extracts a resource file from the plugin's JAR to the plugin's data folder.
     * If the resource file already exists at the target location, the method does nothing.
     * If the file does not exist, it is copied from the JAR to the data folder.
     * This ensures that default configuration or localization files are available.
     *
     * @param destinationPath The relative path to the resource file within the plugin JAR.
     *                        The file will be extracted to the plugin's data folder,
     *                        preserving the relative path structure.
     * @throws RuntimeException If any error occurs while extracting the resource,
     *                          such as missing resources in the JAR or issues with file operations.
     */
    private void extractResource(final Path destinationPath) {
        if (Files.exists(destinationPath)) {
            return;
        }

        try (final InputStream input = getResource(destinationPath)) {
            Files.createDirectories(destinationPath.getParent()); // Ensure directory exists
            Files.copy(input, destinationPath, StandardCopyOption.REPLACE_EXISTING);
            plugin.getSLF4JLogger().info("Successfully extracted default resource '{}'.", destinationPath);
        } catch (final Exception e) {
            throw new RuntimeException("Failed to extract default resource '" + destinationPath + "'.", e);
        }
    }

    /**
     * Ensures the specified file exists at the given path. If the file does not exist,
     * this method attempts to create the necessary parent directories and extracts
     * the default resource to the specified path. If the file already exists, the method
     * logs a message and skips extraction.
     *
     * @param path The path to the file that should be checked and potentially created.
     *             This path represents the target location where the default resource
     *             will be extracted, if the file is missing.
     * @throws RuntimeException If the parent directories cannot be created or if the
     *                          default resource cannot be extracted to the specified
     *                          path.
     */
    private void ensureExists(final Path path) {
        if (!Files.exists(path)) {
            plugin.getSLF4JLogger().info("File '{}' does not exist, extracting default...", path);
            try {
                Files.createDirectories(path.getParent());
            } catch (final Exception e) {
                throw new RuntimeException("Failed to create necessary directories.");
            }
            extractResource(path);
        } else {
            plugin.getSLF4JLogger().info("File '{}' already exists, skipping extraction.", path);
        }
    }

    /**
     * Checks the version of the provided configuration file against the expected version.
     * Logs the current status of the configuration file, indicating whether it is up to date,
     * outdated, or newer than expected.
     * Updates the configuration file to ensure it has the latest version and structure.
     *
     * @param yml             The {@link YamlConfiguration} object representing the configuration file.
     * @param filePath        The path to the configuration file being checked.
     * @param expectedVersion The expected version number of the configuration file.
     */
    private void checkConfigVersion(final YamlConfiguration yml, final Path filePath, final int expectedVersion) {
        final int version = yml.getInt("version", 0);
        if (version < expectedVersion) {
            plugin.getSLF4JLogger().info("Configuration file '{}' is outdated (version: {}; expected version: {}).", filePath, version, expectedVersion);
        } else if (version > expectedVersion) {
            plugin.getSLF4JLogger().warn("Configuration file '{}' is newer than expected (version: {}; expected version: {}).", filePath, version, expectedVersion);
        } else {
            plugin.getSLF4JLogger().info("Configuration file '{}' is up-to-date (version: {}).", filePath, version);
        }
        update(yml, filePath); // Update the configuration file anyway to ensure it has the latest and proper structure
    }

    /**
     * Updates the specified YAML configuration file by comparing it with the default configuration and
     * ensuring that all keys and values from the default configuration are present. Backups the original
     * configuration file before updating and restores missing or outdated values. The version of the configuration
     * file is also updated to match the expected version.
     *
     * @param yml      The {@link YamlConfiguration} object representing the current configuration file to be updated.
     * @param filePath The relative file path to the configuration file being updated, located within the plugin's
     *                 data folder.
     * @throws RuntimeException If a backup cannot be created, the default configuration cannot be loaded, or if
     *                          an error occurs during the update process.
     */
    private void update(YamlConfiguration yml, final Path filePath) {
        plugin.getSLF4JLogger().info("Automatically updating configuration file '{}' for possible older version or missing values.", filePath);

        //Make a backup of the original file
        final Path backupPath = filePath.resolveSibling(filePath.getFileName() + ".bak");

        if (Files.exists(filePath) && !Files.exists(backupPath)) {
            try {
                Files.copy(filePath, backupPath, StandardCopyOption.REPLACE_EXISTING);
                plugin.getSLF4JLogger().info("Backup of configuration file '{}' created as '{}'.", filePath, backupPath.getFileName());
            } catch (final Exception e) {
                throw new RuntimeException("Failed to create backup of configuration file '" + filePath + "' and thus an update task: {}", e);
            }
        }

        // Determine the default file name based on the file path
        // If the file is a locale file, use the en_US locale file name; otherwise, use the main config file name
        final Path fileName = filePath.toString().contains(LOCALE_CONFIG_PATH.toString()) ? Path.of("en_US.yml") : MAIN_CONFIG_PATH.getFileName();

        final YamlConfiguration defaultYml = new YamlConfiguration();
        try (final InputStream input = getResource(fileName)) {
            defaultYml.options().parseComments(true);
            defaultYml.load(new InputStreamReader(input));
        } catch (final Exception e) {
            throw new RuntimeException("Failed to load default resource '" + fileName + "'.", e);
        }

        for (final String key : defaultYml.getKeys(true)) {
            if (yml.contains(key)) {
                defaultYml.set(key, yml.get(key));
            }
        }

        defaultYml.set("version", fileName.equals(MAIN_CONFIG_PATH) ? MAIN_CONFIG_VERSION : LOCALE_CONFIG_VERSION);
        yml = defaultYml;

        try {
            yml.save(filePath.toFile());
            plugin.getSLF4JLogger().info("Successfully updated and saved configuration file '{}'. Deleting backup.", filePath);
            Files.deleteIfExists(backupPath);
        } catch (final Exception e) {
            plugin.getSLF4JLogger().error("Failed to save updated configuration file '{}': {}", filePath, e.getMessage());
        }
    }

    public MainConfig getMainConfig() {
        return mainConfig;
    }

    public LocaleConfig getLocale(final String locale) {
        return locales.getOrDefault(locale, locales.get(mainConfig.lang()));
    }
}