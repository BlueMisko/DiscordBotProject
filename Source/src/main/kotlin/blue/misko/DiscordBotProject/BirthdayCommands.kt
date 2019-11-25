package blue.misko.DiscordBotProject

import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.entities.TextChannel
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.*
import java.io.File
import java.sql.Connection
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import java.util.TimerTask


object Birthdays: Table(){
    val playerId = varchar("playerID", 20).primaryKey()
    val day = integer("day")
    val month = integer("month")
    val year = integer("year").nullable()
}

fun executeBirthdaysCommands (event: MessageReceivedEvent, parameters: String){
    enforceDatabaseExistence(event.guild.id)
    when (true){
        parameters.startsWith("remember")->executeBirthdaysRememberCommand(event, parameters.removePrefix("remember").trim())
        parameters.startsWith("forget")->executeBirthdaysForgetCommand(event, parameters.removePrefix("forget").trim())
        parameters.startsWith("all")->executeBirthdaysAllCommand(event, parameters.removePrefix("all").trim())
        parameters.startsWith("help")->executeBirthdaysHelpCommand(event, parameters.removePrefix("help").trim())
        parameters.startsWith("set channel")->executeBirthdaysSetChannelCommand(event, parameters.removePrefix("set channel").trim())
        parameters.startsWith("<@")->executeBirthdaysRequestCommand(event, parameters.removePrefix("<@").trim())
        //parameters.startsWith("")->{}

        else -> {
            var prefix =getPrefix(event.guild.id)
            if(prefix.length > 1)
                prefix += " "
            event.channel.sendMessage("That isn't a birthday command. Try using " + prefix +"Birthday help").queue()
        }
    }
}

fun executeBirthdaysRememberCommand (event: MessageReceivedEvent, parameters: String){
    enforceDatabaseExistence(event.guild.id)
    if(knownBirthday(event, event.author.id)){
        event.channel.sendMessage("Hey! I already know your birthday!").queue()
        return
    }
    val date = parameters.split("/")
    when {
        date.size==3 -> {
            if(!isDataValid(date[0].toInt(),date[1].toInt(),date[2].toInt())){
                event.channel.sendMessage("This is not a valid date").queue()
                return
            }
            val database = Database.connect("jdbc:sqlite:"+getAbsolutePath("Servers/${event.guild.id}")+"/birthdays.sqlite","org.sqlite.JDBC")
            TransactionManager.manager.defaultIsolationLevel = Connection.TRANSACTION_SERIALIZABLE
            transaction (database) {
                Birthdays.insert {
                    it[Birthdays.playerId] = event.author.id
                    it[Birthdays.day] = date[0].toInt()
                    it[Birthdays.month] = date[1].toInt()
                    it[Birthdays.year] = date[2].toInt()
                }
            }
        }
        date.size==2 -> {
            if(!isDataValid(date[0].toInt(),date[1].toInt())){
                event.channel.sendMessage("This is not a valid date").queue()
                return
            }
            val database = Database.connect("jdbc:sqlite:"+getAbsolutePath("Servers/${event.guild.id}")+"/birthdays.sqlite","org.sqlite.JDBC")
            TransactionManager.manager.defaultIsolationLevel = Connection.TRANSACTION_SERIALIZABLE
            transaction (database) {
                Birthdays.insert {
                    it[Birthdays.playerId] = event.author.id
                    it[Birthdays.day] = date[0].toInt()
                    it[Birthdays.month] = date[1].toInt()
                }
            }
        }
        else -> {
            event.channel.sendMessage("This is not a valid date").queue()
            return
        }
    }
    event.channel.sendMessage("Got it").queue()

}

fun executeBirthdaysForgetCommand (event: MessageReceivedEvent, parameters: String){
    enforceDatabaseExistence(event.guild.id)
    if(!knownBirthday(event, event.author.id)){
        event.channel.sendMessage("I don't know your birthday!").queue()
        return
    }
    val database = Database.connect("jdbc:sqlite:"+getAbsolutePath("Servers/${event.guild.id}")+"/birthdays.sqlite","org.sqlite.JDBC")
    TransactionManager.manager.defaultIsolationLevel = Connection.TRANSACTION_SERIALIZABLE
    transaction (database) {
        Birthdays.deleteWhere {
            Birthdays.playerId eq event.author.id
        }
    }
    event.channel.sendMessage("Done").queue()
}

