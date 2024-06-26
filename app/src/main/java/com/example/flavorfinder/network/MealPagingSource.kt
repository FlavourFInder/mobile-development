package com.example.flavorfinder.network

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.flavorfinder.network.response.MealsItem
import com.example.flavorfinder.network.retrofit.MealsApiService

class MealPagingSource(private val mealsApiService: MealsApiService): PagingSource<Int, MealsItem>() {
    private companion object {
        const val INITIAL_PAGE_INDEX = 1
    }

    override fun getRefreshKey(state: PagingState<Int, MealsItem>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MealsItem> {
        return try {
            val position = params.key ?: INITIAL_PAGE_INDEX
            val responseData = mealsApiService.getMeals(position, params.loadSize)

            LoadResult.Page(
                data = responseData.meals,
                prevKey = if (position == INITIAL_PAGE_INDEX) null else position - 1,
                nextKey = if (responseData.meals.isNullOrEmpty()) null else position + 1
            )
        } catch (exception: Exception) {
            return LoadResult.Error(exception)
        }
    }

}