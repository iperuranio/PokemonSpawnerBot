package com.claxon007.pokemonspawnerbot.commands

import com.claxon007.pokemonspawnerbot.utils.Utils
import com.github.kotlintelegrambot.CommandHandleUpdate

interface IPokemonSpawnerCommand {
    fun sendMessage(message: String = "", disableWebPagePreview : Boolean = false, chatID: Long) {
        Utils.sendMessage(message, disableWebPagePreview = disableWebPagePreview, chatID = chatID);
    }

    fun command() : CommandHandleUpdate
}