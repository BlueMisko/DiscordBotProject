package blue.misko.DiscordBotProject.Commands.Music

import blue.misko.DiscordBotProject.Interface.ICommand
import blue.misko.DiscordBotProject.Objects.CommandContext
import net.dv8tion.jda.api.Permission
import java.util.ArrayList

class MusicStopCommand: ICommand {
    override val subCommands: ArrayList<ICommand>?
        get() = null
    override val neededPermissions: ArrayList<Permission>?
        get() = null
    override val neededRank: Int
        get() = 0
    override val hiddenCommand: Boolean
        get() = false

    override fun execute(ctx: CommandContext) {


    }

    override fun getName(): String {
        return "stop"
    }

    override fun getFullName(): String {
        return "music stop"
    }

    override fun getDescription(): String {
        return "Stops the music if it's playing"
    }

    override fun getInstruction(): String {
        return "music stop"
    }
}