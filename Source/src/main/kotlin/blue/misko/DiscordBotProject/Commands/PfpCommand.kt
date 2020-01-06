package blue.misko.DiscordBotProject.Commands

import blue.misko.DiscordBotProject.Objects.CommandContext
import blue.misko.DiscordBotProject.Interface.ICommand
import net.dv8tion.jda.api.Permission
import java.util.ArrayList

class PfpCommand: ICommand {
    override val subCommands: ArrayList<ICommand>?
        get() = null

    override val neededPermissions: ArrayList<Permission>?
        get() = null
    override val neededRank: Int
        get() = 0
    override val hiddenCommand: Boolean
        get() = false


    override fun execute(ctx: CommandContext) {

        var user = try{
            ctx.jda.getUserById(ctx.args[0].removePrefix("<@").removeSuffix(">"))!!
        } catch(e: Exception){
            ctx.author
        }

        if(user != null){
            ctx.channel.sendMessage(user.avatarUrl!!).queue()
        }
        else{
            ctx.channel.sendMessage(user.defaultAvatarUrl).queue()
        }
    }

    override fun getName(): String {
        return "pfp"
    }

    override fun getFullName(): String {
        return getName()
    }

    override fun getDescription(): String {
        return "Returns the users profile picture"
    }

    override fun getInstruction(): String {
        return "pfp [@user]"
    }
}
