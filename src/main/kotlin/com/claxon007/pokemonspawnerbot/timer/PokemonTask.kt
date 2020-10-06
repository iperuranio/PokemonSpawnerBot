package com.claxon007.pokemonspawnerbot.timer

import com.claxon007.pokemonspawnerbot.PokemonSpawnerBot
import com.claxon007.pokemonspawnerbot.pokemon.Pokemon
import com.github.kotlintelegrambot.entities.ParseMode
import java.util.*

class PokemonTask(var chatID: Long, var startTimer: Int = PokemonSpawnerBot.instance.defaultTimer) : TimerTask() {

    var decrement = 0
    var stop : Boolean = false
    var pokemonSpawned = false

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

    fun stop() {
        stop = true
        Thread.currentThread().interrupt()
    }

    fun updateTimer(timer : Int) {
        startTimer = timer
        resetDecrement()
    }

    private fun spawnPokemon() {
        setSpawnPokemon(true)

        val dummyPokemon = Pokemon("ditto")

        PokemonSpawnerBot.instance.bot!!.sendPhoto(chatID, dummyPokemon.getPhotoAsFile(),
            "‼️ Attenzione è spawnato ${dummyPokemon.correctName}!\n\nUtilizza *inserire qualcosa da utilizzare* per catturarlo prima degli altri! \uD83C\uDFC6",
            ParseMode.HTML)
    }

    fun setSpawnPokemon(pokemonSpawned : Boolean) {
        this.pokemonSpawned = pokemonSpawned
    }
}