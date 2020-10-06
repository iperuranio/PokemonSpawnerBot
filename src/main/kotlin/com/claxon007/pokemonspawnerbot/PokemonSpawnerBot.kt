package com.claxon007.pokemonspawnerbot

import com.claxon007.pokemonspawnerbot.commands.*
import com.github.kotlintelegrambot.Bot
import com.github.kotlintelegrambot.bot
import com.github.kotlintelegrambot.dispatch
import com.github.kotlintelegrambot.dispatcher.command
import okhttp3.logging.HttpLoggingInterceptor

class PokemonSpawnerBot {
    private object InstanceHolder {
        @JvmStatic
        val instance : PokemonSpawnerBot = PokemonSpawnerBot()
    }

    private val apiKey = ""
    var bot : Bot? = null
    var version = "1.0.0-BETA"

    val urlPokemonResolver = "https://pokeapi.co/api/v2/pokemon?offset=0&limit=-1"

    companion object {
        val instance : PokemonSpawnerBot by lazy { InstanceHolder.instance }

        @JvmStatic
        fun main(args: Array<String>) {
//            version =

            instance.bot = bot {
                token = instance.apiKey
                timeout = 0
                logLevel = HttpLoggingInterceptor.Level.NONE

                dispatch {
                    command("start", StartCommand.instance.command())
                    command("tempo", TempoCommand.instance.command())
                    command("configura", ConfiguraCommand.instance.command())
                    command("list", ListCommand.instance.command())
//                    command("info", InfoCommand.instance.command())
                }
            }

            instance.bot!!.startPolling()
        }
    }
}