package blue.misko.DiscordBotProject

import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.*
import java.io.File
import java.sql.Connection


object Birthdays: Table(){
    val playerId = varchar("playerID", 20).primaryKey()
    val day = integer("day")
    val month = integer("month")
    val year = integer("year").nullable()
}

fun executeBirthdaysCommands (event: MessageReceivedEvent, parameters: String){
    enforceDatabaseExistence(event)
    when (true){
        parameters.startsWith("remember")->executeBirthdaysRememberCommand(event, parameters.removePrefix("remember").trim())
        parameters.startsWith("forget")->executeBirthdaysForgetCommand(event, parameters.removePrefix("forget").trim())
        parameters.startsWith("all")->executeBirthdaysAllCommand(event, parameters.removePrefix("all").trim())
        parameters.startsWith("help")->executeBirthdaysHelpCommand(event, parameters.removePrefix("help").trim())
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
    enforceDatabaseExistence(event)
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
    enforceDatabaseExistence(event)
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
    enforceDatabaseExistence(event)
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
            "all:: shows all your birthday\n" +
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
    enforceDatabaseExistence(event)
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



fun enforceDatabaseExistence(event: MessageReceivedEvent){
    val filename = getAbsolutePath("Servers/${event.guild.id}")+"/birthdays.sqlite"
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