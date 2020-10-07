package com.claxon007.pokemonspawnerbot

import com.claxon007.pokemonspawnerbot.commands.*
import com.claxon007.pokemonspawnerbot.pokemon.Pokemon
import com.claxon007.pokemonspawnerbot.utils.Utils
import com.github.kotlintelegrambot.Bot
import com.github.kotlintelegrambot.bot
import com.github.kotlintelegrambot.dispatch
import com.github.kotlintelegrambot.dispatcher.command
import com.github.kotlintelegrambot.dispatcher.message
import com.github.kotlintelegrambot.entities.ParseMode
import com.github.kotlintelegrambot.extensions.filters.Filter
import okhttp3.logging.HttpLoggingInterceptor
import java.io.File
import java.lang.Exception

class PokemonSpawnerBot {
    private object InstanceHolder {
        @JvmStatic
        val instance : PokemonSpawnerBot = PokemonSpawnerBot()
    }

    var timers : HashMap<Long, Int> = HashMap()
    var pokemons : List<Pokemon> = ArrayList()

    val defaultTimer = 60
    private val apiKey = 
    var bot : Bot? = null
    var version = "1.0.0-BETA"

    val urlPokemonResolver = "https://pokeapi.co/api/v2/pokemon?offset=0&limit=-1"
    val stringToCheck = "spawnedPokemon"

    val imageFolder = "images"
    val groupsConfigurationFolder = "saved_groups"
    var congratulationFile : File = File("")

    companion object {
        val instance : PokemonSpawnerBot by lazy { InstanceHolder.instance }

        @JvmStatic
        fun main(args: Array<String>) {
//            version =

            var groupsConfiguration = File(instance.groupsConfigurationFolder)

            if(!groupsConfiguration.exists()) {
                groupsConfiguration.mkdirs()
            }

            instance.congratulationFile = File("${instance.imageFolder}/congratulations.gif")

            if(!instance.congratulationFile.exists())
                println("GIF non trovata.")

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

                    message(Filter.Reply and Filter.Sticker) { _, update ->
                        val repliedMessage = update.message!!.replyToMessage
                        val chatID = update.message!!.chat.id

                        if(repliedMessage!!.forwardFromChat != null)
                            return@message

                        if(repliedMessage.from!!.id == Utils.getBotID()) {
                            var pokemonString: String

                            try {
                                val stringToUse = if(repliedMessage.text == null) repliedMessage.caption else repliedMessage.text
                                pokemonString = stringToUse!!.reversed().split("#")[0].reversed()
                            } catch(e : Exception) {
                                return@message
                            }

                            if(pokemonString.contains(instance.stringToCheck)) {
                                var pokemon = pokemonString.substring(instance.stringToCheck.length)
                                var userWhoWon = update.message!!.from!!

                                var ex = bot.sendAnimation(chatID, instance.congratulationFile, caption = "Congratulazioni! \uD83D\uDC51\n" +
                                        "\n" +
                                        "<a href=\"tg://user?id=${userWhoWon.id}\">${userWhoWon.firstName}</a> ha catturato <b>$pokemon</b>!\n" +
                                        "Il prossimo Pokémon apparirà tra X minuti.", parseMode = ParseMode.HTML.toString())

                                println(ex.first!!.errorBody()!!.string())
                            }
                        }
                    }
                }
            }

            instance.bot!!.startPolling()
        }
    }
}