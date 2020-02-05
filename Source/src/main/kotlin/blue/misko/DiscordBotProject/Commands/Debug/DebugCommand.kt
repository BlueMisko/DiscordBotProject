package blue.misko.DiscordBotProject.Commands.Debug

import blue.misko.DiscordBotProject.Interface.ICommand
import blue.misko.DiscordBotProject.Objects.CommandContext
import blue.misko.DiscordBotProject.getPrefix
import net.dv8tion.jda.api.Permission
import java.util.ArrayList

class DebugCommand: ICommand {
    override val subCommands: ArrayList<ICommand>?
    init {
        subCommands = ArrayList<ICommand>()
        addSubCommand(DebugTimeCommand())
    }
    override val neededPermissions: ArrayList<Permission>?
        get() = null
    override val neededRank: Int
        get() = 0
    override val hiddenCommand: Boolean
        get() = true

    override fun execute(ctx: CommandContext) {
        var success= handleSubCommand(ctx)
        if (!success){
            ctx.channel.sendMessage("I don't know which command you are talking about. Try using "+ getPrefix(ctx.guild.id) +"help "+ getFullName())
        }
    }

    override fun getName(): String {
        return "debug"
    }

    override fun getFullName(): String {
        return "debug"
    }

    override fun getDescription(): String {
        return "The commands to help developers check bot data"
    }

    override fun getInstruction(): String {
        return ""
    }
}