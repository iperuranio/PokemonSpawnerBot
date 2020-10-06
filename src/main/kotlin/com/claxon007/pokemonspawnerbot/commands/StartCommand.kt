package com.claxon007.pokemonspawnerbot.commands

import com.claxon007.pokemonspawnerbot.utils.Utils
import com.github.kotlintelegrambot.Bot
import com.github.kotlintelegrambot.CommandHandleUpdate
import com.github.kotlintelegrambot.entities.Update

class StartCommand : IPokemonSpawnerCommand {
    private object InstanceHandler {
        val instance = StartCommand()
    }

    companion object {
        val instance : StartCommand by lazy { InstanceHandler.instance }
    }

    private val messageSuffix = "\n" +
            "Una volta fatto digita /configura."

    private val messageGroupPrefix = "\uD83D\uDC51 Ciao e <b>grazie</b> per avermi aggiunto al tuo gruppo!" + "\n" + "\n" +
            "Prima di iniziare con la configurazione ti chiedo di nominarmi <b>amministratore</b>. \uD83D\uDEE0\n"

    override fun command() : CommandHandleUpdate = { bot: Bot, update: Update, list: List<String> ->
        val message = update.message
        val chat = message!!.chat
        val chatType = chat!!.type
        val chatID = chat!!.id
        val userSentMessage = message.from

        if(chatType == "private") {
            sendMessage("\uD83D\uDC51 Ciao ${userSentMessage!!.firstName}!\n\n" +
                    "Aggiungimi ad un gruppo e rendimi amministratore. \uD83E\uDDE8\n" + messageSuffix, chatID = chatID)
        } else if(chatType != "channel") {
            if(!userSentMessage!!.isBot) {
                val chatMemberOfUser = bot.getChatMember(chatID, userSentMessage.id).first!!.body()!!.result

                if(Utils.isAdministrator(chatMemberOfUser!!)) {
                    sendMessage(messageGroupPrefix + messageSuffix, chatID = chatID)
                }
            }
        }
    }
}