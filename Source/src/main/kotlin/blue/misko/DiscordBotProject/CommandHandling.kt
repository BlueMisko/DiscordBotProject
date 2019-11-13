package blue.misko.DiscordBotProject

import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.entities.User
import net.dv8tion.jda.api.events.message.*

fun executeCommand(event: MessageReceivedEvent, command:String){
    when (true){
        command.startsWith("birthday") -> executeBirthdaysCommands(event, command.removePrefix("birthday").trim())
        command.startsWith("set prefix") -> executeSetPrefixCommand(event, command.removePrefix("set prefix").trim())
        command.startsWith("set role") -> executeSetRoleCommand(event, command.removePrefix("set role").trim())
        command.startsWith("play music") -> executePlayMusicCommand(event, command.removePrefix("play music").trim())
        command.startsWith("pfp") -> executePFPCommand(event, command.removePrefix("pfp").trim())
        command.startsWith("help") -> executeHelpCommand(event, command.removePrefix("help").trim())
        command.startsWith("ping") -> event.channel.sendMessage("pong").queue()
        command.startsWith("add command") -> executeAddCommendCommand(event, command.removePrefix("add command").trim())
        //command.startsWith("") -> {}
        else ->{
            if(command.contains("says what")||command.contains("say what")){
                event.channel.sendMessage("I'm not falling for that again").queue()
            }
            var prefix =getPrefix(event.guild.id)
            if(prefix.length > 1)
                prefix += " "
            event.channel.sendMessage("What?\n I don't know what you want. Try using " + getPrefix(event.guild.id) +"help").queue()
        }
    }
}


fun executeSetPrefixCommand (event: MessageReceivedEvent, parameters: String){
    if(!hasPermission(event, Permission.MANAGE_SERVER)){
        event.channel.sendMessage("You don't have the permission to do that").queue()
        return
    }

    if(setPrefix(event.guild.id, parameters))
        event.channel.sendMessage("Prefix was set to $parameters").queue()
    else
        event.channel.sendMessage("Failed to set a prefix").queue()
}

fun executeSetRoleCommand (event: MessageReceivedEvent, parameters: String){
    event.channel.sendMessage("Set role commands are not yet implemented").queue()
}

fun executePlayMusicCommand (event: MessageReceivedEvent, parameters: String){
    event.channel.sendMessage("Play music commands are not yet implemented").queue()
}

fun executePFPCommand (event: MessageReceivedEvent, parameters: String){
    var user :User

    try{
        user= event.jda.getUserById(parameters.removePrefix("<@").removeSuffix(">"))!!
    }
    catch(e: Exception){
        user = event.author
    }

    if(event.author.avatarUrl != null){
        event.channel.sendMessage(user.avatarUrl!!).queue()
    }
    else{
        event.channel.sendMessage(user.defaultAvatarUrl).queue()
    }
}

fun executeHelpCommand (event: MessageReceivedEvent, parameters: String){
    val prefix =getPrefix(event.guild.id)
    event.channel.sendMessage("Type "+ prefix +" + command: ```asciidoc\n" +
            "pfp @user:: get their profile picture\n" +
            "birthday:: birthday commands\n" + /*
            "-> remember date(dd/mm/yyyy):: saves your birthday\n" +
            "-> forget:: deletes your birthday\n" +
            "-> all:: shows all birthdays\n" +
            "-> @user:: get users birthday\n" +*/
            "```").queue()
}

fun executeAddCommendCommand (event: MessageReceivedEvent, parameters: String){
    event.channel.sendMessage("Add command is not yet implemented").queue()
}
//event.channel.sendMessage("pong").queue()