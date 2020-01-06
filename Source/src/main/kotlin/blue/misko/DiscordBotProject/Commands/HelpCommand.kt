package blue.misko.DiscordBotProject.Commands

import blue.misko.DiscordBotProject.Objects.CommandContext
import blue.misko.DiscordBotProject.Handlers.CommandManager
import blue.misko.DiscordBotProject.Interface.ICommand
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.Permission
import java.awt.Color
import java.util.ArrayList

class HelpCommand(private val manager: CommandManager): ICommand {
    override val subCommands: ArrayList<ICommand>?
        get() = null
    override val neededPermissions: ArrayList<Permission>?
        get() = null
    override val neededRank: Int
        get() = 0
    override val hiddenCommand: Boolean
        get() = false


    override fun execute(ctx: CommandContext) {
        if (ctx.args.isEmpty()){

            var eb = EmbedBuilder()
            eb.setTitle("List of commands")
            eb.setColor(Color.CYAN)

            var commandNames=""
            manager.getCommands.forEach { if (!it.hiddenCommand) commandNames+= it.getName()+"\n" }
            eb.setDescription(commandNames)

            ctx.channel.sendMessage(eb.build()).queue()
        }
        else{
            val command = manager.getCommand(ctx.args[0])
            var temp = ctx.args.drop(1)
            command?.getHelp(temp, ctx)
        }
    }

    override fun getName(): String {
        return "help"
    }

    override fun getFullName(): String {
        return getName()
    }

    override fun getDescription(): String {
        return "Literally this"
    }

    override fun getInstruction(): String {
        return "help [command] [subcommand]"
    }
}