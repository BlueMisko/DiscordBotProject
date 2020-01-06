package blue.misko.DiscordBotProject.Commands.Prefix

import blue.misko.DiscordBotProject.Objects.CommandContext
import blue.misko.DiscordBotProject.Interface.ICommand
import blue.misko.DiscordBotProject.setPrefix
import net.dv8tion.jda.api.Permission
import java.util.ArrayList

class PrefixSetCommand: ICommand {
    override val subCommands: ArrayList<ICommand>?
        get() = null
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

        val prefix = ctx.args[0]
        if(setPrefix(ctx.guild.id, prefix))
            ctx.channel.sendMessage("Prefix was set to $prefix").queue()
        else
            ctx.channel.sendMessage("Failed to set a prefix").queue()
    }

    override fun getName(): String {
        return "set"
    }

    override fun getFullName(): String {
        return "prefix set"
    }

    override fun getDescription(): String {
        return "Set a new prefix for the server."
    }

    override fun getInstruction(): String {
        return "prefix set <new prefix>"
    }
}