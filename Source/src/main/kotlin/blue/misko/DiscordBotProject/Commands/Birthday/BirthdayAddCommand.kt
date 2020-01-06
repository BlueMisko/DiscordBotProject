package blue.misko.DiscordBotProject.Commands.Birthday

import blue.misko.DiscordBotProject.Interface.ICommand
import blue.misko.DiscordBotProject.Objects.CommandContext
import blue.misko.DiscordBotProject.getAbsolutePath
import blue.misko.DiscordBotProject.isDataValid
import net.dv8tion.jda.api.Permission
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.TransactionManager
import org.jetbrains.exposed.sql.transactions.transaction
import java.sql.Connection
import java.util.ArrayList

class BirthdayAddCommand: ICommand {
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
        if(knownBirthday(ctx.event, ctx.author.id)){
            ctx.channel.sendMessage("Hey! I already know your birthday!").queue()
            return
        }
        val date = ctx.args[0].split("/")
        when {
            date.size==3 -> {
                if(!isDataValid(date[0].toInt(), date[1].toInt(), date[2].toInt())){
                    ctx.channel.sendMessage("This is not a valid date").queue()
                    return
                }
                val database = Database.connect("jdbc:sqlite:"+ getAbsolutePath("Servers/${ctx.guild.id}") +"/birthdays.sqlite","org.sqlite.JDBC")
                TransactionManager.manager.defaultIsolationLevel = Connection.TRANSACTION_SERIALIZABLE
                transaction (database) {
                    Birthdays.insert {
                        it[playerId] = ctx.author.id
                        it[day] = date[0].toInt()
                        it[month] = date[1].toInt()
                        it[year] = date[2].toInt()
                    }
                }
            }
            date.size==2 -> {
                if(!isDataValid(date[0].toInt(), date[1].toInt())){
                    ctx.channel.sendMessage("This is not a valid date").queue()
                    return
                }
                val database = Database.connect("jdbc:sqlite:"+ getAbsolutePath("Servers/${ctx.guild.id}") +"/birthdays.sqlite","org.sqlite.JDBC")
                TransactionManager.manager.defaultIsolationLevel = Connection.TRANSACTION_SERIALIZABLE
                transaction (database) {
                    Birthdays.insert {
                        it[playerId] = ctx.author.id
                        it[day] = date[0].toInt()
                        it[month] = date[1].toInt()
                    }
                }
            }
            else -> {
                ctx.channel.sendMessage("This is not a valid date").queue()
                return
            }
        }
        ctx.channel.sendMessage("Got it").queue()

    }

    override fun getName(): String {
        return "add"
    }

    override fun getFullName(): String {
        return "birthday add"
    }

    override fun getDescription(): String {
        return "Add your birthday to the list of everyone's birthdays"
    }

    override fun getInstruction(): String {
        return "birthday add <date>\n" +
                "dd/mm/yyyy or just dd/mm"
    }
}