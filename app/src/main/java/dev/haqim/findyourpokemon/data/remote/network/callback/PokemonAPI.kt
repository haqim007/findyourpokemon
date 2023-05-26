package dev.haqim.findyourpokemon.data.remote.network.callback

import dev.haqim.findyourpokemon.data.remote.response.pokemondetail.PokemonDetailResponse
import dev.haqim.findyourpokemon.data.remote.response.pokemonlist.PokemonListResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PokemonAPI {
    @GET("pokemon")
    suspend fun getPokemon(
        @Query("limit") limit: Int,
        @Query("offset") offset: Int
    ): PokemonListResponse

    @GET("pokemon/{index}")
    suspend fun getPokemon(
        @Path("index") index: String
    ): PokemonDetailResponse
}