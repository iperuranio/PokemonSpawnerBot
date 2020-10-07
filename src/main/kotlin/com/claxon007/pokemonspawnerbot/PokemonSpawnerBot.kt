package com.claxon007.pokemonspawnerbot

import com.claxon007.pokemonspawnerbot.commands.*
import com.claxon007.pokemonspawnerbot.pokemon.Pokemon
import com.claxon007.pokemonspawnerbot.utils.Utils
import com.github.kotlintelegrambot.Bot
import com.github.kotlintelegrambot.bot
import com.github.kotlintelegrambot.dispatch
import com.github.kotlintelegrambot.dispatcher.command
import com.github.kotlintelegrambot.dispatcher.message
import com.github.kotlintelegrambot.dispatcher.text
import com.github.kotlintelegrambot.extensions.filters.Filter
import okhttp3.logging.HttpLoggingInterceptor

class PokemonSpawnerBot {
    private object InstanceHolder {
        @JvmStatic
        val instance : PokemonSpawnerBot = PokemonSpawnerBot()
    }

    var timers : HashMap<Long, Int> = HashMap()
    var pokemons : List<Pokemon> = ArrayList()

    val defaultTimer = 60
    private val apiKey = ""
    var bot : Bot? = null
    var version = "1.0.0-BETA"

    val urlPokemonResolver = "https://pokeapi.co/api/v2/pokemon?offset=0&limit=-1"
    private val stringToCheck = "spawnedPokemon"

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

                    message(Filter.Reply and Filter.Sticker) { bot, update ->
                        val repliedMessage = update.message!!.replyToMessage

                        if(repliedMessage!!.from!!.id == Utils.getBotID()) {
                            var pokemonString = repliedMessage.text!!.reversed().split("#")[0].reversed()

                            if(pokemonString.contains(instance.stringToCheck)) {
                                var pokemon = pokemonString.substring(instance.stringToCheck.length)


                            }
                        }
                    }
                }
            }

            instance.bot!!.startPolling()
        }
    }
}