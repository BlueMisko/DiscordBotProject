package blue.misko.DiscordBotProject

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import net.dv8tion.jda.api.managers.AudioManager
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist
import com.sedmelluq.discord.lavaplayer.track.AudioTrack


fun executeMusicCommands (event: MessageReceivedEvent, parameters: String){
    when (true){
        parameters.startsWith("play")->executeMusicPlayCommand(event, parameters.removePrefix("play").trim())
        parameters.startsWith("stop")->executeMusicStopCommand(event, parameters.removePrefix("remember").trim())
        //parameters.startsWith("")->{}

        /*else -> {
            var prefix =getPrefix(event.guild.id)
            if(prefix.length > 1)
                prefix += " "
            event.channel.sendMessage("That isn't a music command. Try using " + prefix +"Birthday help").queue()
        }*/
    }
    event.channel.sendMessage("Music commands aren't implemented yet").queue()
}


fun executeMusicPlayCommand(event: MessageReceivedEvent, parameters: String) {
    val guild = event.guild

    val voiceState = event.member!!.voiceState!!

    if(!voiceState.inVoiceChannel()){
        event.channel.sendMessage("You need to be in a voice channel to use this command").queue()
        return
    }

    val channel = voiceState.channel!!
    val manager = guild.audioManager

    val player = playerManager.createPlayer()

    val trackScheduler = TrackScheduler()
    player.addListener(trackScheduler)

    manager.sendingHandler = AudioPlayerSendHandler(player)
    manager.openAudioConnection(channel)

    playerManager.loadItem(parameters,  object: AudioLoadResultHandler {

        override fun trackLoaded(track: AudioTrack) {
            player.playTrack(track)
        }

        override fun playlistLoaded(playlist: AudioPlaylist) {
        }

        override fun noMatches() {
            // Notify the user that we've got nothing
        }

        override fun loadFailed(throwable: FriendlyException) {
            // Notify the user that everything exploded
        }
    })



}

fun executeMusicStopCommand(event: MessageReceivedEvent, parameters: String) {

}