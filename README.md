# SUtils

Paper 26.3 plugin with small utilities and shenanigans.

## Commands
| Command | Permission (default) | What it does |
|---|---|---|
| `/enchant <player> <enchantment> [level]` | `sutils.enchant` (op) | Replaces vanilla. Any enchantment on any item or block in the target's main hand, any level up to `enchant.max-level`. Level 0 removes it. |
| `/upgrade` | `sutils.upgrade` (op) | Upgrades the tool in your hand (wooden to diamond sword, spear, pickaxe, axe, shovel, hoe) to netherite, keeping all other item data. |
| `/status <text\|remove>` | `sutils.status` (everyone) | Shows a status in front of your name in the tab list. `&` color codes work. Saved in `plugins/SUtils/status.yml`. |

Chat is formatted as `[status] Name: message` (operator names pink, others light gray; `&` codes work in messages with `sutils.chat.color`, default everyone; `chat.enabled` in the config).

Minecraft cannot store enchantment levels above 255 on an item, so that is the default and the technical maximum.

## Build
    export PATH=$(echo ~/mc-tools/jdk-25*)/bin:~/mc-tools/apache-maven-3.9.16/bin:$PATH; mvn package
    cp target/SUtils.jar ~/mc-server/plugins/
