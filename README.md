# ✈️ TideFly

> **Manage in-game flight with flexible controls** 🚀

A lightweight Paper Minecraft plugin for basic flight management with permission system and localization support.

## ✅ Currently Supported Features

🎯 **Basic Flight Control** - Enable, disable or toggle flight for yourself or other players  
⏰ **Unlimited Flight Time** - No time restrictions on flight duration  
🌐 **Localization Support** - Multi-language support with ISO standard locale files (e.g., `en_US.yml`)  
⚙️ **Configuration System** - General settings category of config.yml fully implemented   
🔐 **Comprehensive Permissions** - Detailed permission system compatible with permission management plugins  
💬 **Message System** - Internal and General message categories fully implemented  
🔄 **Reload Commands** - `/tidefly reload all|config|locale` commands working  
🎛️ **Custom Command Aliases** - Configure custom aliases for the `/tidefly` command in config.yml

## 📋 Planned Features (To Be Implemented)

### 🚀 Advanced Flight Management

- ⏱️ **Flight Duration Limits** - Set time-based flight restrictions for players
- 🏃 **Speed Control** - Adjust flight speed for customized experience
- 💸 **Flight Time Trading** - Allow players to exchange flight time with others

### 🛡️ Safety & Administration

- 🩹 **Fall Damage Protection** - Prevent damage when flight time expires
- ⚠️ **Expiration Warnings** - Advance notifications before flight time runs out
- 👑 **Admin Bypass** - Override flight time restrictions for administrators
- 🎁 **Daily Bonuses** - Set daily flight time rewards for players

### 📊 Data Management

- 🗄️ **Database Storage** - Network-wide flight data synchronization
- 📈 **Statistics Tracking** - Comprehensive flight usage analytics
- 🔄 **Data Migration** - Import/export flight data between servers

## 🔐 Permission System

TideFly includes a comprehensive permission system that works with permission management plugins.

### 🌟 Master Permissions

| Permission        | Default | Description                         |
|-------------------|---------|-------------------------------------|
| `tidefly.*`       | op      | All permissions for TideFly         |
| `tidefly.admin.*` | -       | All admin permissions for TideFly   |
| `tidefly.cmd.*`   | -       | All command permissions for TideFly |

### 👑 Admin Permissions

| Permission                        | Description                    |
|-----------------------------------|--------------------------------|
| `tidefly.admin`                   | Basic admin permission         |
| `tidefly.admin.cmd`               | Access to admin commands       |
| `tidefly.admin.cmd.add`           | Add fly time to a player       |
| `tidefly.admin.cmd.remove`        | Remove fly time from a player  |
| `tidefly.admin.cmd.set`           | Set fly time for a player      |
| `tidefly.admin.cmd.reset`         | Reset fly time for a player    |
| `tidefly.admin.cmd.info`          | Display plugin information     |
| `tidefly.admin.cmd.reload`        | Reload plugin configuration    |
| `tidefly.admin.cmd.reload.all`    | Reload all configurations      |
| `tidefly.admin.cmd.reload.config` | Reload main configuration file |
| `tidefly.admin.cmd.reload.locale` | Reload language files          |

### 🎮 Player Permissions

| Permission                  | Default | Description                                                           |
|-----------------------------|---------|-----------------------------------------------------------------------|
| `tidefly.cmd`               | true    | Use /tidefly command                                                  |
| `tidefly.cmd.on`            | -       | Enable fly                                                            |
| `tidefly.cmd.on.others`     | -       | Enable fly for others                                                 |
| `tidefly.cmd.off`           | -       | Disable fly                                                           |
| `tidefly.cmd.off.others`    | -       | Disable fly for others                                                |
| `tidefly.cmd.toggle`        | -       | Toggle fly                                                            |
| `tidefly.cmd.toggle.others` | -       | Toggle fly for others                                                 |
| `tidefly.cmd.status`        | -       | View fly status                                                       |
| `tidefly.cmd.status.others` | -       | View fly status for others                                            |
| `tidefly.cmd.help`          | -       | View command help                                                     |
| `tidefly.cmd.help.others`   | -       | View command help for others                                          |
| `tidefly.cmd.speed`         | -       | Change fly speed                                                      |
| `tidefly.cmd.speed.others`  | -       | Change fly speed for others                                           |
| `tidefly.cmd.pay`           | -       | Pay fly time to another player                                        |
| `tidefly.cmd.pay.others`    | -       | Pay fly time to another player for others                             |
| `tidefly.cmd.others`        | -       | Execute commands for other players (groups all `.others` permissions) |

## 🛠️ Installation & Requirements

