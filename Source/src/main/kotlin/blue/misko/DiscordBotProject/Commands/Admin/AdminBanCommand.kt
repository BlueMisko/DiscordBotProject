package blue.misko.DiscordBotProject.Commands.Admin

import blue.misko.DiscordBotProject.Interface.ICommand
import blue.misko.DiscordBotProject.Objects.CommandContext
import net.dv8tion.jda.api.Permission
import java.util.ArrayList

class AdminBanCommand: ICommand {
    override val subCommands: ArrayList<ICommand>?
        get() = null
    override val neededPermissions: ArrayList<Permission>?
    init{
        neededPermissions = ArrayList<Permission>()
        neededPermissions.add(Permission.BAN_MEMBERS)
    }
    override val neededRank: Int
        get() = 0
    override val hiddenCommand: Boolean
        get() = false

    override fun execute(ctx: CommandContext) {
        val user = ctx.event.message.mentionedMembers.last()
        user.ban(1)
        ctx.channel.sendMessage(user.effectiveName + "was banned for a day").queue()
    }

    override fun getName(): String {
        return "ban"
    }

    override fun getFullName(): String {
        return "admin ban"
    }

    override fun getDescription(): String {
        return "ban the mentioned member for a day"
    }

    override fun getInstruction(): String {
        return "admin ban <member>"
    }
}