@file:OptIn(ExperimentalMaterial3Api::class)

package work.therock24.albumapp.albums.presentation.album_list

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.LoadStates
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import kotlinx.coroutines.flow.MutableStateFlow
import work.therock24.albumapp.albums.presentation.models.AlbumUiModel
import work.therock24.albumapp.ui.theme.DevicePreview
import work.therock24.albumapp.util.TestTags
import work.therock24.albumapp.R
import work.therock24.albumapp.albums.presentation.album_list.composable.AlbumItem
import work.therock24.albumapp.ui.theme.Dimensions

@Composable
fun AlbumListRoute(viewModel: AlbumListViewModel = hiltViewModel()) {
    val pagedAlbums = viewModel.albums.collectAsLazyPagingItems()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.uiEvent.collect { event ->
            if (event is AlbumListUiEvent.ShowSnackbar) {
                val result = snackbarHostState.showSnackbar(
                    message = event.message,
                    actionLabel = event.actionLabel
                )
                if (result == SnackbarResult.ActionPerformed) {
                    viewModel.onEvent(AlbumListEvent.SyncAlbums)
                }
            }
        }
    }

    when (uiState) {
        AlbumListUiState.Loading -> FullScreenLoader()
        else -> AlbumListScreen(
            albumsPaging = pagedAlbums,
            onEventAction = viewModel::onEvent,
            snackbarHostState = snackbarHostState
        )
    }
}

@Composable
fun AlbumListScreen(
    albumsPaging: LazyPagingItems<AlbumUiModel>,
    snackbarHostState: SnackbarHostState,
    onEventAction: (AlbumListEvent) -> Unit,
    errorText: String? = null
) {
    Scaffold(
        topBar = { AlbumsAppBar() },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            RenderContent(albumsPaging, onEventAction)
            if (errorText != null) {
                AlbumsErrorDialog(
                    message = errorText,
                    onClose = { onEventAction(AlbumListEvent.CloseDialog) },
                    onRetry = {
                        onEventAction(AlbumListEvent.CloseDialog)
                        onEventAction(AlbumListEvent.SyncAlbums)
                    }
                )
            }
        }
    }
}

@Composable
fun AlbumsAppBar() {
    TopAppBar(
        title = {
            Text(
                text = stringResource(R.string.albums_text),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )
        },
        colors = topAppBarColors(
            containerColor = MaterialTheme.colorScheme.background
        )
    )
}

@Composable
fun RenderContent(
    albumsPaging: LazyPagingItems<AlbumUiModel>,
    onEventAction: (AlbumListEvent) -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        when (val state = albumsPaging.loadState.refresh) {
            is LoadState.Loading -> AlbumsLoadingOverlay()
            is LoadState.NotLoading -> {
                if (albumsPaging.itemCount > 0) {
                    AlbumsGrid(albumsPaging)
                } else {
                    EmptyAlbumsMessage(modifier = Modifier.align(Alignment.Center))
                }
            }
            is LoadState.Error -> Unit // Handle if needed
        }
    }
}

@Composable
fun AlbumsGrid(albumsPaging: LazyPagingItems<AlbumUiModel>) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(Dimensions.AlbumItemWidth),
        modifier = Modifier.testTag(TestTags.AlbumList)
    ) {
        items(albumsPaging.itemCount) { index ->
            albumsPaging[index]?.let {
                AlbumItem(it)
            }
        }
    }
}

@Composable
fun AlbumsLoadingOverlay() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.5f))
            .testTag(TestTags.LoadingOverlay)
            .clickable(enabled = false) {}
    ) {
        CircularProgressIndicator(
            modifier = Modifier
                .align(Alignment.Center)
                .testTag(TestTags.LoadingSpinner)
        )
    }
}

@Composable
fun EmptyAlbumsMessage(modifier: Modifier) {
    Text(
        text = stringResource(R.string.album_list_empty_text),
        modifier = modifier.testTag(TestTags.EmptyListText)
    )
}

@Composable
fun AlbumsErrorDialog(
    message: String,
    onClose: () -> Unit,
    onRetry: () -> Unit
) {
    AlertDialog(
        onDismissRequest = { onClose() },
        containerColor = Color.White,
        title = {
            Text(text = stringResource(R.string.album_list_dialog_error_title))
        },
        text = {
            Text(message)
        },
        confirmButton = {
            Button(onClick = onRetry) {
                Text(text = stringResource(R.string.album_list_dialog_error_retry_button))
            }
        }
    )
}

@Composable
fun FullScreenLoader() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@DevicePreview
@Composable
fun AlbumsScreen_Preview_Populated() {
    val fakeAlbums = listOf(
        AlbumUiModel(1, 1, "First Album", "https://via.placeholder.com/600/92c952", "https://via.placeholder.com/150/92c952"),
        AlbumUiModel(1, 2, "Second Album", "https://via.placeholder.com/600/771796", "https://via.placeholder.com/150/771796"),
        AlbumUiModel(1, 3, "Third Album", "https://via.placeholder.com/600/24f355", "https://via.placeholder.com/150/24f355"),
    )

    val albumsFlow = MutableStateFlow(PagingData.from(fakeAlbums))
    AlbumListScreen(
        albumsPaging = albumsFlow.collectAsLazyPagingItems(),
        snackbarHostState = remember { SnackbarHostState() },
        onEventAction = {}
    )
}

@DevicePreview
@Composable
fun AlbumsScreen_Preview_Loading() {
    val loadingState = PagingData.from(
        emptyList<AlbumUiModel>(),
        sourceLoadStates = LoadStates(
            refresh = LoadState.Loading,
            prepend = LoadState.Loading,
            append = LoadState.Loading
        )
    )

    AlbumListScreen(
        albumsPaging = MutableStateFlow(loadingState).collectAsLazyPagingItems(),
        snackbarHostState = remember { SnackbarHostState() },
        onEventAction = {}
    )
}

@DevicePreview
@Composable
fun AlbumsScreen_Preview_ErrorDialog() {
    val errorState = PagingData.from(
        emptyList<AlbumUiModel>(),
        sourceLoadStates = LoadStates(
            refresh = LoadState.Error(Throwable("Something went wrong")),
            prepend = LoadState.Loading,
            append = LoadState.Loading
        )
    )

    AlbumListScreen(
        albumsPaging = MutableStateFlow(errorState).collectAsLazyPagingItems(),
        snackbarHostState = remember { SnackbarHostState() },
        errorText = "Something went wrong",
        onEventAction = {}
    )
}

@DevicePreview
@Composable
fun AlbumsScreen_Preview_Snackbar() {
    val emptyPaging = PagingData.from(emptyList<AlbumUiModel>())

    Box(modifier = Modifier.fillMaxSize()) {
        AlbumListScreen(
            albumsPaging = MutableStateFlow(emptyPaging).collectAsLazyPagingItems(),
            snackbarHostState = SnackbarHostState(), // Not used here
            onEventAction = {}
        )

        // Manually injected Snackbar for preview purposes only
        Snackbar(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp),
            action = {
                Text("Retry", color = MaterialTheme.colorScheme.primary)
            }
        ) {
            Text("You're offline")
        }
    }
}
