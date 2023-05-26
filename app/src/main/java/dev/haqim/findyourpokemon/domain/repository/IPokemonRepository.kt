package dev.haqim.findyourpokemon.domain.repository

import androidx.paging.PagingData
import dev.haqim.findyourpokemon.data.mechanism.Resource
import dev.haqim.findyourpokemon.domain.model.CatchPokemonStatus
import dev.haqim.findyourpokemon.domain.model.Pokemon
import kotlinx.coroutines.flow.Flow

interface IPokemonRepository {
    fun getPokemonList(): Flow<PagingData<Pokemon>>
    fun getOwnedPokemonList(): Flow<PagingData<Pokemon>>
    fun getPokemon(id: String): Flow<Resource<Pokemon?>>
    fun catchPokemon(id: String): Flow<Resource<CatchPokemonStatus>>
    suspend fun addPokemonNickname(id: String, nickname: String)
    suspend fun releasePokemon(pokemonId: String)
}