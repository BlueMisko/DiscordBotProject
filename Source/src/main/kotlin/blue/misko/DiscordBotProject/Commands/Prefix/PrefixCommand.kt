package blue.misko.DiscordBotProject.Commands.Prefix

import blue.misko.DiscordBotProject.Objects.CommandContext
import blue.misko.DiscordBotProject.Interface.ICommand
import blue.misko.DiscordBotProject.getPrefix
import net.dv8tion.jda.api.Permission
import java.util.ArrayList

class PrefixCommand: ICommand {

    override val subCommands: ArrayList<ICommand>?
    init {
        subCommands = ArrayList()
        subCommands.add(PrefixSetCommand())
    }
    override val neededPermissions: ArrayList<Permission>?
        init{
            neededPermissions = ArrayList<Permission>()
            neededPermissions.add(Permission.MANAGE_SERVER)
        }
    override val neededRank: Int
        get() = 0
    override val hiddenCommand: Boolean
        get() = false

    override fun execute(ctx: CommandContext) {
        var success= handleSubCommand(ctx)
        if (!success){
            ctx.channel.sendMessage("I don't know which command you are talking about. Try using "+ getPrefix(ctx.guild.id)+"help "+ getFullName())
        }
    }

    override fun getName(): String {
        return "prefix"
    }

    override fun getFullName(): String {
        return getName()
    }

    override fun getDescription(): String {
        return "Used to set and check the prefix for this server"
    }

    override fun getInstruction(): String {
        return ""
    }
}