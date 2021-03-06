package blue.misko.DiscordBotProject.Commands.Birthday

import blue.misko.DiscordBotProject.*
import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.*
import java.io.File
import java.sql.Connection
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.ArrayList


object Birthdays: Table(){
    val playerId = varchar("playerID", 20).primaryKey()
    val day = integer("day")
    val month = integer("month")
    val year = integer("year").nullable()
}

fun executeBirthdaysRequestCommand (event: GuildMessageReceivedEvent, parameters: String){
    val userID = parameters.removeSuffix(">")
    val toLong = userID.toLongOrNull()
    if(toLong==null){
        event.channel.sendMessage("That's not a valid user").queue()
        return
    }
    enforceDatabaseExistence(event.guild.id)
    if(!knownBirthday(event, event.author.id)){
        event.channel.sendMessage("I don't know their birthday!").queue()
        return
    }
    val database = Database.connect("jdbc:sqlite:"+ getAbsolutePath("Servers/${event.guild.id}") +"/birthdays.sqlite","org.sqlite.JDBC")
    TransactionManager.manager.defaultIsolationLevel = Connection.TRANSACTION_SERIALIZABLE
    var data =""
    transaction (database) {
        Birthdays.select { Birthdays.playerId eq userID}.forEach{
            val user = event.jda.getUserById(it[Birthdays.playerId])
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
    if(data==""){
        data="I don't know their birthday"
    }
    event.channel.sendMessage(data).queue()
}

fun enforceDatabaseExistence(serverID: String){
    val filename = getAbsolutePath("Servers/${serverID}") +"/birthdays.sqlite"
    if(!File(filename).exists()){
        val database = Database.connect("jdbc:sqlite:$filename","org.sqlite.JDBC")
        TransactionManager.manager.defaultIsolationLevel = Connection.TRANSACTION_SERIALIZABLE
        transaction (database) {
            SchemaUtils.create(Birthdays)
        }
    }
}

fun knownBirthday(event: GuildMessageReceivedEvent, userID: String) :Boolean{
    val database = Database.connect("jdbc:sqlite:"+ getAbsolutePath("Servers/${event.guild.id}") +"/birthdays.sqlite","org.sqlite.JDBC")
    TransactionManager.manager.defaultIsolationLevel = Connection.TRANSACTION_SERIALIZABLE
    val user = transaction { Birthdays.select{ Birthdays.playerId eq userID}.toList() }
    return user.isNotEmpty()
}

fun executeBirthdaysSetChannelCommand(event: GuildMessageReceivedEvent, parameters: String){

    if(!hasPermission(event, Permission.MANAGE_SERVER)){
        event.channel.sendMessage("You don't have the permission to do that").queue()
        return
    }

    val channelID = parameters.removePrefix("<#").removeSuffix(">")
    var result = false
    try{
        if(channelID!="") {
            val channel = event.guild.getTextChannelById(channelID)
            result = writeToFile(
                getAbsolutePath("Servers/" + event.guild.id) + "/BirthdayChannel.txt",
                channel?.id!!
            )
        }
        else
            result = writeToFile(
                getAbsolutePath("Servers/" + event.guild.id) + "/BirthdayChannel.txt",
                ""
            )
    }
    catch (e: Exception){

    }
    if(result){
        if(channelID!="")
            event.channel.sendMessage("Channel set").queue()
        else
            event.channel.sendMessage("Channel cleared").queue()
    }
    else{
        event.channel.sendMessage("Failed to set a channel").queue()
    }
}

fun getBirthdaysToday(serverID: String): String{
    enforceDatabaseExistence(serverID)
    val database = Database.connect("jdbc:sqlite:"+ getAbsolutePath("Servers/${serverID}") +"/birthdays.sqlite","org.sqlite.JDBC")
    TransactionManager.manager.defaultIsolationLevel = Connection.TRANSACTION_SERIALIZABLE
    var users = ArrayList<String>()

    val currentDate = LocalDateTime.now().format(DateTimeFormatter.BASIC_ISO_DATE)!!
    val day: Int = currentDate.substring(6,8).toInt()
    val month: Int =currentDate.substring(4,6).toInt()
    transaction (database) {
        Birthdays.select { (Birthdays.day eq day) and (Birthdays.month eq month)}.toList().forEach{
            users.add(it[Birthdays.playerId])
        }
    }
    var text = ""
    users.forEachIndexed { index, user ->
        text += "<@$user>"
        when(users.size + index){
            1->{}
            2-> text += " and "
            else -> ", "
        }
    }
    return text
}

fun happyBirthdayWishing(){
    File(getAbsolutePath("Servers")).listFiles().forEach {
        val channelID =
            readFromFile(getAbsolutePath("Servers/" + it.name) + "/BirthdayChannel.txt")
        val channel = if (Bot.getTextChannelById(channelID) != null) Bot.getTextChannelById(channelID)!! else return@forEach
        val text = getBirthdaysToday(it.name)
        if (text != "") channel.sendMessage("Happy birthday to $text").queue()
    }
}