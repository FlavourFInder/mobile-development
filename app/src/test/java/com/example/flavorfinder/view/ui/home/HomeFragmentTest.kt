package com.example.flavorfinder.view.ui.home

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.paging.PagingData
import com.example.flavorfinder.network.repository.MealRepository
import com.example.flavorfinder.network.response.MealsItem
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
}
