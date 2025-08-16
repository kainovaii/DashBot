package fr.kainovaii.dashbot;

import fr.kainovaii.dashbot.commands.Command;
import fr.kainovaii.dashbot.listeners.SlashCommandListener;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.reflections.Reflections;
import io.github.cdimascio.dotenv.Dotenv;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Main
{
    public static void main(String[] args) throws Exception
        String DISCORD_TOKEN = "YOUR_SAFE_PLACEHOLDER";
        Dotenv dotenv = Dotenv.configure().directory("../").load();
        String token = dotenv.get("DISCORD_TOKEN");

        if (token == null || token.isEmpty()) {
            throw new IllegalStateException("Le token Discord n'a pas été trouvé !");
        }

        Reflections reflections = new Reflections("fr.kainovaii.dashbot.commands");
        Set<Class<? extends Command>> commandClasses = reflections.getSubTypesOf(Command.class);

        List<Command> commands = commandClasses.stream()
                .map(cls -> {
                    try {
                        return cls.getDeclaredConstructor().newInstance();
                    } catch (Exception e) {
                        e.printStackTrace();
                        return null;
                    }
                })
                .filter(cmd -> cmd != null)
                .collect(Collectors.toList());

        JDABuilder builder = JDABuilder.createDefault(token, GatewayIntent.GUILD_MESSAGES, GatewayIntent.GUILD_MEMBERS)
                .addEventListeners(new SlashCommandListener(commands));

        JDA jda = builder.build();
        jda.awaitReady();

        long guildId = 826490869359968326L;
        Guild guild = jda.getGuildById(guildId);

        if (guild != null) {
            guild.updateCommands().addCommands(
                    commands.stream().map(Command::getCommandData).toList()
            ).queue();
            System.out.println("Commandes mises à jour !");
        } else {
            System.out.println("La guild n'a pas été trouvée !");
        }
    }
}
