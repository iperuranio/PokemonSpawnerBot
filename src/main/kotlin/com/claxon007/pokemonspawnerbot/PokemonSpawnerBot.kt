package com.claxon007.pokemonspawnerbot

import com.claxon007.pokemonspawnerbot.commands.ConfiguraCommand
import com.claxon007.pokemonspawnerbot.commands.ListCommand
import com.claxon007.pokemonspawnerbot.commands.StartCommand
import com.claxon007.pokemonspawnerbot.commands.TempoCommand
import com.claxon007.pokemonspawnerbot.pokemon.Pokemon
import com.claxon007.pokemonspawnerbot.timer.PokemonTask
import com.claxon007.pokemonspawnerbot.utils.Utils
import com.github.kotlintelegrambot.Bot
import com.github.kotlintelegrambot.bot
import com.github.kotlintelegrambot.dispatch
import com.github.kotlintelegrambot.dispatcher.command
import com.github.kotlintelegrambot.dispatcher.message
import com.github.kotlintelegrambot.entities.ParseMode
import com.github.kotlintelegrambot.extensions.filters.Filter
import com.google.gson.JsonParser
import com.google.gson.stream.JsonReader
import okhttp3.logging.HttpLoggingInterceptor
import java.io.File
import java.io.StringReader
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


class PokemonSpawnerBot {
    private object InstanceHolder {
        @JvmStatic
        val instance : PokemonSpawnerBot = PokemonSpawnerBot()
    }

    var timers : HashMap<Long, Int> = HashMap()
    var tasks : MutableList<PokemonTask> = ArrayList()
    var pokemons : MutableList<Pokemon> = ArrayList()

    val defaultTimer = 2 //cambiare a 60
    private val apiKey = 
    var bot : Bot? = null
    var version = "1.0.0-BETA"

    val congratulationFileLink = "https://i.imgur.com/XxK4nrk.gif"
    val sadFileLink = "https://i.imgur.com/4ccLxjT.gif"
    val urlPokemonResolver = "https://pokeapi.co/api/v2/pokemon?offset=0&limit=893"
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

            instance.pokemons.clear()

            try {
                val reader = JsonReader(StringReader(Utils.getJSONResponse(instance.urlPokemonResolver)))
                reader.isLenient = true

                var jsonObject = JsonParser.parseReader(reader).asJsonObject

                jsonObject.get("results").asJsonArray.forEach { pokemonName ->
                    val pokemonName = pokemonName.asJsonObject.get("name").toString().replace("\"", "")
                    val pokemon = Pokemon(pokemonName)

                    if(pokemon.image.exists()) {
                        instance.pokemons.add(pokemon)
                        println("Aggiungo $pokemonName")
                    }
                }

            } catch (e: Exception) {
                e.printStackTrace()
                return
            }

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

                    message(Filter.Reply and Filter.Sticker and Filter.Group) { _, update ->
                        val repliedMessage = update.message!!.replyToMessage
                        val chatID = update.message!!.chat.id

                        if(repliedMessage!!.forwardFromChat != null)
                            return@message

                        if(repliedMessage.from!!.id == Utils.getBotID()) {
                            var pokemonString: String

                            try {
                                val stringToUse = if(repliedMessage.text == null) repliedMessage.caption else repliedMessage.text
                                pokemonString = stringToUse!!.reversed().split("#")[0].reversed()
                            } catch (e: Exception) {
                                return@message
                            }

                            if(pokemonString.contains(instance.stringToCheck)) {
                                var pokemon = pokemonString.substring(instance.stringToCheck.length)
                                var pokemonTask : PokemonTask? = Utils.getTaskFromChatID(chatID)

                                if(pokemonTask == null) {
                                    println("Nessuna task attiva per la chat $chatID ${update.message!!.chat.title}")
                                    return@message
                                }

                                if(pokemonTask.pokemonName != pokemon.toLowerCase()) {
                                    return@message
                                }

                                var userWhoWon = update.message!!.from!!

                                pokemonTask.restart()
                                val suffix = "Il prossimo Pokémon apparirà tra <b>${pokemonTask.startTimer} minuti</b>." //dividere per 60

                                val random = Random().nextInt(10)

                                if(random > 3) {
                                    bot.sendAnimation(chatID, instance.congratulationFileLink, caption = "Congratulazioni! \uD83D\uDC51\n" +
                                            "\n" +
                                            "<a href=\"tg://user?id=${userWhoWon.id}\">${userWhoWon.firstName}</a> ha catturato <b>$pokemon</b>!\n" + suffix
                                            , parseMode = ParseMode.HTML.toString(), replyToMessageId = repliedMessage.messageId)
                                } else {
                                    bot.sendAnimation(chatID, instance.sadFileLink, caption = "Per poco! \uD83D\uDE14\n" +
                                            "\n" +
                                            "<a href=\"tg://user?id=${userWhoWon.id}\">${userWhoWon.firstName}</a> stava per catturare <b>$pokemon</b>!\n" + suffix
                                            , parseMode = ParseMode.HTML.toString(), replyToMessageId = repliedMessage.messageId)
                                }
                            }
                        }
                    }
                }
            }

            instance.bot!!.startPolling()
        }
    }
}