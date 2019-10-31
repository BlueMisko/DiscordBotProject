package blue.misko.DiscordBotProject

import net.dv8tion.jda.api.entities.User
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction


object Birthdays: Table(){
    val playerId = varchar("playerID", 20).primaryKey()
    val day = integer("day")
    val month = integer("month")
    val year = integer("year").nullable()
}

fun executeBirthdaysCommands (event: MessageReceivedEvent, parameters: String){

    when (true){
        //parameters.startsWith("remember")->executeBirthdaysRememberCommand(event, parameters.removePrefix("remember").trim())
        //parameters.startsWith("forget")->executeBirthdaysForgetCommand(event, parameters.removePrefix("forget").trim())
        parameters.startsWith("all")->executeBirthdaysAllCommand(event, parameters.removePrefix("all").trim())
        //parameters.startsWith("help")->executeBirthdaysHelpCommand(event, parameters.removePrefix("help").trim())
        //parameters.startsWith("<@")->executeBirthdaysRequestCommand(event, parameters.removePrefix("remember").trim())
        //parameters.startsWith("")->{}

        else -> {

        }
    }
    event.channel.sendMessage("Birthday commands are not yet implemented").queue()
}

fun executeBirthdaysAllCommand (event: MessageReceivedEvent, parameters: String){
    /*try{
        val database = Database.connect("jdbc:sqlite:"+GetAbsolutePath("Servers/${event.guild.id}")+"/birthdays.sqlite","org.sqlite.JDBC")
        var data ="```"
        transaction (database) {
            Birthdays.selectAll().forEach{
                val user = event.jda.getUserById(it[Birthdays.playerId])
                data += if(user != null)
                    user.name.padEnd(33)
                else
                    "A lost soul"
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
        data +="```"
        event.channel.sendMessage(data).queue()
    }
    catch (e:Exception){
         event.channel.sendMessage(e.toString()).queue()
    }*/
}