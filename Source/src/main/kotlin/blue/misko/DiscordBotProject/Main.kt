package blue.misko.DiscordBotProject


import blue.misko.DiscordBotProject.Commands.Birthday.happyBirthdayWishing
import blue.misko.DiscordBotProject.Objects.Listener
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers
import net.dv8tion.jda.api.*
import java.util.*

fun main(args: Array<String>) {

    Bot = JDABuilder(args[0])
        .addEventListeners(Listener())
        .build()

    Timer("Birthdays",false).scheduleAtFixedRate(
        DailyEvents(),
        getNextDateInGMT(millisecondsInAHour()*12),
        millisecondsInADay()
    )

    AudioSourceManagers.registerRemoteSources(playerManager)
}

class DailyEvents : TimerTask() {

    override fun run(){
        happyBirthdayWishing()
    }
}