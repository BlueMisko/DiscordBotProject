package blue.misko.DiscordBotProject.Commands.Debug

import blue.misko.DiscordBotProject.Interface.ICommand
import blue.misko.DiscordBotProject.Objects.CommandContext
import net.dv8tion.jda.api.Permission
import java.time.LocalDateTime
import java.util.ArrayList

class DebugTimeCommand: ICommand {
    override val subCommands: ArrayList<ICommand>?
        get() = null
    override val neededPermissions: ArrayList<Permission>?
        get() = null
    override val neededRank: Int
        get() = 0
    override val hiddenCommand: Boolean
        get() = false

    override fun execute(ctx: CommandContext) {
        ctx.channel.sendMessage(LocalDateTime.now().toString()).queue()
    }

    override fun getName(): String {
        return "time"
    }

    override fun getFullName(): String {
        return "debug time"
    }

    override fun getDescription(): String {
        return "get the time specifics of where the bot is located"
    }

    override fun getInstruction(): String {
        return "debug time"
    }
}