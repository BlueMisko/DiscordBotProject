package blue.misko.DiscordBotProject

import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers
import net.dv8tion.jda.api.*
import net.dv8tion.jda.api.entities.*
import net.dv8tion.jda.api.events.message.*
import net.dv8tion.jda.api.hooks.*
import java.util.*
import kotlin.system.exitProcess

fun main(args: Array<String>) {

    Bot = JDABuilder(args[0])
        .addEventListeners(BotHandler())
        .setActivity(Activity.listening("your shit"))
        .build()

    Timer("Birthdays",false).scheduleAtFixedRate(
        DailyEvents(),
        getNextDateInGMT(millisecondsInAHour()*12),
        millisecondsInADay()
    )

    AudioSourceManagers.registerRemoteSources(playerManager)
}

class BotHandler: ListenerAdapter() {
    override fun onMessageReceived(event: MessageReceivedEvent){
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
            command = command.removePrefix(prefix)
            executeCommand(event, command.trim())
        }
    }
}

class DailyEvents : TimerTask() {

    override fun run(){
        happyBirthdayWishing()
    }
}