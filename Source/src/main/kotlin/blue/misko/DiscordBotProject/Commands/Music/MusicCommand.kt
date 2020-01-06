package blue.misko.DiscordBotProject.Commands.Music

import blue.misko.DiscordBotProject.Commands.Birthday.BirthdayAddCommand
import blue.misko.DiscordBotProject.Commands.Birthday.BirthdayAllCommand
import blue.misko.DiscordBotProject.Commands.Birthday.BirthdayRemoveCommand
import blue.misko.DiscordBotProject.Interface.ICommand
import blue.misko.DiscordBotProject.Objects.CommandContext
import blue.misko.DiscordBotProject.getPrefix
import net.dv8tion.jda.api.Permission
import java.util.ArrayList

class MusicCommand: ICommand {
    override val subCommands: ArrayList<ICommand>?
    init {
        subCommands = ArrayList<ICommand>()
        subCommands.add(MusicPlayCommand())
    }
    override val neededPermissions: ArrayList<Permission>?
        get() = null
    override val neededRank: Int
        get() = 0
    override val hiddenCommand: Boolean
        get() = false

    override fun execute(ctx: CommandContext) {
        var success= handleSubCommand(ctx)
        if (!success){
            ctx.channel.sendMessage("I don't know which command you are talking about. Try using "+ getPrefix(ctx.guild.id) +"help "+ getFullName())
        }
    }

    override fun getName(): String {
        return "music"
    }

    override fun getFullName(): String {
        return "music"
    }

    override fun getDescription(): String {
        return ""
    }

    override fun getInstruction(): String {
        return ""
    }
}