package com.claxon007.pokemonspawnerbot.timer

import com.claxon007.pokemonspawnerbot.PokemonSpawnerBot
import com.claxon007.pokemonspawnerbot.utils.Utils
import com.github.kotlintelegrambot.entities.ParseMode
import java.util.*

class PokemonTask(private var chatID : Long, var startTimer : Int = PokemonSpawnerBot.instance.defaultTimer) : TimerTask() {
    private var decrement = 0
    private var stop : Boolean = false
    private var pokemonSpawned = false
    var pokemonName : String = ""

    init {
        resetDecrement()
    }

    override fun run() {
        if(!stop && !pokemonSpawned) {
            if(decrement-- == 0) {
                resetDecrement()
                //spawn pokemon
                spawnPokemon()
            }
        }
    }

    private fun resetDecrement() {
        decrement = startTimer
    }

    fun setTemporaryStop(stop : Boolean) {
        this.stop = stop
    }

    fun stop() {
        stop = true
        removeFromList()
        Thread.currentThread().interrupt()
    }

    fun updateTimer(timer : Int) {
        startTimer = timer
        resetDecrement()
    }

    fun removeFromList() {
        PokemonSpawnerBot.instance.tasks.forEach { task ->
            if(task.match(chatID)) {
                PokemonSpawnerBot.instance.tasks.remove(task)
            }
        }
    }

    fun restart() {
        setSpawnPokemon(false)
        pokemonName = ""
    }

    fun match(chatID : Long) = chatID == this.chatID

    private fun spawnPokemon() {
        setSpawnPokemon(true)

        val randomPokemon = Utils.randomPokemon()
        pokemonName = randomPokemon.correctName.toLowerCase()

        println(randomPokemon.image.absolutePath)
        PokemonSpawnerBot.instance.bot!!.sendPhoto(chatID, randomPokemon.image,
            "‼️ Attenzione è spawnato <b>${randomPokemon.correctName}</b>!\n\nUtilizza *inserire qualcosa da utilizzare* per catturarlo prima degli altri! \uD83C\uDFC6" +
                    "\n\n<code>#${PokemonSpawnerBot.instance.stringToCheck}${randomPokemon.correctName}</code>",
            ParseMode.HTML)
    }

    fun setSpawnPokemon(pokemonSpawned : Boolean) {
        this.pokemonSpawned = pokemonSpawned
    }
}