# HelpMe-X Plugin

## version

**Current Version:** 1.2.0

## Requirements

- Paper/Spigot 1.8.x – 1.21.x
- Java 8 or higher
- Maven 3.6+ (for building from source)

## Building

To build the plugin from source:

```bash
mvn clean package
```

## Installation

1. Download the latest release or build from source
2. Copy the JAR file into your server's `plugins/` folder
3. Restart your server (or use `/reload` if you prefer)
4. The plugin will automatically create `config.yml` and `data.yml` on first run

## Configuration

The `config.yml` file contains the following options:

### Language

- **Option:** `language`
- **Values:** `EN` (English) or `FA` (Persian/فارسی)
- **Default:** `EN`
- **Description:** Changes all plugin messages between formal English and formal Persian. The prefix remains the same regardless of language setting.

### Menu Mode

- **Option:** `menu-mode`
- **Values:** `text` or `menu`
- **Default:** `menu`
- **Description:** Controls how the `/helplist` command displays help requests:
  - `text` - Shows requests as clickable text messages in chat
  - `menu` - Shows requests in a GUI menu with hover tooltips (5 rows minimum)

### Colored Messages

- **Option:** `colored-messages`
- **Values:** `true` or `false`
- **Default:** `true`
- **Description:** When enabled, commands mentioned in messages will be automatically highlighted in yellow for better readability. The prefix color remains unchanged.

**Note:** Remember to restart or reload your server after changing any configuration options.

## Commands

### Player Commands

- `/helpme <message>` - Submit a help request with a description of your issue
- `/helpchat <message>` - Send a private message to your assigned helper (or player if you're a helper)

### Helper/Staff Commands

- `/helpaccept <player>` - Accept and take ownership of a player's help request
- `/helplist` - View all pending help requests (displays as text or GUI menu based on config)
- `/helpreject <player>` - Reject and close a help request without providing assistance
- `/helptp <player>` - Teleport to the location of a player you're helping
- `/helpcomplete` - Mark the current help session as complete
- `/helpadmins` - View statistics for all helpers (accepts and rejects)

## Permissions

- `helpme.use` - **Default:** `true` - Allows players to use `/helpme` and `/helpchat`
- `helpme.helper` - **Default:** `op` - Grants access to all helper/admin commands:
  - `/helpaccept`
  - `/helplist`
  - `/helpreject`
  - `/helptp`
  - `/helpcomplete`
  - `/helpadmins`

Enjoy!
