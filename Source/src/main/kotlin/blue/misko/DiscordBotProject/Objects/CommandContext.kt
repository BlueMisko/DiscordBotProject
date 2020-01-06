package blue.misko.DiscordBotProject.Objects

import blue.misko.DiscordBotProject.Interface.*
import net.dv8tion.jda.api.entities.*
import net.dv8tion.jda.api.events.message.guild.*


class CommandContext(private val eventReceived: GuildMessageReceivedEvent, val args: List<String>): ICommandContext{


    override val guild: Guild
        get() = this.event.guild
    override val event: GuildMessageReceivedEvent
        get() = this.eventReceived


}