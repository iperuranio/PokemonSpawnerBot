package com.claxon007.pokemonspawnerbot.pokemon

import com.claxon007.pokemonspawnerbot.utils.Utils
import org.jsoup.Jsoup
import org.jsoup.select.Elements
import java.io.File
import java.io.FileOutputStream

class Pokemon(var name: String) {
    val urlPrefix = "https://pokeapi.co/api/v2/pokemon/"
    var pokemonURL : String = ""
    val stringKey = "front_default"
    var imageURL : String = ""
    var correctName : String = ""
    val folder = "images"

    init {
        pokemonURL = urlPrefix + name
        correctName = name[0].toUpperCase()+name.substring(1)

        val response = Utils.getResponse(pokemonURL)
        imageURL = response.split(stringKey)[1].split(",")[0].replaceFirst(":", "").replace("\"", "")
    }

    fun getPhotoAsFile() : File {
        val doc = Jsoup.connect(imageURL).ignoreContentType(true).execute()
        var file = File("$folder/$name.png")
        var folder = File("$folder")

        if(!folder.exists()) {
            folder.mkdirs()
        }

        if(!file.exists()) {
            file.createNewFile()
        }

        var out = FileOutputStream(file)
        out.write(doc.bodyAsBytes())

        out.close()
        return file
    }
}