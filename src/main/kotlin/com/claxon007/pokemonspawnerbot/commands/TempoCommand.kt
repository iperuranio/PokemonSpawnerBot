package com.claxon007.pokemonspawnerbot.commands

import com.github.kotlintelegrambot.Bot
import com.github.kotlintelegrambot.CommandHandleUpdate
import com.github.kotlintelegrambot.entities.Update

class TempoCommand : IPokemonSpawnerCommand {
    private object InstanceHandler {
        val instance = TempoCommand()
    }

    companion object {
        val instance : TempoCommand by lazy { InstanceHandler.instance }
    }

    override fun command() : CommandHandleUpdate = { bot: Bot, update: Update, list: List<String> ->

    }

}
