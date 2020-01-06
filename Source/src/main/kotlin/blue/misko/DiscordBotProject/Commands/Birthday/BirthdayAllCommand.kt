package blue.misko.DiscordBotProject.Commands.Birthday


import blue.misko.DiscordBotProject.Interface.ICommand
import blue.misko.DiscordBotProject.Objects.CommandContext
import blue.misko.DiscordBotProject.getAbsolutePath
import net.dv8tion.jda.api.Permission
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.TransactionManager
import org.jetbrains.exposed.sql.transactions.transaction
import java.sql.Connection
import java.util.ArrayList

class BirthdayAllCommand:ICommand {
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
        val database = Database.connect("jdbc:sqlite:"+ getAbsolutePath("Servers/${ctx.guild.id}") +"/birthdays.sqlite","org.sqlite.JDBC")
        TransactionManager.manager.defaultIsolationLevel = Connection.TRANSACTION_SERIALIZABLE
        var data ="```"
        transaction (database) {
            Birthdays.selectAll().orderBy(Birthdays.month).orderBy(
                Birthdays.day
            ).forEach{
                val user = ctx.jda.getUserById(it[Birthdays.playerId])
                data += (user?.name ?: "A lost soul").padEnd(33)
                data += it[Birthdays.day].toString().padStart(2)
                data += "/"
                data += it[Birthdays.month].toString().padStart(2)
                if(it[Birthdays.year] != null){
                    data+="/"
                    data += it[Birthdays.year]
                }
                data+="\n"
            }
        }
        if(data=="```"){
            data="I know no one's birthday "
        }
        else
            data +="```"
        ctx.channel.sendMessage(data).queue()
    }

    override fun getName(): String {
        return "all"
    }

    override fun getFullName(): String {
        return "birthday all"
    }

    override fun getDescription(): String {
        return "Shows all known birthdays"
    }

    override fun getInstruction(): String {
        return "birthday all"
    }

}