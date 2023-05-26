package dev.haqim.findyourpokemon.data.remotemediator

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import dev.haqim.findyourpokemon.data.local.LocalDataSource
import dev.haqim.findyourpokemon.data.local.entity.PokemonDetailEntity
import dev.haqim.findyourpokemon.data.local.entity.RemoteKeys
import dev.haqim.findyourpokemon.data.remote.RemoteDataSource
import dev.haqim.findyourpokemon.data.remote.response.pokemonlist.toEntity
import javax.inject.Inject


@OptIn(ExperimentalPagingApi::class)
class PokemonRemoteMediator @Inject constructor(
    private val localDataSource: LocalDataSource,
    private val remoteDataSource: RemoteDataSource
): RemoteMediator<Int, PokemonDetailEntity>() {
    override suspend fun load(loadType: LoadType, state: PagingState<Int, PokemonDetailEntity>): MediatorResult {
        
        val page = when(loadType){
            LoadType.REFRESH -> {
                val remoteKeys: RemoteKeys? = getRemoteKeyClosestToCurrentPosition(state)
                remoteKeys?.nextKey?.minus(1) ?: INITIAL_PAGE_INDEX
            }
            LoadType.PREPEND -> {
                val remoteKeys: RemoteKeys? = getRemoteKeyForFirstItem(state)
                val prevKey: Int = remoteKeys?.prevKey ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                prevKey
            }
            LoadType.APPEND -> {
                val remoteKeys: RemoteKeys? = getRemoteKeyForLastItem(state)
                val nextKey: Int = remoteKeys?.nextKey ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                nextKey
            }
        }

        val result = try {
            val offset = (page - 1) * state.config.pageSize
            val response = remoteDataSource.getPokemonList(state.config.pageSize, offset)
            val endOfPaginationReached = response.getOrNull()?.nextUrl == null

            val prevKey = if (page == 1) null else page - 1
            val nextKey = if (endOfPaginationReached) null else page + 1
            val pokemons = response.getOrNull()?.results ?: listOf()
            val keys = pokemons.map {
                val urlParts = it.url.split("/")
                val id = urlParts[urlParts.size - 2]
                RemoteKeys(
                    id,
                    prevKey,
                    nextKey
                )
            }

            localDataSource.insertKeysAndPokemons(
                keys,
                pokemons.toEntity(),
                loadType == LoadType.REFRESH
            )
            
            MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        }catch (e: Exception){
            MediatorResult.Error(e)
        }

        return result

    }


    private suspend fun getRemoteKeyClosestToCurrentPosition(state: PagingState<Int, PokemonDetailEntity>): RemoteKeys? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.pokemon?.id?.let { id ->
                localDataSource.getRemoteKeysById(id)
            }
        }
    }

    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, PokemonDetailEntity>): RemoteKeys?{
        return state.pages
            .firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()
            ?.let { data ->
                localDataSource.getRemoteKeysById(data.pokemon.id)
            }
    }

    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, PokemonDetailEntity>): RemoteKeys?{
        return state.pages.lastOrNull{it.data.isNotEmpty()}?.data?.lastOrNull()?.let { data ->
            localDataSource.getRemoteKeysById(data.pokemon.id)
        }
    }

     companion object {
        const val INITIAL_PAGE_INDEX = 1
        const val POKEMON_PAGE_SIZE = 30
    }
}