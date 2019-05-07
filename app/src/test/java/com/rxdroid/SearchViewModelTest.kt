package com.rxdroid

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.rxdroid.app.search.SearchViewModel
import com.rxdroid.app.search.UserItemViewModel
import com.rxdroid.app.util.RxSchedulerRule
import com.rxdroid.app.util.testObserver
import com.rxdroid.repository.model.Resource
import com.rxdroid.repository.model.User
import com.rxdroid.repository.repositories.search.UserSearchRepository
import io.mockk.every
import io.mockk.mockk
import io.reactivex.Observable
import io.reactivex.schedulers.TestScheduler
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class SearchViewModelTest {

    @get:Rule
    val rxSchedulerRule = RxSchedulerRule()

    @get:Rule
    val taskExecutorRule = InstantTaskExecutorRule()

    lateinit var tested: SearchViewModel

    private val repository = mockk<UserSearchRepository>()

    @Before
    fun setUp() {
        tested = SearchViewModel(repository, TestScheduler())
    }

    @Test
    fun testUserItem() {
        val testUser = mockk<User>()
        every { testUser.login } returns "testUser"
        val testUserResource: Resource<User> = Resource.success(testUser)
        every { repository.searchForUser("testUser") } returns Observable.just(testUserResource)

        tested.initialize()
        tested.updateSearchInput("testUser")

        val testObserver = tested.getUserItems().testObserver()
        val expected = testObserver.observedValues.first()?.get(0)
        assert(expected is UserItemViewModel)
    }

}