package work.therock24.albumapp.albums.presentation

import android.content.Context
import androidx.compose.material3.SnackbarHostState
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.paging.*
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import work.therock24.albumapp.R
import work.therock24.albumapp.util.TestDataProvider
import work.therock24.albumapp.albums.presentation.album_list.AlbumListScreen
import work.therock24.albumapp.albums.presentation.models.AlbumUiModel
import work.therock24.albumapp.util.TestTags

@RunWith(AndroidJUnit4::class)
class AlbumListScreenTest {

    @get:Rule
    val compose = createComposeRule()

    private val appContext: Context = ApplicationProvider.getApplicationContext()

    @Test
    fun whenAlbumsAreLoading_thenShowLoadingUI() {
        renderAlbumsScreen()

        compose.onNodeWithTag(TestTags.LoadingOverlay).assertIsDisplayed()
        compose.onNodeWithTag(TestTags.LoadingSpinner).assertIsDisplayed()
    }

    @Test
    fun whenAlbumsLoaded_thenDisplayListAndHideLoading() {
        renderAlbumsScreen(
            albumItems = TestDataProvider.albumsUiList,
            refreshState = LoadState.NotLoading(false)
        )

        compose.onNodeWithTag(TestTags.AlbumList).assertIsDisplayed()
        compose.onNodeWithTag(TestTags.LoadingOverlay).assertIsNotDisplayed()
        compose.onNodeWithTag(TestTags.LoadingSpinner).assertIsNotDisplayed()
    }

    @Test
    fun whenNoAlbums_thenShowEmptyMessage() {
        renderAlbumsScreen(refreshState = LoadState.NotLoading(false))

        compose.onNodeWithTag(TestTags.EmptyListText).assertIsDisplayed()
        compose.onNodeWithTag(TestTags.AlbumList).assertIsNotDisplayed()
        compose.onNodeWithTag(TestTags.LoadingOverlay).assertIsNotDisplayed()
        compose.onNodeWithTag(TestTags.LoadingSpinner).assertIsNotDisplayed()
    }

    @Test
    fun whenErrorOccurs_thenDisplayErrorDialog() {
        val errorText = "Something went wrong"
        val title = appContext.getString(R.string.general_error)
        val buttonLabel = appContext.getString(R.string.general_retry)

        renderAlbumsScreen(
            refreshState = LoadState.NotLoading(false),
            errorMessage = errorText
        )

        compose.onNodeWithText(title).assertIsDisplayed()
        compose.onNodeWithText(errorText).assertIsDisplayed()
        compose.onNodeWithText(buttonLabel).assertIsDisplayed()
    }

    @Test
    fun whenRetryButtonClicked_thenTriggerClickAction() {
        val errorMessage = "Something went wrong"

        renderAlbumsScreen(
            refreshState = LoadState.NotLoading(false),
            errorMessage = errorMessage
        )

        val retryLabel = appContext.getString(R.string.general_retry)

        compose.onNodeWithText(retryLabel)
            .assertIsDisplayed()
            .performClick()
    }

    private fun renderAlbumsScreen(
        albumItems: List<AlbumUiModel> = emptyList(),
        refreshState: LoadState = LoadState.Loading,
        errorMessage: String? = null,
        snackbarState: SnackbarHostState = SnackbarHostState()
    ) = runTest {
        val pagingData = PagingData.from(
            data = albumItems,
            sourceLoadStates = LoadStates(
                refresh = refreshState,
                prepend = LoadState.Loading,
                append = LoadState.Loading,
            )
        )

        compose.setContent {
            val lazyPagingItems = flowOf(pagingData).collectAsLazyPagingItems()
            AlbumListScreen(
                albumsPaging = lazyPagingItems,
                onEventAction = {},
                errorText = errorMessage,
            )
        }
    }
}
