package com.uniwa.moviender.data

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.uniwa.moviender.network.Movie
import com.uniwa.moviender.network.MovienderApiService
import retrofit2.HttpException
import java.io.IOException

private const val API_STARTING_PAGE_INDEX = 1
private const val PAGE_INCREMENT = 1

class ServerPagingSource(
    private val service: MovienderApiService,
    private val genres: List<Int>
) : PagingSource<Int, Movie>() {
    override fun getRefreshKey(state: PagingState<Int, Movie>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(PAGE_INCREMENT)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(PAGE_INCREMENT)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> {
        val position = params.key ?: API_STARTING_PAGE_INDEX
        return try {
            val movies = service.getMovies(position, genres)
            val nextKey = if (movies.size < 15 || movies.isEmpty()) {
                null
            }
            else {
                position + PAGE_INCREMENT
            }

            LoadResult.Page(
                data = movies,
                prevKey = if (position == API_STARTING_PAGE_INDEX) null else position - PAGE_INCREMENT,
                nextKey = nextKey
            )
        } catch (exception: IOException) {
            return LoadResult.Error(exception)
        } catch (exception: HttpException) {
            return LoadResult.Error(exception)
        }
    }

}