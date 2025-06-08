# NexTier

**NexTier** is a powerful and flexible Minecraft plugin that allows you to create upgradeable items with a customizable GUI and configuration-driven system. It supports dynamic upgrades, permissions, economy integration (via Vault), and full customization of items and messages.

---

## ✨ Features

* 🔧 Upgrade items through a beautiful custom GUI
* 📚 Configure upgrade types and max levels via `config.yml`
* 💬 Fully customizable messages with MiniMessage support (`messages.yml`)
* 🧱 Custom GUI items through `items.yml`
* 💰 Economy integration using Vault
* 🎯 Uses PDC (PersistentDataContainer) for internal identification
* 🔁 Supports `/nextier reload` for hot config reloading

---

## 📦 Installation

1. Download the plugin `.jar` file and place it in your server's `/plugins/` folder
2. Make sure **Vault** is installed on your server
3. Restart the server or use `/reload`
4. Configure the plugin files generated in `/plugins/NexTier/`

---

## 🛠 Configuration Files

### `config.yml`

Defines the available upgrade types:

```yaml
upgrades:
  upgrade1:
    enchant: EFFICIENCY
    max-level: 5
    display-name: Efficiency
  upgrade2:
    enchant: UNBREAKING
    max-level: 3
    display-name: Unbreaking
```

### `messages.yml`

All plugin messages with MiniMessage support:

```yaml
prefix: "<bold><aqua>NexTier</aqua></bold> <gray>|</gray> "
messages:
  SUCCESS: "<green>Successfully upgraded %UPGRADE% to level %LEVEL%!</green>"
  CANCELLED: "<red>Upgrade cancelled.</red>"
  NOT_ENOUGH_MONEY: "<red>You need <gold>%PRICE%</gold>$ to upgrade.</red>"
  ...
```

### `items.yml`

Define GUI buttons/items with full control:

```yaml
buttons:
  CONFIRM:
    material: GREEN_WOOL
    custom-model-data: 1111
    name: "<green>Confirm - %PRICE%$"
    lore:
      - "Click to confirm upgrade"
```

---

## 🎮 Commands

| Command                | Description                                        |
| ---------------------- | -------------------------------------------------- |
| `/nextier item add`    | Adds the item in your hand to the upgradeable list |
| `/nextier item remove` | Removes the held item from the upgradeable list    |
| `/nextier item list`   | Lists all currently upgradeable items              |
| `/nextier reload`      | Reloads all plugin configuration files             |

---

## 🧠 How It Works

1. Admins define upgradeable materials via `/nextier item add`
2. Players sneak + right-click to open upgrade GUI
3. Upgrade options are displayed depending on the item’s current level
4. Confirming an upgrade costs money via Vault
5. All names, messages, buttons, and enchantments are defined in config files

---

## 🧩 Dependencies

* [Vault](https://www.spigotmc.org/resources/vault.34315/) – required for economy handling

---

## 📸 Screenshots

*Coming soon!*

---

## 🧪 Permissions

| Permission      | Description                                         |
| --------------- | --------------------------------------------------- |
| `nextier.admin` | Allows access to admin commands (add/remove/reload) |

---

## 📄 License

This plugin is licensed under MIT. See [LICENSE](LICENSE) for more.

---

## 🙋 Support

Feel free to open an issue or discussion on GitHub if you have any questions or suggestions!

---

developed with ☕ and ❤️ by **JustMaya**
