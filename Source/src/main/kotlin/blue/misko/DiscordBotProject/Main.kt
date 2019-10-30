package blue.misko.DiscordBotProject

import net.dv8tion.jda.api.*
import net.dv8tion.jda.api.entities.*
import net.dv8tion.jda.api.events.message.*
import net.dv8tion.jda.api.hooks.*
import kotlin.system.exitProcess

fun main(args: Array<String>) {
    var bot = JDABuilder(args[0])
            .setActivity(Activity.listening("your shit"))
            .build()
    bot.addEventListener(Bot())
}

class Bot: ListenerAdapter() {
    override fun onMessageReceived(event: MessageReceivedEvent){
        if(event.author.isBot){
            return
        }
        val prefix = GetPrefix(event.guild.id)
        var command =event.message.contentRaw.decapitalize()
        if(event.author.id=="216095103104712706" && command=="admin stop"){
            exitProcess(0)
        }
        if(command.startsWith(prefix)){
            command = command.removePrefix(prefix)
            executeCommand(event, command.trim())
        }
    }
}