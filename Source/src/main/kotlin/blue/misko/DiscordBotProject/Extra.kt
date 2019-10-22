package blue.mismas.fAnnedomBot

import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.events.message.*

fun HasPermission(event: MessageReceivedEvent, permission: Permission): Boolean{

    if(event.author.id=="216095103104712706")
        return true

    if(event.member?.hasPermission(permission) == true)
        return true

    return false
}