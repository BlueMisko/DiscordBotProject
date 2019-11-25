package blue.misko.DiscordBotProject

import net.dv8tion.jda.api.*
import net.dv8tion.jda.api.entities.*
import net.dv8tion.jda.api.events.message.*
import net.dv8tion.jda.api.hooks.*
import java.util.*
import kotlin.system.exitProcess

fun main(args: Array<String>) {
    var bot = JDABuilder(args[0])
            .setActivity(Activity.listening("your shit"))
            .build()

    System.currentTimeMillis()


    Timer("Birthdays",false).scheduleAtFixedRate(
        HappyBirthdayWishing(bot),
        getNextDateInGMT(12 * millisecondsInAHour()),
        millisecondsInADay()
    )
    bot.addEventListener(Bot())
}

class Bot: ListenerAdapter() {
    override fun onMessageReceived(event: MessageReceivedEvent){
        if(event.author.isBot){
            return
        }
        val prefix = getPrefix(event.guild.id)
        var command =event.message.contentRaw.decapitalize()
        if(event.author.id=="216095103104712706" && command=="admin stop"){
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