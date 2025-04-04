package work.therock24.albumapp.albums.presentation.album_list

import androidx.paging.PagingData
import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import work.therock24.albumapp.albums.domain.model.Album
import work.therock24.albumapp.albums.domain.use_case.GetLocalAlbumsUseCase
import work.therock24.albumapp.albums.domain.use_case.SaveAlbumsUseCase
import work.therock24.albumapp.albums.domain.use_case.SyncAlbumsUseCase
import work.therock24.albumapp.albums.presentation.SnackbarController
import work.therock24.albumapp.albums.presentation.SnackbarEvent
import work.therock24.albumapp.albums.presentation.SnackbarEventType
import work.therock24.albumapp.core.domain.Error
import work.therock24.albumapp.core.domain.Result
import work.therock24.albumapp.rules.MainDispatcherRule
import kotlinx.coroutines.channels.Channel

class AlbumListViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule(testDispatcher)

    private val getLocalAlbumsUseCase: GetLocalAlbumsUseCase = mockk(relaxed = true)
    private val syncAlbumsUseCase: SyncAlbumsUseCase = mockk()
    private val saveAlbumsUseCase: SaveAlbumsUseCase = mockk(relaxed = true)

    private val fakeAlbums = listOf(
        Album(id = 1, albumId = 1, title = "Title 1", url = "url1", thumbnailUrl = "thumb1"),
        Album(id = 2, albumId = 2, title = "Title 2", url = "url2", thumbnailUrl = "thumb2")
    )

    private lateinit var snackbarChannel: Channel<SnackbarEvent>

    @Before
    fun setup() {
        val pagingData = PagingData.from(fakeAlbums)
        val flow: Flow<PagingData<Album>> = flowOf(pagingData)
        every { getLocalAlbumsUseCase() } returns flow

        snackbarChannel = Channel(Channel.UNLIMITED)
        SnackbarController.overrideEvents(snackbarChannel)
    }

    @Test
    fun `syncAlbums - success - updates state and saves albums`() = runTest(testDispatcher) {
        coEvery { syncAlbumsUseCase() } returns Result.Success(fakeAlbums)

        val viewModel = createViewModel()

        viewModel.uiState.test {
            assertThat(awaitItem()).isEqualTo(AlbumListUiState.Loading)
            assertThat(awaitItem()).isEqualTo(AlbumListUiState.Success)
        }

        coVerify(exactly = 1) { saveAlbumsUseCase(fakeAlbums) }
    }

    @Test
    fun `syncAlbums - error - updates state and emits snackbar`() = runTest(testDispatcher) {
        coEvery { syncAlbumsUseCase() } returns Result.Error(Error.NetworkError.NO_INTERNET)

        val viewModel = createViewModel()

        viewModel.uiState.test {
            assertThat(awaitItem()).isEqualTo(AlbumListUiState.Loading)
            assertThat(awaitItem()).isEqualTo(AlbumListUiState.Offline)
            cancelAndIgnoreRemainingEvents()
        }

        val snackbarEvent = snackbarChannel.receive()
        assertThat(snackbarEvent.type).isEqualTo(SnackbarEventType.Offline)
    }

    @Test
    fun `init - should trigger syncAlbums on success`() = runTest(testDispatcher) {
        coEvery { syncAlbumsUseCase() } returns Result.Success(fakeAlbums)

        val viewModel = createViewModel()

        viewModel.uiState.test {
            assertThat(awaitItem()).isEqualTo(AlbumListUiState.Loading)
            assertThat(awaitItem()).isEqualTo(AlbumListUiState.Success)
            cancelAndIgnoreRemainingEvents()
        }

        coVerify(exactly = 1) { syncAlbumsUseCase() }
        coVerify(exactly = 1) { saveAlbumsUseCase(fakeAlbums) }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `init - sync fails - should update to offline and emit snackbar`() = runTest {
        coEvery { syncAlbumsUseCase() } returns Result.Error(Error.NetworkError.REQUEST_TIMEOUT)

        val viewModel = createViewModel()

        viewModel.uiState.test {
            assertThat(awaitItem()).isEqualTo(AlbumListUiState.Loading)
            assertThat(awaitItem()).isEqualTo(AlbumListUiState.Offline)
            cancelAndIgnoreRemainingEvents()
        }

        val snackbarEvent = snackbarChannel.receive()
        assertThat(snackbarEvent.type).isEqualTo(SnackbarEventType.Offline)

        coVerify(exactly = 1) { syncAlbumsUseCase() }
        coVerify(exactly = 0) { saveAlbumsUseCase(any()) }
    }

    private fun createViewModel(): AlbumListViewModel {
        return AlbumListViewModel(
            getLocalAlbumsUseCase,
            syncAlbumsUseCase,
            saveAlbumsUseCase,
            connectivityObserver = mockk(relaxed = true)
        )
    }
}
