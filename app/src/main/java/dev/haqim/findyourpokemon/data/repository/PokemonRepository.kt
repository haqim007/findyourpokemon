package dev.haqim.findyourpokemon.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import dev.haqim.findyourpokemon.data.local.LocalDataSource
import dev.haqim.findyourpokemon.data.local.entity.PokemonOwnedEntity
import dev.haqim.findyourpokemon.data.local.entity.toModel
import dev.haqim.findyourpokemon.data.mechanism.NetworkBoundResource
import dev.haqim.findyourpokemon.data.mechanism.Resource
import dev.haqim.findyourpokemon.data.remote.RemoteDataSource
import dev.haqim.findyourpokemon.data.remote.response.pokemondetail.PokemonDetailResponse
import dev.haqim.findyourpokemon.data.remote.response.pokemondetail.toPokemonMovesEntity
import dev.haqim.findyourpokemon.data.remote.response.pokemondetail.toPokemonTypesEntity
import dev.haqim.findyourpokemon.data.remotemediator.PokemonRemoteMediator
import dev.haqim.findyourpokemon.data.remotemediator.PokemonRemoteMediator.Companion.POKEMON_PAGE_SIZE
import dev.haqim.findyourpokemon.di.DispatcherIO
import dev.haqim.findyourpokemon.domain.model.CatchPokemonStatus
import dev.haqim.findyourpokemon.domain.model.Pokemon
import dev.haqim.findyourpokemon.domain.repository.IPokemonRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PokemonRepository @Inject constructor(
    private val localDataSource: LocalDataSource,
    private val remoteMediator: PokemonRemoteMediator,
    private val remoteDataSource: RemoteDataSource,
    @DispatcherIO private val dispatcher: CoroutineDispatcher
): IPokemonRepository{
    @OptIn(ExperimentalPagingApi::class)
    override fun getPokemonList(): Flow<PagingData<Pokemon>> {
        return Pager(
            config = PagingConfig(
                pageSize = POKEMON_PAGE_SIZE
            ),
            remoteMediator = remoteMediator,
            pagingSourceFactory = {
                localDataSource.getAllPokemons()
            }
        ).flow.flowOn(dispatcher).map { pagingData -> 
            pagingData.map { 
                it.toModel()
            }
        }
    }

    @OptIn(ExperimentalPagingApi::class)
    override fun getOwnedPokemonList(): Flow<PagingData<Pokemon>> {
        return Pager(
            config = PagingConfig(
                pageSize = POKEMON_PAGE_SIZE
            ),
            remoteMediator = remoteMediator,
            pagingSourceFactory = {
                localDataSource.getAllOwnedPokemons()
            }
        ).flow.flowOn(dispatcher).map { pagingData ->
            pagingData.map {
                it.toModel()
            }
        }
    }

    override fun getPokemon(id: String): Flow<Resource<Pokemon?>> {
        return object: NetworkBoundResource<Pokemon?, PokemonDetailResponse>(){
            override suspend fun fetchFromRemote(): Result<PokemonDetailResponse> {
                return remoteDataSource.getPokemon(id)
            }

            override suspend fun saveRemoteData(data: PokemonDetailResponse) {
                localDataSource.upsertPokemonTypesAndMoves(
                    id,
                    data.toPokemonMovesEntity(id),
                    data.toPokemonTypesEntity(id)
                )
            }

            override fun loadFromNetwork(data: PokemonDetailResponse): Flow<Pokemon?> {
                return localDataSource.getPokemonById(id).map { 
                    it?.toModel()
                }
            }

        }.asFlow().flowOn(dispatcher)
    }

    override fun catchPokemon(id: String): Flow<Resource<CatchPokemonStatus>> {
        return flow { 
            emit(Resource.Loading())
            delay(1500)
            val result = CatchPokemonStatus.values().random()
            
            if(result == CatchPokemonStatus.SUCCESS)  {
                localDataSource.upsertPokemonOwned(pokemon = PokemonOwnedEntity(pokemonId = id))
            }

            emit(Resource.Success(result))
            
        }
    }

    override suspend fun addPokemonNickname(id: String, nickname: String){
        localDataSource.upsertPokemonOwned(pokemon = PokemonOwnedEntity(pokemonId = id, nickname = nickname))
    }

    override suspend fun releasePokemon(pokemonId: String) {
        localDataSource.removePokemonOwned(pokemonId)
    }
}

