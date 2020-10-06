package com.claxon007.pokemonspawnerbot.commands

import com.github.kotlintelegrambot.Bot
import com.github.kotlintelegrambot.CommandHandleUpdate
import com.github.kotlintelegrambot.entities.Update

class ListCommand : IPokemonSpawnerCommand {
    private object InstanceHandler {
        val instance = ListCommand()
    }

    companion object {
        val instance : ListCommand by lazy { InstanceHandler.instance }
    }

    override fun command() : CommandHandleUpdate = { bot: Bot, update: Update, list: List<String> ->

    }
}