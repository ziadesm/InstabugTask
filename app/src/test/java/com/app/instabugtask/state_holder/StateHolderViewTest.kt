package com.app.instabugtask.state_holder
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.app.database.DatabaseHelperSingleton
import com.app.instabugtask.repo.CallingInstaBugFree
import com.app.network_helper.NetworkResponse
import com.google.common.truth.Truth
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
internal class StateHolderViewTest {
    private val mDatabase = mockk<DatabaseHelperSingleton>()
    private val mRepo = mockk<CallingInstaBugFree>()

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    val scope = TestScope()

    @Before
    fun setUp() {
        Dispatchers.setMain(StandardTestDispatcher(scope.testScheduler))
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun test_database_insert() = runTest(UnconfinedTestDispatcher()) {
//        every { mDatabase.insertWordsToDatabase("nothing") }
//        every { mDatabase.gettingWordFromDatabase() }
        every { mRepo.callInstaBugWebsite("https://instabug.com") } returns
                NetworkResponse.ApiError(Throwable("Nothing New"), 400)

        launch { mRepo.callInstaBugWebsite("https://instabug.com") }
        //advanceUntilIdle() // Yields to perform the registrations

        Truth.assertThat(mRepo.callInstaBugWebsite("https://instabug.com").toString())
            .isEqualTo(NetworkResponse.ApiError(Throwable("Nothing New"), 400).toString())

    }
}