### Prerequisites

- **Java 21+** - Required for optimal performance
- **Paper 1.21+** - Compatible with Paper server versions 1.21 and above
- **Permission Plugin** - LuckPerms, PermissionsEx, or similar (recommended)

### Installation Steps

1. **Download the plugin:**
   Get the latest release from GitHub Releases

2. **Install the plugin:**
   Place the .jar file in your server's plugins folder

3. **Restart your server:**
   Stop and start your server to load TideFly

4. **Configure the plugin:**
   Edit config.yml and language files in plugins/TideFly/

## ⚙️ Configuration

### 📁 Configuration Files

TideFly generates several configuration files:

- **`config.yml`** - Main plugin configuration (general settings implemented)
- **`en_US.yml`** - English language file (Internal and General messages working)
- **Custom locales** - Add files following ISO standard (e.g., `de_DE.yml`)

### 🌐 Localization

The plugin automatically selects the appropriate language file based on player locale:

```yaml
# Example locale files:
en_US.yml    # English (United States)
cs_CZ.yml    # Czech (Czech Republic)
de_DE.yml    # German (Germany)
fr_FR.yml    # French (France)
```

### 🎯 Current Config Features

- ✅ **General Settings** - Basic plugin configuration
- ✅ **Message Categories** - Internal and General message systems
- ✅ **Command Aliases** - Custom aliases for plugin commands
- 🔄 **Advanced Settings** - Database, timing, and safety features (planned)

## 🚀 Usage Guide

### Currently Working Commands

```
/tidefly on [player]           # Enable flight
/tidefly off [player]          # Disable flight
/tidefly toggle [player]       # Toggle flight status
/tidefly reload all            # Reload all configurations
/tidefly reload config         # Reload main configuration
/tidefly reload locale         # Reload language files
```

### Planned Commands

```
/tidefly status [player]       # Check flight status
/tidefly speed <value>         # Set flight speed
/tidefly help [page]           # Display help information
/tidefly pay <player> <time>   # Transfer flight time
```

## 🔧 Development Status

### ✅ Implemented Features

- **Core Flight Control** - Basic enable/disable functionality
- **Permission System** - Complete permission structure
- **Localization Framework** - Multi-language support foundation
- **Configuration System** - Basic config.yml support
- **Message System** - Internal and General categories
- **Reload Commands** - Configuration and locale reloading

### 🚧 In Development

- **Advanced Commands** - Toggle, status, speed control
- **Time Management** - Duration limits and tracking
- **Safety Features** - Fall protection and warnings
- **Database Integration** - Network-wide data storage
- **Admin Tools** - Complete administrative interface

### 📅 Future Plans

- **Web Interface** - Browser-based administration panel
- **API Integration** - Third-party plugin compatibility
- **Statistics Dashboard** - Comprehensive usage analytics

## 🔍 Troubleshooting

### Common Issues

#### ❌ Commands not working

**Causes:**

- Missing permissions
- Plugin not properly loaded
- Configuration errors

**Solutions:**

- Check permission assignments with your permission plugin
- Verify plugin loaded with `/plugins`
- Review server console for errors

#### ❌ Language not switching

**Causes:**

- Missing locale file
- Incorrect file naming
- Client locale detection issues

**Solutions:**

- Ensure locale file follows ISO standard (e.g., `en_US.yml`)
- Check file encoding is UTF-8
- Verify player's client locale settings

## 🤝 Contributing

You can help improve TideFly by providing feedback and suggestions:

### 🐛 **Issue Reporting**

- Report bugs and unexpected behavior
- Suggest new features and improvements
- Share compatibility issues with other plugins
- Document configuration problems

### 💡 **Feature Suggestions**

- Propose new flight management features
- Suggest UI/UX improvements
- Share integration ideas with other plugins

Feel free to open issues on GitHub with your feedback!

## 📄 License

This project is licensed under the **GNU General Public License v3.0**.

📖 See [LICENSE.txt](LICENSE.txt) for full details  
🌐 Or visit [https://www.gnu.org/licenses/gpl-3.0.txt](https://www.gnu.org/licenses/gpl-3.0.txt)

## ⚠️ Important Notes

- 🚧 **Limited Functionality** - Many advertised features are not yet implemented
- 🔐 **Permission Compatible** - Works with all major permission plugins
- 🌐 **Locale Support** - Automatic language detection and switching
- ⏰ **Unlimited Flight** - Currently no time restrictions on flight duration
- 📚 **Documentation** - Wiki will be available once the project reaches a stable working state

---

**Basic flight control for your Paper server!** ✈️
