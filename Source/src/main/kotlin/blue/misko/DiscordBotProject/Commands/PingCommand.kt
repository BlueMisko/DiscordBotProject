package blue.misko.DiscordBotProject.Commands

import blue.misko.DiscordBotProject.Interface.ICommand
import blue.misko.DiscordBotProject.Objects.CommandContext
import net.dv8tion.jda.api.Permission
import java.util.ArrayList

class PingCommand: ICommand {
    override val subCommands: ArrayList<ICommand>?
        get() = null
    override val neededPermissions: ArrayList<Permission>?
        get() = null
    override val neededRank: Int
        get() = 0
    override val hiddenCommand: Boolean
        get() = true

    override fun execute(ctx: CommandContext) {
        ctx.channel.sendMessage("pong").queue()
    }

    override fun getName(): String {
        return "ping"
    }

    override fun getFullName(): String {
        return "ping"
    }

    override fun getDescription(): String {
        return "pong"
    }

    override fun getInstruction(): String {
        return "ping"
    }
}