# SUtils

Paper 26.3 plugin with small utilities and shenanigans.

## Commands
| Command | Permission (default) | What it does |
|---|---|---|
| `/enchant <player> <enchantment> [level]` | `sutils.enchant` (op) | Replaces vanilla. Any enchantment on any item or block in the target's main hand, any level up to `enchant.max-level`. Level 0 removes it. |
| `/status <text\|remove>` | `sutils.status` (everyone) | Shows a status in front of your name in the tab list. `&` color codes work. Saved in `plugins/SUtils/status.yml`. |

Chat is formatted as `[status] Name: message` (operator names pink, others light gray; `chat.enabled` in the config).

Minecraft cannot store enchantment levels above 255 on an item, so that is the default and the technical maximum.

## Build
    export PATH=$(echo ~/mc-tools/jdk-25*)/bin:~/mc-tools/apache-maven-3.9.16/bin:$PATH; mvn package
    cp target/SUtils.jar ~/mc-server/plugins/
