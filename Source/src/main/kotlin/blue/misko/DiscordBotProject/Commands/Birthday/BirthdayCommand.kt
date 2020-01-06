package blue.misko.DiscordBotProject.Commands.Birthday

import blue.misko.DiscordBotProject.Interface.ICommand
import blue.misko.DiscordBotProject.Objects.CommandContext
import blue.misko.DiscordBotProject.getPrefix
import net.dv8tion.jda.api.Permission
import java.util.ArrayList

class BirthdayCommand: ICommand {
    override val subCommands: ArrayList<ICommand>?
    init {
        subCommands = ArrayList<ICommand>()
        subCommands.add(BirthdayAllCommand())
        subCommands.add(BirthdayAddCommand())
        subCommands.add(BirthdayRemoveCommand())
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
        return "birthday"
    }

    override fun getFullName(): String {
        return getName()
    }

    override fun getDescription(): String {
        return ""
    }

    override fun getInstruction(): String {
        return ""
    }
}