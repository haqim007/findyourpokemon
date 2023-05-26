package dev.haqim.findyourpokemon.data.remote

import dev.haqim.findyourpokemon.data.remote.network.callback.PokemonAPI
import dev.haqim.findyourpokemon.data.remote.response.pokemondetail.PokemonDetailResponse
import dev.haqim.findyourpokemon.data.remote.response.pokemonlist.PokemonListResponse
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RemoteDataSource @Inject constructor(
    private val service: PokemonAPI
){
    
    suspend fun getPokemonList(limit: Int, offset: Int): Result<PokemonListResponse>{
        return try {
            Result.success(service.getPokemon(limit, offset))
        }catch (e: Exception){
            Result.failure(e)
        }
    }

    suspend fun getPokemon(id: String): Result<PokemonDetailResponse>{
        return try {
            Result.success(service.getPokemon(id))
        }catch (e: Exception){
            Result.failure(e)
        }
    }
    
}