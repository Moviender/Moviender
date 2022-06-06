package com.uniwa.moviender.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.uniwa.moviender.network.Movie
import com.uniwa.moviender.network.client.MovieClient
import retrofit2.HttpException
import java.io.IOException

private const val API_STARTING_PAGE_INDEX = 1
private const val PAGE_INCREMENT = 1

class ServerPagingSource(
    private val service: MovieClient,
    private val genres: List<Int>,
    private val uid: String
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
            val movies = if (genres[0] != -1) {
                service.getMovies(position, genres).body
            } else {
                service.getUserRecommendations(position, uid).body
            }
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