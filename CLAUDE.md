# CLAUDE.md

## Project
Minecraft spigot, bungeecord plugin for teamcocoa core system

## Coding conventions
- No wildcard imports
- All Minecraft related module should be based on API, not try to reflect implementations
- No NMS class use (net.minecraft.server)
- No CraftBukkit class use
- Do not add comments logic is non-obvious
- Use PacketEvents for packet modification
- Use PacketEvents from (kr.teamcocoa.core.bukkit.packetevents)