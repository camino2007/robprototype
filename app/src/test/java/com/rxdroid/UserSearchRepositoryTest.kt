package com.rxdroid

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.rxdroid.api.github.model.GitHubUserData
import com.rxdroid.api.github.search.SearchApiProvider
import com.rxdroid.app.util.RxSchedulerRule
import com.rxdroid.data.search.UserDatabaseProvider
import com.rxdroid.data.search.UserDto
import com.rxdroid.repository.model.Status
import com.rxdroid.repository.repositories.search.UserSearchRepository
import com.rxdroid.repository.repositories.search.UserSearchRepositoryImpl
import io.mockk.every
import io.mockk.mockk
import io.reactivex.Completable
import io.reactivex.Single
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import retrofit2.Response
import java.net.HttpURLConnection

class UserSearchRepositoryTest {

    companion object {
        private const val TEST_USER = "testUser"
    }

    @get:Rule
    val rxSchedulerRule = RxSchedulerRule()

    @get:Rule
    val taskExecutorRule = InstantTaskExecutorRule()

    private val searchApiProvider = mockk<SearchApiProvider>()
    private val userDatabaseProvider = mockk<UserDatabaseProvider>()

    private lateinit var tested: UserSearchRepository

    private lateinit var userData: GitHubUserData
    private lateinit var userDto: UserDto

    @Before
    fun setUp() {
        tested = UserSearchRepositoryImpl(searchApiProvider, userDatabaseProvider)

        userData = GitHubUserData(id = 666, name = "test", login = TEST_USER, avatarUrl = "avatarUrl",
                publicGistCount = 0, publicRepoCount = 0, company = "company", email = "email",
                hireable = "true", htmlUrl = "htmlUrl", isSiteAdmin = false, type = "type", url = "url")

        userDto = UserDto(name = userData.name,
                login = userData.login,
                publicRepoCount = userData.publicRepoCount,
                publicGistCount = userData.publicGistCount,
                githubUserId = userData.id)
    }

    @Test
    fun testUserSearchOnSuccess() {
        val userResponse: Response<GitHubUserData> = Response.success(userData)
        every { searchApiProvider.findUserBySearchValue(TEST_USER) } returns Single.just(userResponse)
        every { userDatabaseProvider.insertOrUpdate(userDto) } returns Completable.complete()

        val expectedFlowable = tested.searchForUser(TEST_USER)

        val expected = expectedFlowable.blockingFirst()
        assert(expected.status == Status.SUCCESS)
        assert(expected.data?.login == TEST_USER)
    }

    @Test
    fun testUserSearchOnError() {
        val userResponse: Response<GitHubUserData> = Response.error(HttpURLConnection.HTTP_NOT_FOUND, mockk())
        every { searchApiProvider.findUserBySearchValue(TEST_USER) } returns Single.just(userResponse)
        every { userDatabaseProvider.insertOrUpdate(userDto) } returns Completable.complete()

        val expectedFlowable = tested.searchForUser(TEST_USER)

        val expected = expectedFlowable.blockingFirst()
        assert(expected.status == Status.ERROR)
    }

    @Test
    fun testEmptyUserSearch() {
        val expectedFlowable = tested.searchForUser("")

        val expected = expectedFlowable.blockingFirst()
        assert(expected.status == Status.ERROR)
    }

}