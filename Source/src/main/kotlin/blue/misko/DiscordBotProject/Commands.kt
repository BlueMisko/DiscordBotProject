package blue.misko.DiscordBotProject

import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.events.message.*

fun executeCommand(event: MessageReceivedEvent, command:String){
    when (true){
        command.startsWith("birthday") -> executeBirthdayCommand(event, command.removePrefix("birthday").trim())
        command.startsWith("set prefix") -> executeSetPrefixCommand(event, command.removePrefix("set prefix").trim())
        command.startsWith("set role") -> executeSetRoleCommand(event, command.removePrefix("set role").trim())
        command.startsWith("play music") -> executePlayMusicCommand(event, command.removePrefix("play music").trim())
        command.startsWith("help") -> executeHelpCommand(event, command.removePrefix("help").trim())
        command.startsWith("ping") -> event.channel.sendMessage("pong").queue()
        command.startsWith("add command") -> executeAddCommendCommand(event, command.removePrefix("add command").trim())
        else ->{
            event.channel.sendMessage("What?").queue()
        }
    }
}

fun executeBirthdayCommand (event: MessageReceivedEvent, parameters: String){
    event.channel.sendMessage("Birthday commands are not yet implemented").queue()
}

fun executeSetPrefixCommand (event: MessageReceivedEvent, parameters: String){
    if(!HasPermission(event, Permission.MANAGE_SERVER)){
        event.channel.sendMessage("You don't have the permission to do that").queue()
        return
    }

    if(SetPrefix(event.guild.id, parameters))
        event.channel.sendMessage("Prefix was set to $parameters").queue()
    else
        event.channel.sendMessage("Failed to set a prefix").queue()
}

fun executeSetRoleCommand (event: MessageReceivedEvent, parameters: String){
    event.channel.sendMessage("Birthday commands are not yet implemented").queue()
}

fun executePlayMusicCommand (event: MessageReceivedEvent, parameters: String){
    event.channel.sendMessage("Birthday commands are not yet implemented").queue()
}

fun executeHelpCommand (event: MessageReceivedEvent, parameters: String){
    event.channel.sendMessage("Help command is not yet implemented").queue()
}

fun executeAddCommendCommand (event: MessageReceivedEvent, parameters: String){
    event.channel.sendMessage("Add command is not yet implemented").queue()
}
//event.channel.sendMessage("pong").queue()