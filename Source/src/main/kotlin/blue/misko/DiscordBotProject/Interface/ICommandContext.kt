package blue.misko.DiscordBotProject.Interface

import net.dv8tion.jda.api.sharding.ShardManager
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.entities.*
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent


interface ICommandContext {

    val guild: Guild

    val event: GuildMessageReceivedEvent

    val channel: TextChannel
        get() = this.event.channel

    val message: Message
        get() = this.event.message

    val author: User
        get() = this.event.author

    val member: Member?
        get() = this.event.member

    val jda: JDA
        get() = this.event.jda

    val shardManager: ShardManager?
        get() = this.jda.shardManager

    val selfUser: User
        get() = this.jda.selfUser

    val selfMember: Member
        get() = this.guild.getSelfMember()

}