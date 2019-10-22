package blue.mismas.fAnnedomBot

import net.dv8tion.jda.api.*
import net.dv8tion.jda.api.entities.*
import net.dv8tion.jda.api.events.message.*
import net.dv8tion.jda.api.hooks.*

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
        if(event.message.contentRaw.startsWith(prefix)){
            var command =event.message.contentRaw.removePrefix(prefix)
            executeCommand(event, command.trim())
        }
    }
}