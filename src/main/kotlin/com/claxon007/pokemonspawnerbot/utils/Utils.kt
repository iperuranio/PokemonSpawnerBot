package com.claxon007.pokemonspawnerbot.utils

import com.claxon007.pokemonspawnerbot.PokemonSpawnerBot
import com.github.kotlintelegrambot.Bot
import com.github.kotlintelegrambot.entities.ChatMember
import com.github.kotlintelegrambot.entities.ParseMode
import org.jsoup.Jsoup
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

object Utils {
    @JvmStatic
    fun sendMessage(message : String = "", disableWebPagePreview : Boolean = false, chatID : Long) {
        PokemonSpawnerBot.instance.bot!!.sendMessage(chatID, message, disableWebPagePreview = disableWebPagePreview, parseMode = ParseMode.HTML)
    }

    @JvmStatic
    fun isAdministrator(chatID: Long, userID: Long) : Boolean = isAdministrator(getChatMember(chatID, userID)!!)

    @JvmStatic
    fun isAdministrator(chatMember : ChatMember) : Boolean {
        return when(chatMember.status) {
            "creator", "administrator" -> true
            else -> false
        }
    }

    @JvmStatic
    fun getChatMember(chatID : Long, userID : Long) : ChatMember? = PokemonSpawnerBot.instance.bot!!.getChatMember(chatID, userID).first!!.body()!!.result

    fun Bot.isAdmin(chatID: Long) : Boolean = Utils.isAdministrator(chatID, this.getMe().first!!.body()!!.result!!.id)

    fun getResponse(url : String) : String {
        val doc = Jsoup.connect(url).ignoreContentType(true).get()

        return doc.outerHtml()
    }
}