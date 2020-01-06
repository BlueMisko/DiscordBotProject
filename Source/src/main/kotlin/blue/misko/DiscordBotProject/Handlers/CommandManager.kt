package blue.misko.DiscordBotProject.Handlers

import blue.misko.DiscordBotProject.Objects.CommandContext
import blue.misko.DiscordBotProject.Commands.*
import blue.misko.DiscordBotProject.Commands.Birthday.BirthdayCommand
import blue.misko.DiscordBotProject.Commands.Music.MusicCommand
import blue.misko.DiscordBotProject.Commands.Prefix.PrefixCommand
import blue.misko.DiscordBotProject.Interface.ICommand
import blue.misko.DiscordBotProject.getPrefix
import java.util.*
import net.dv8tion.jda.api.events.message.guild.*
import java.util.regex.Pattern


class CommandManager {
    private val commands = ArrayList<ICommand>()

    init {
        addCommand(PfpCommand())
        addCommand(HelpCommand(this))
        addCommand(PrefixCommand())
        addCommand(PingCommand())
        addCommand(BirthdayCommand())
        addCommand(MusicCommand())
    }

    val getCommands: ArrayList<ICommand>
        get() = commands

    private fun addCommand(cmd: ICommand) {
        val nameFound = this.commands.stream().anyMatch { it.getName() == cmd.getName() }

        if (nameFound) {
            throw IllegalArgumentException("A command with this name is already present")
        }

        commands.add(cmd)
    }

    fun getCommand(search: String): ICommand? {
        val searchLower = search.toLowerCase()

        for (cmd in this.commands) {
            if (cmd.getName() == searchLower || cmd.getAliases().contains(searchLower)) {
                return cmd
            }
        }

        return null
    }

    fun handle(event: GuildMessageReceivedEvent) {
        var split = event.message.contentRaw
            .replaceFirst(("(?i)" + Pattern.quote(getPrefix(event.guild.id))).toRegex(), "")
            .split("\\s+".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()

        if(split[0]==""){
            split= split.drop(1).toTypedArray()
        }

        val invoke = split[0].toLowerCase()
        val cmd = this.getCommand(invoke)

        if (cmd != null) {
            event.channel.sendTyping().queue()
            val args = listOf(*split).subList(1, split.size)

            val ctx = CommandContext(event, args)

            cmd.handle(ctx)
        }
    }
}