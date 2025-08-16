package fr.kainovaii.dashbot.commands;

import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public interface Command
{
    String getName();
    SlashCommandData getCommandData();
    void execute(SlashCommandInteractionEvent event);
}
