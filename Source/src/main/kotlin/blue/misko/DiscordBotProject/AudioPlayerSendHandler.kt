package blue.misko.DiscordBotProject

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer
import com.sedmelluq.discord.lavaplayer.track.playback.AudioFrame
import net.dv8tion.jda.api.audio.AudioSendHandler
import java.nio.ByteBuffer
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter
import com.sedmelluq.discord.lavaplayer.track.AudioTrack


class AudioPlayerSendHandler(private val audioPlayer: AudioPlayer) : AudioSendHandler {
    private var lastFrame: AudioFrame? = null

    override fun canProvide(): Boolean {
        lastFrame = audioPlayer.provide()
        return lastFrame != null
    }

    override fun provide20MsAudio(): ByteBuffer? {
        return ByteBuffer.wrap(lastFrame!!.data)
    }

    override fun isOpus(): Boolean {
        return true
    }
}

class TrackScheduler : AudioEventAdapter() {
    override fun onPlayerPause(player: AudioPlayer?) {
        // Player was paused
    }

    override fun onPlayerResume(player: AudioPlayer?) {
        // Player was resumed
    }

    override fun onTrackStart(player: AudioPlayer, track: AudioTrack) {
        // A track started playing
    }

        override fun onTrackEnd(player: AudioPlayer, track: AudioTrack, endReason: AudioTrackEndReason) {
            if (endReason.mayStartNext) {
                // Start next track
        }

        // endReason == FINISHED: A track finished or died by an exception (mayStartNext = true).
        // endReason == LOAD_FAILED: Loading of a track failed (mayStartNext = true).
        // endReason == STOPPED: The player was stopped.
        // endReason == REPLACED: Another track started playing while this had not finished
        // endReason == CLEANUP: Player hasn't been queried for a while, if you want you can put a
        //                       clone of this back to your queue
    }

    override fun onTrackException(player: AudioPlayer, track: AudioTrack, exception: FriendlyException) {
        // An already playing track threw an exception (track end event will still be received separately)
    }

    override fun onTrackStuck(player: AudioPlayer, track: AudioTrack, thresholdMs: Long) {
        // Audio track has been unable to provide us any audio, might want to just start a new track
    }
}