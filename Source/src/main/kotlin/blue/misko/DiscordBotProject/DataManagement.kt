package blue.misko.DiscordBotProject

import java.io.*

fun GetAbsolutePath(dirPath: String): String{ //creates directory if it doesn't exist
    var directory = File(dirPath)
        if(! directory.exists()){
            directory.mkdir()
        }
    return directory.absolutePath
}

fun GetPrefix(ServerId: String): String {
    val path  = "Servers/$ServerId"
    var prefix = "!"
    try{
        val absolutePath = GetAbsolutePath(path)
        var reader = FileReader(absolutePath+"/prefix.txt")
        prefix = reader.readText()
    }
    catch(e:Exception){
        print(e.message)
    }
    return prefix
}
fun SetPrefix(ServerId: String, newPrefix: String): Boolean{
    val path  = "Servers/$ServerId"
    try{
        val absolutePath = GetAbsolutePath(path)
        var writer = FileWriter(absolutePath+"/prefix.txt")
        writer.write(newPrefix)
        writer.close()
    }
    catch(e:Exception){
        print(e.message)
        return false
    }
    return true
}
