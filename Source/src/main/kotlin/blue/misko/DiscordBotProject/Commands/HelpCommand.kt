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
            manager.getCommands.forEach {

                var canShow = !it.hiddenCommand
                if(it.neededPermissions!=null){

                    var hasPermission = true
                    it.neededPermissions!!.forEach { permission ->

                        val hasThatPermission = ctx.member?.permissions?.contains(permission)
                        if( hasThatPermission==false)
                            hasPermission = false

                    }
                    canShow = canShow && hasPermission
                }
                if (canShow)
                    commandNames+= it.getName()+"\n"
            }
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
        return "It shows all commands available to you or if given a command it will show its description like this"
    }

    override fun getInstruction(): String {
        return "help [command] [subcommand]"
    }
}