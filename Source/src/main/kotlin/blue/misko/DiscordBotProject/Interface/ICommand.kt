package blue.misko.DiscordBotProject.Interface

import blue.misko.DiscordBotProject.Objects.CommandContext
import blue.misko.DiscordBotProject.getPrefix
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.Permission
import java.awt.Color
import java.util.ArrayList

interface ICommand {
    val subCommands: ArrayList<ICommand>?

    val neededPermissions: ArrayList<Permission>?
    val neededRank: Int
    val hiddenCommand: Boolean


    fun handle(ctx: CommandContext){
        var valid = true

        if(neededPermissions!=null){
            var hasPermission = true
            neededPermissions!!.forEach {
                val hasThatPermission = ctx.member?.permissions?.contains(it)
                if( hasThatPermission==false)
                    hasPermission = false
            }
            valid = hasPermission
        }

        if(!valid){
            ctx.channel.sendMessage("You don't have the permission to use this command")
            return
        }

        this.execute(ctx)
    }

    fun execute(ctx: CommandContext)

    fun handleSubCommand(ctx: CommandContext): Boolean{
        var foundCommand = false
        subCommands!!.forEach {
            if(it.getName()==ctx.args[0]){
                it.handle(CommandContext(ctx.event, ctx.args.drop(1)))
                foundCommand = true
            }
        }

        return foundCommand
    }

    fun getName(): String

    fun getFullName(): String

    fun getHelp(args: List<String>, ctx: CommandContext){

        if(args.isNotEmpty() && getSubCommand(args[0]) != null){

            val command = getSubCommand(args[0])!!
            command.getHelp(args.drop(1), ctx)

        }
        else{

            var eb = EmbedBuilder()
            eb.setTitle(getFullName())
            eb.setColor(Color.CYAN)
            eb.setDescription(getDescription() + if(getInstruction()!="") {"\n" + getPrefix(ctx.guild.id) + getInstruction()} else "")

            if(subCommands!=null){

                var canShow =true
                if(neededPermissions!=null){
                    var hasPermission = true
                    neededPermissions!!.forEach {
                        val hasThatPermission = ctx.member?.permissions?.contains(it)
                        if( hasThatPermission==false)
                            hasPermission = false
                    }
                    canShow = canShow && hasPermission
                }

                var subCommandNames=""
                subCommands!!.forEach { if(canShow) subCommandNames+= it.getName()+"\n" }
                eb.addField("Subcommands:", subCommandNames, false)

            }

            ctx.channel.sendMessage(eb.build()).queue()
        }
    }

    fun getDescription(): String

    fun getInstruction(): String

    fun getAliases(): List<String> {
        return listOf<String>()
    }

    fun getSubCommand(search: String): ICommand? {

        val searchLower = search.toLowerCase()

        if(subCommands!=null)
            for (cmd in this.subCommands!!) {
                if (cmd.getName() == searchLower || cmd.getAliases().contains(searchLower)) {
                    return cmd
                }
            }

        return null
    }
}