package blue.misko.DiscordBotProject

import java.io.*

fun getAbsolutePath(dirPath: String): String{ //creates directory if it doesn't exist
    var directory = File(File("").absolutePath+"/../"+"/../"+"/../blue.mismas.DiscordBotProject/"+dirPath).normalize()
    if(! directory.parentFile.exists()){
        directory.parentFile.mkdirs()
    }
    if(! directory.exists()){
        directory.mkdir()
    }
    return directory.absolutePath
}

fun getPrefix(ServerId: String): String {
    val path  = "Servers/$ServerId"
    var prefix = "!"
    try{
        val absolutePath = getAbsolutePath(path)
        var reader = FileReader(absolutePath+"/prefix.txt")
        prefix = reader.readText()
    }
    catch(e:Exception){
        print(e.message)
    }
    return prefix
}
fun setPrefix(ServerId: String, newPrefix: String): Boolean{
    val path  = "Servers/$ServerId"
    try{
        val absolutePath = getAbsolutePath(path)
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


fun writeToFile(path: String, text: String): Boolean{
    try{
        var writer = FileWriter(path)
        writer.write(text)
        writer.close()
    }
    catch(e:Exception){
        print(e.message)
        return false
    }
    return true
}

fun readFromFile(path: String): String{
    var text= ""
    try{
        var reader = FileReader(path)
        text = reader.readText()
    }
    catch(e:Exception){
        print(e.message)
    }
    return text
}