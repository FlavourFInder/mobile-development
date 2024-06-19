package com.example.flavorfinder.view.ui.home

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.recyclerview.widget.ListUpdateCallback
import com.example.flavorfinder.network.repository.MealRepository
import com.example.flavorfinder.network.response.MealsItem
import com.example.flavorfinder.network.response.MealsResponse
import com.example.flavorfinder.view.ui.adapter.MealListAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class HomeFragmentTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRules = MainDispatcherRule()

    @Mock
    private lateinit var mealsRepository: MealRepository
    private lateinit var homeViewModel: HomeViewModel
    private val dummyMeals = DataDummy.generateDummyMealsItem()

    @ExperimentalCoroutinesApi
    @Test
    fun `when get meal should not be null and return success`() = runTest {
        val data: PagingData<MealsItem> = MealPagingSource.snapshot(dummyMeals)
        val expectedMeal = MutableLiveData<PagingData<MealsItem>>()
        expectedMeal.value = data
        `when`(mealsRepository.getMeals()).thenReturn(expectedMeal)

        homeViewModel = HomeViewModel(mealsRepository)

        val actualMeal: PagingData<MealsItem> = homeViewModel.meal.getOrAwaitValue()

        val differ = AsyncPagingDataDiffer(
            diffCallback = MealListAdapter.DIFF_CALLBACK,
            updateCallback = noopListUpdateCallback,
            workerDispatcher = Dispatchers.Main
        )
        differ.submitData(actualMeal)

        assertNotNull(differ.snapshot())
        Assert.assertEquals(dummyMeals.size, differ.snapshot().size)
        Assert.assertEquals(dummyMeals[0], differ.snapshot()[0])
    }

    class MealPagingSource : PagingSource<Int, MealsItem>(){
        companion object{
            fun snapshot(items: List<MealsItem>): PagingData<MealsItem>{
                return PagingData.from(items)
            }
        }
        override fun getRefreshKey(state: PagingState<Int, MealsItem>): Int {
            return 0
        }

        override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MealsItem> {
            return LoadResult.Page(emptyList(), 0, 1)
        }
    }

    val noopListUpdateCallback = object : ListUpdateCallback {
        override fun onInserted(position: Int, count: Int) {}
        override fun onRemoved(position: Int, count: Int) {}
        override fun onMoved(fromPosition: Int, toPosition: Int) {}
        override fun onChanged(position: Int, count: Int, payload: Any?) {}
    }
}
