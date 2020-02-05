package blue.misko.DiscordBotProject.Commands.Admin

import blue.misko.DiscordBotProject.Interface.ICommand
import blue.misko.DiscordBotProject.Objects.CommandContext
import net.dv8tion.jda.api.Permission
import java.util.ArrayList

class AdminPurgeCommand: ICommand {
    override val subCommands: ArrayList<ICommand>?
        get() = null
    override val neededPermissions: ArrayList<Permission>?
    init{
        neededPermissions = ArrayList<Permission>()
        neededPermissions.add(Permission.MESSAGE_MANAGE)
    }
    override val neededRank: Int
        get() = 0
    override val hiddenCommand: Boolean
        get() = false

    override fun execute(ctx: CommandContext) {
        if(ctx.args.isEmpty()){
            ctx.channel.sendMessage("You need to give an amount of messages you want to be deleted").queue()
            return
        }
        if(ctx.args[0].toIntOrNull()==null){
            ctx.channel.sendMessage("You need to send a number").queue()
            return
        }
        val amount =ctx.args[0].toInt()

        val messages = ctx.channel.history.retrievePast(amount + 2).complete()

        ctx.channel.deleteMessages(messages).queue()
    }

    override fun getName(): String {
        return "purge"
    }

    override fun getFullName(): String {
        return "admin purge"
    }

    override fun getDescription(): String {
        return "Deletes the given amount of messages"
    }

    override fun getInstruction(): String {
        return "admin purge <amount of messages>"
    }
}