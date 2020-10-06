package com.claxon007.pokemonspawnerbot.commands

import com.github.kotlintelegrambot.Bot
import com.github.kotlintelegrambot.CommandHandleUpdate
import com.github.kotlintelegrambot.entities.Update

class InfoCommand : IPokemonSpawnerCommand {
    private object InstanceHandler {
        val instance = InfoCommand()
    }

    companion object {
        val instance : InfoCommand by lazy { InstanceHandler.instance }
    }

    override fun command() : CommandHandleUpdate = { bot: Bot, update: Update, list: List<String> ->

    }

}