fun executeBirthdaysAllCommand (event: MessageReceivedEvent, parameters: String){
    enforceDatabaseExistence(event.guild.id)
    val database = Database.connect("jdbc:sqlite:"+getAbsolutePath("Servers/${event.guild.id}")+"/birthdays.sqlite","org.sqlite.JDBC")
    TransactionManager.manager.defaultIsolationLevel = Connection.TRANSACTION_SERIALIZABLE
    var data ="```"
    transaction (database) {
        Birthdays.selectAll().orderBy(Birthdays.month).orderBy(Birthdays.day).forEach{
            val user = event.jda.getUserById(it[Birthdays.playerId])
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
    event.channel.sendMessage(data).queue()
}

fun executeBirthdaysHelpCommand (event: MessageReceivedEvent, parameters: String){
    var prefix =getPrefix(event.guild.id)
    if(prefix.length > 1)
        prefix += " "
    event.channel.sendMessage("Type "+ prefix +"Birthday + sub-command: ```asciidoc\n" +
            "remember:: add your birthday\n" +
            "forget:: remove your birthday\n" +
            "all:: shows all birthdays\n" +
            "@ someone:: shows their birthday\n" +
            "```").queue()
}

fun executeBirthdaysRequestCommand (event: MessageReceivedEvent, parameters: String){
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
    val database = Database.connect("jdbc:sqlite:"+getAbsolutePath("Servers/${event.guild.id}")+"/birthdays.sqlite","org.sqlite.JDBC")
    TransactionManager.manager.defaultIsolationLevel = Connection.TRANSACTION_SERIALIZABLE
    var data =""
    transaction (database) {
        Birthdays.select {Birthdays.playerId eq userID}.forEach{
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
    val filename = getAbsolutePath("Servers/${serverID}")+"/birthdays.sqlite"
    if(!File(filename).exists()){
        val database = Database.connect("jdbc:sqlite:$filename","org.sqlite.JDBC")
        TransactionManager.manager.defaultIsolationLevel = Connection.TRANSACTION_SERIALIZABLE
        transaction (database) {
            SchemaUtils.create(Birthdays)
        }
    }
}

fun knownBirthday(event: MessageReceivedEvent, userID: String) :Boolean{
    val database = Database.connect("jdbc:sqlite:"+getAbsolutePath("Servers/${event.guild.id}")+"/birthdays.sqlite","org.sqlite.JDBC")
    TransactionManager.manager.defaultIsolationLevel = Connection.TRANSACTION_SERIALIZABLE
    val user = transaction { Birthdays.select{Birthdays.playerId eq userID}.toList() }
    return user.isNotEmpty()
}



fun executeBirthdaysSetChannelCommand(event: MessageReceivedEvent, parameters: String){
    val channelID = parameters.removePrefix("<#").removeSuffix(">")
    var result = false
    try{
        val channel = event.guild.getTextChannelById(channelID)
        result= writeToFile(getAbsolutePath("Servers/"+event.guild.id)+"/BirthdayChannel.txt", channel?.id!!)
    }
    catch (e: Exception){

    }
    if(result){
        event.channel.sendMessage("Channel set").queue()
    }
    else{
        event.channel.sendMessage("Failed to set a channel").queue()
    }
}

fun getBirthdaysToday(serverID: String): String{
    enforceDatabaseExistence(serverID)
    val database = Database.connect("jdbc:sqlite:"+getAbsolutePath("Servers/${serverID}")+"/birthdays.sqlite","org.sqlite.JDBC")
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


class HappyBirthdayWishing(input: JDA) : TimerTask() {

    private val bot: JDA = input

    override fun run(){
        File(getAbsolutePath("Servers")).listFiles().forEach {
            val channelID = readFromFile(getAbsolutePath("Servers/"+it.name)+"/BirthdayChannel.txt")
            val channel= if (bot.getTextChannelById(channelID) != null) bot.getTextChannelById(channelID)!! else return@forEach
            val text=getBirthdaysToday(it.name)
            if(text!="")channel.sendMessage("Happy birthday to $text").queue()
        }
    }
}