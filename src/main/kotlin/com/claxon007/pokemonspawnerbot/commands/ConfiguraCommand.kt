package com.claxon007.pokemonspawnerbot.commands

import com.claxon007.pokemonspawnerbot.PokemonSpawnerBot
import com.claxon007.pokemonspawnerbot.utils.Utils
import com.github.kotlintelegrambot.Bot
import com.github.kotlintelegrambot.CommandHandleUpdate
import com.github.kotlintelegrambot.entities.Update

class ConfiguraCommand : IPokemonSpawnerCommand {
    private object InstanceHandler {
        val instance = ConfiguraCommand()
    }

    companion object {
        val instance : ConfiguraCommand by lazy { InstanceHandler.instance }
    }

    private val messageConfigura = "La configurazione del bot è semplice e intuitiva. \uD83D\uDE4B\uD83C\uDFFB\u200D♂️\n" +
            "\uD83D\uDEE0 Puoi ottenere questo messaggio in ogni momento tramite il comando /configura.\n" +
            "\n" +
            "\uD83C\uDFC6 Ecco la lista dei comandi utili per la configurazione:\n" +
            "\n" +
            "<code>/tempo &lt;minuti&gt;</code> - Definisce l'intervallo di tempo di <b>stallo</b> tra i vari spawn di Pokémon. (<i>default: 60 minuti</i>)\n" +
            "_______________________________\n" +
            "\n" +
            "\uD83D\uDCE4 Comandi di altro tipo:\n" +
            "\n" +
            "<code>/spawn</code> - Puoi spawnare un <b>Pokémon</b> casuale quando preferisci.\n" +
            "<code>/list</code> - Ricevi la <b>lista</b> dei Pokémon che possono spawnare.\n" +
            "_______________________________\n" +
            "\n" +
            "\uD83D\uDD2E Pokémon Spawner Bot <b>${PokemonSpawnerBot.instance.version}</b> by @claxon007."

    override fun command() : CommandHandleUpdate = { bot: Bot, update: Update, list: List<String> ->
        val message = update.message
        val chat = message!!.chat
        val chatType = chat!!.type
        val chatID = chat!!.id
        val userSentMessage = message.from

        if(chatType == "private") {
            sendMessage(messageConfigura, chatID = chatID)
        } else if(chatType != "channel") {
            if(!userSentMessage!!.isBot) {
                if(Utils.isAdministrator(chatID, bot.getMe().first!!.body()!!.result!!.id)) { //se il bot è admin
                    val chatMemberOfUser =

                        if(Utils.isAdministrator(chatMemberOfUser!!)) {
                            sendMessage(messageConfigura, chatID = chatID)
                        }
                }
            }
        }
    }
}