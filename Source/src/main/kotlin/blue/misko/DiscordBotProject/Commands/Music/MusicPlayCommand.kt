package blue.misko.DiscordBotProject.Commands.Music

import blue.misko.DiscordBotProject.Interface.ICommand
import blue.misko.DiscordBotProject.Objects.AudioPlayerSendHandler
import blue.misko.DiscordBotProject.Objects.CommandContext
import blue.misko.DiscordBotProject.Objects.TrackScheduler
import blue.misko.DiscordBotProject.playerManager
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist
import com.sedmelluq.discord.lavaplayer.track.AudioTrack
import net.dv8tion.jda.api.Permission
import java.util.ArrayList

class MusicPlayCommand: ICommand {
    override val subCommands: ArrayList<ICommand>?
        get() = null
    override val neededPermissions: ArrayList<Permission>?
        get() = null
    override val neededRank: Int
        get() = 0
    override val hiddenCommand: Boolean
        get() = false

    override fun execute(ctx: CommandContext) {
        val guild = ctx.guild

        val voiceState = ctx.member!!.voiceState!!

        if(!voiceState.inVoiceChannel()){
            ctx.channel.sendMessage("You need to be in a voice channel to use this command").queue()
            return
        }

        val channel = voiceState.channel!!
        val manager = guild.audioManager

        val player = playerManager.createPlayer()

        val trackScheduler = TrackScheduler()
        player.addListener(trackScheduler)

        manager.sendingHandler = AudioPlayerSendHandler(player)
        manager.openAudioConnection(channel)

        playerManager.loadItem(ctx.args[0],  object: AudioLoadResultHandler {

            override fun trackLoaded(track: AudioTrack) {
                trackScheduler.queue.add(track)
                if(player.playingTrack==null){
                    player.playTrack(trackScheduler.queue[0])
                }
            }

            override fun playlistLoaded(playlist: AudioPlaylist) {
                for (track in playlist.tracks) {
                    trackScheduler.queue.add(track)
                }
                if(player.playingTrack==null){
                    player.playTrack(trackScheduler.queue[0])
                }
            }

            override fun noMatches() {
                // Notify the user that we've got nothing
            }

            override fun loadFailed(throwable: FriendlyException) {
                // Notify the user that everything exploded
            }
        })
    }

    override fun getName(): String {
        return "play"
    }

    override fun getFullName(): String {
        return "music play"
    }

    override fun getDescription(): String {
        return ""
    }

    override fun getInstruction(): String {
        return ""
    }
}