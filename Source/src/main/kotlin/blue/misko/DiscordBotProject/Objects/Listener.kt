package blue.misko.DiscordBotProject.Objects

import net.dv8tion.jda.api.events.ReadyEvent
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import kotlin.system.exitProcess
import blue.misko.DiscordBotProject.Handlers.CommandManager
import blue.misko.DiscordBotProject.getPrefix


class Listener: ListenerAdapter() {
    private val manager = CommandManager()

    override fun onReady(event: ReadyEvent) {
        println("Bot is ready")
    }

    override fun onGuildMessageReceived(event: GuildMessageReceivedEvent){
        if(event.author.isBot){
            return
        }
        val prefix = getPrefix(event.guild.id)
        var command =event.message.contentRaw.decapitalize()
        if(event.author.id=="216095103104712706" && command=="shutdown"){
            exitProcess(0)
        }
        if(event.author.id=="216095103104712706" && command.startsWith("say in")){
            command = command.removePrefix("say in").trim()
            var meta = command.split("||")
            var guild = meta[0].split(" ")
            event.jda.getGuildById(guild[0])?.getTextChannelById(guild[1])?.sendMessage(meta[1])?.queue()
        }

        if(command.startsWith(prefix)){
            manager.handle(event)
        }
    }
}