package com.example.flavorfinder.view.ui.home

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.recyclerview.widget.ListUpdateCallback
import com.example.flavorfinder.network.repository.MealRepository
import com.example.flavorfinder.network.response.MealsItem
import com.example.flavorfinder.network.response.MealsResponse
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
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
    @OptIn(ExperimentalCoroutinesApi::class)
    private val testDispatcher = UnconfinedTestDispatcher()

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var mealsRepository: MealRepository
    private lateinit var homeViewModel: HomeViewModel
    private val dummyMeals = DataDummy.generateDummyMealsItem()

    @Before
    fun setUp() {
        homeViewModel = HomeViewModel(mealsRepository)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `when get meal should not be null and return success`() = runTest(testDispatcher) {
        val observer = Observer<PagingData<MealsItem>> {}
        try {
            val pagingData = PagingData.from(dummyMeals)
            val expectedMeals = MutableLiveData<PagingData<MealsItem>>()
            expectedMeals.value = pagingData

            `when`(mealsRepository.getMeals()).thenReturn(expectedMeals)

            homeViewModel.meal.observeForever(observer)

            val actualMeals = homeViewModel.meal.value

            verify(mealsRepository).getMeals()
            assertNotNull(actualMeals)
        } finally {
            homeViewModel.meal.removeObserver(observer)
        }
    }

    class MealPagingSource : PagingSource<Int, MealsItem>(){
        companion object{
//            di mealPagingSource asli seperti ini
//            const val INITIAL_PAGE_INDEX = 1

//            ini mengikuti contoh modul
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
