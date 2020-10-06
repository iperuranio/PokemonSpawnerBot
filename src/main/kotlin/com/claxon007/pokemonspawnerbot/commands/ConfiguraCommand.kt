package com.claxon007.pokemonspawnerbot.commands

import com.claxon007.pokemonspawnerbot.PokemonSpawnerBot
import com.claxon007.pokemonspawnerbot.timer.PokemonTask
import com.claxon007.pokemonspawnerbot.utils.Utils
import com.claxon007.pokemonspawnerbot.utils.Utils.isAdmin
import com.github.kotlintelegrambot.Bot
import com.github.kotlintelegrambot.CommandHandleUpdate
import com.github.kotlintelegrambot.entities.Update
import java.util.*

class ConfiguraCommand : IPokemonSpawnerCommand {
    private object InstanceHandler {
        val instance = ConfiguraCommand()
    }

    companion object {
        val instance : ConfiguraCommand by lazy { InstanceHandler.instance }
    }

    private val nonAdministratorYet = "❌ Non sono ancora amministratore del gruppo."
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
        val chatType = chat.type
        val chatID = chat.id
        val userSentMessage = message.from

        if(chatType == "private") {
            sendMessage(messageConfigura, chatID = chatID)
        } else if(chatType != "channel") {
            val chatMemberOfUser = Utils.getChatMember(chatID, userSentMessage!!.id)

            if(Utils.isAdministrator(chatMemberOfUser!!)) { //se quello che fa il comando è admin
                if(!userSentMessage.isBot) {
                    if(bot.isAdmin(chatID)) { //se il bot è admin
                        sendMessage(messageConfigura, chatID = chatID)

                        PokemonSpawnerBot.instance.timers.put(chatID, PokemonSpawnerBot.instance.defaultTimer)

                        val timer = Timer()
                        val task = PokemonTask(chatID, 2)

                        timer.schedule(task as TimerTask, 0, 1000)
                    } else {
                        sendMessage(nonAdministratorYet, chatID = chatID)
                    }
                }
            }
        }
    }
}