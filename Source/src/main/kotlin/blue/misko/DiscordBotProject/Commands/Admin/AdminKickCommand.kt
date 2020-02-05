package blue.misko.DiscordBotProject.Commands.Admin

import blue.misko.DiscordBotProject.Interface.ICommand
import blue.misko.DiscordBotProject.Objects.CommandContext
import net.dv8tion.jda.api.Permission
import java.util.ArrayList

class AdminKickCommand: ICommand {
    override val subCommands: ArrayList<ICommand>?
        get() = null
    override val neededPermissions: ArrayList<Permission>?
    init{
        neededPermissions = ArrayList<Permission>()
        neededPermissions.add(Permission.KICK_MEMBERS)
    }
    override val neededRank: Int
        get() = 0
    override val hiddenCommand: Boolean
        get() = false

    override fun execute(ctx: CommandContext) {
        val mentionedMembers =ctx.event.message.mentionedMembers
        val user = if(mentionedMembers.isEmpty()) null else mentionedMembers.last()
        if(user==null){
            ctx.channel.sendMessage("You need to mention a user in this server").queue()
            return
        }
        ctx.guild.kick(user).queue()
        ctx.channel.sendMessage(user.effectiveName + " was kicked").queue()
    }

    override fun getName(): String {
        return "kick"
    }

    override fun getFullName(): String {
        return "admin kick"
    }

    override fun getDescription(): String {
        return "kick the mentioned member"
    }

    override fun getInstruction(): String {
        return "admin kick <member>"
    }
}