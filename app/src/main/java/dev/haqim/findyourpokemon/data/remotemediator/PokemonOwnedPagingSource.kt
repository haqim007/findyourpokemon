package dev.haqim.findyourpokemon.data.remotemediator

import androidx.paging.PagingSource
import androidx.paging.PagingState
import dev.haqim.findyourpokemon.data.local.LocalDataSource
import dev.haqim.findyourpokemon.data.local.entity.toModel
import dev.haqim.findyourpokemon.domain.model.Pokemon
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class PokemonOwnedPagingSource (
    private val localDataSource: LocalDataSource
): PagingSource<Int, Pokemon>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Pokemon> {

        val position = params.key ?: 0
        val offset = position * 30

        return try {
            val pokemons = withContext(Dispatchers.IO){
                localDataSource.getAllOwnedPokemons(30, offset).toModel()
            }

            val nextKey = if (pokemons.isEmpty()) {
                null
            } else {
                // initial load size = 3 * NETWORK_PAGE_SIZE
                // ensure we're not requesting duplicating items, at the 2nd request
                position + (params.loadSize / 30)
            }
            LoadResult.Page(
                data = pokemons,
                prevKey = if (position == 0) null else position - 1,
                nextKey = nextKey
            )
        } catch (exception: IOException) {
            return LoadResult.Error(exception)
        } catch (exception: HttpException) {
            return LoadResult.Error(exception)
        } catch (exception: Exception) {
            return LoadResult.Error(exception)
        }
    }
    // The refresh key is used for subsequent refresh calls to PagingSource.load after the initial load
    override fun getRefreshKey(state: PagingState<Int, Pokemon>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey
        }
    }
}
