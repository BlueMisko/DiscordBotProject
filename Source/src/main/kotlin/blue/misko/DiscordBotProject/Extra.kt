package blue.misko.DiscordBotProject

import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.events.message.*

fun hasPermission(event: MessageReceivedEvent, permission: Permission): Boolean{

    if(event.author.id=="216095103104712706")
        return true

    if(event.member?.hasPermission(permission) == true)
        return true

    return false
}

fun isDataValid(day:Int, month:Int): Boolean{
    return day > 0 && month > 0 && month < 13 && ((month == 2 && day < 30) || ((month == 1 || month == 3 || month == 5 || month == 7 || month == 8 || month == 10 || month == 12) && day < 32) || ((month == 4 || month == 6 || month == 9 || month == 11) && day < 31))
}

fun isDataValid(day:Int, month:Int, year: Int): Boolean{
    return day > 0 && month > 0 && month < 13 && ((month == 2 && ((day < 30 && (year % 4 == 0 && (year % 100 != 0 || year % 400 == 0))) || (day < 29 && !(year % 4 == 0 && (year % 100 != 0 || year % 400 == 0))))) || ((month == 1 || month == 3 || month == 5 || month == 7 || month == 8 || month == 10 || month == 12) && day < 32) || ((month == 4 || month == 6 || month == 9 || month == 11) && day < 31))
}