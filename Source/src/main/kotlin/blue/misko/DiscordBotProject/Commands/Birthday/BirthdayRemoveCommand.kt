package blue.misko.DiscordBotProject.Commands.Birthday

import blue.misko.DiscordBotProject.Interface.ICommand
import blue.misko.DiscordBotProject.Objects.CommandContext
import blue.misko.DiscordBotProject.getAbsolutePath
import net.dv8tion.jda.api.Permission
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.transactions.TransactionManager
import org.jetbrains.exposed.sql.transactions.transaction
import java.sql.Connection
import java.util.ArrayList

class BirthdayRemoveCommand: ICommand {
    override val subCommands: ArrayList<ICommand>?
        get() = null
    override val neededPermissions: ArrayList<Permission>?
        get() = null
    override val neededRank: Int
        get() = 0
    override val hiddenCommand: Boolean
        get() = false

    override fun execute(ctx: CommandContext) {
        enforceDatabaseExistence(ctx.guild.id)
        if(!knownBirthday(ctx.event, ctx.author.id)){
            ctx.channel.sendMessage("I don't know your birthday!").queue()
            return
        }
        val database = Database.connect("jdbc:sqlite:"+ getAbsolutePath("Servers/${ctx.guild.id}") +"/birthdays.sqlite","org.sqlite.JDBC")
        TransactionManager.manager.defaultIsolationLevel = Connection.TRANSACTION_SERIALIZABLE
        transaction (database) {
            Birthdays.deleteWhere {
                Birthdays.playerId eq ctx.author.id
            }
        }
        ctx.channel.sendMessage("Done").queue()
    }

    override fun getName(): String {
        return "remove"
    }

    override fun getFullName(): String {
        return "birthday remove"
    }

    override fun getDescription(): String {
        return "Remove your birthday from the list"
    }

    override fun getInstruction(): String {
        return "birthday remove"
    }
}