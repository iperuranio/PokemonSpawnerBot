package com.claxon007.pokemonspawnerbot.utils

import com.claxon007.pokemonspawnerbot.PokemonSpawnerBot
import com.github.kotlintelegrambot.entities.ChatMember
import com.github.kotlintelegrambot.entities.ParseMode

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
}