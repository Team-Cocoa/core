package kr.teamcocoa.core.bungee.commands;

import kr.teamcocoa.core.utils.StringUtils;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Command;

public class DiscordCommand extends Command {

    public DiscordCommand(String name) {
        super(name);
    }

    @Override
    public void execute(CommandSender commandSender, String[] strings) {
        commandSender.sendMessage(TextComponent.fromLegacyText(StringUtils.color(
                "&a[&dTeamCocoa&a] &9https://discord.teamcocoa.kr")
        ));
    }
}
