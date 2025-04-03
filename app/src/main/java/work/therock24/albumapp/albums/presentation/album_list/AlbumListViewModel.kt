package work.therock24.albumapp.albums.presentation.album_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import work.therock24.albumapp.albums.domain.use_case.GetLocalAlbumsUseCase
import work.therock24.albumapp.albums.domain.use_case.SaveAlbumsUseCase
import work.therock24.albumapp.albums.domain.use_case.SyncAlbumsUseCase
import work.therock24.albumapp.albums.presentation.models.AlbumUiModel
import javax.inject.Inject
import work.therock24.albumapp.core.domain.Result
import work.therock24.albumapp.albums.domain.model.Album
import work.therock24.albumapp.albums.presentation.models.toAlbumUi

private const val PAGE_SIZE = 20

@HiltViewModel
class AlbumListViewModel @Inject constructor(
    private val getLocalAlbumsUseCase: GetLocalAlbumsUseCase,
    private val syncAlbumsUseCase: SyncAlbumsUseCase,
    private val saveAlbumsUseCase: SaveAlbumsUseCase
) : ViewModel() {

    /**
    * A [Flow] of paginated album data.
    * The data is cached in the [viewModelScope] to survive configuration changes.
    */
    val albums: Flow<PagingData<AlbumUiModel>> = getLocalAlbumsUseCase().cachedIn(viewModelScope).map { data ->
        data.map { album ->
            album.toAlbumUi()
        }
    }

    /**
     * Internal mutable state for tracking the current UI state.
     * This is exposed as a read-only [StateFlow] to the UI.
     */
    private val _uiState = MutableStateFlow<AlbumListUiState>(AlbumListUiState.Loading)
    val uiState: StateFlow<AlbumListUiState> = _uiState

    private val _uiEvent = MutableSharedFlow<AlbumListUiEvent>()
    val uiEvent = _uiEvent

    init {
        // Album data is fetched when the ViewModel is instantiated.
        // This way we ensure that fetchData is not called on configuration changes.
        syncAlbums()
    }

    fun onEvent(event: AlbumListEvent) {
        when (event) {
            AlbumListEvent.SyncAlbums -> {
                syncAlbums()
            }

            AlbumListEvent.CloseDialog -> {
            }
        }
    }

    /**
     * Fetches album data from the network and updates the local database.
     * This function handles the network request,
     * on success saves the fetched data to the local database,
     * And finally updates the UI state.
     */
    fun syncAlbums() {
        viewModelScope.launch {
            _uiState.update {
                AlbumListUiState.Loading
            }
            when (val result = syncAlbumsUseCase()) {
                is Result.Error -> {
                    _uiState.update {
                        AlbumListUiState.Offline
                    }
                    _uiEvent.emit(
                        AlbumListUiEvent.ShowSnackbar(
                            message = "You're offline, could not sync",
                            actionLabel = "Retry"
                        )
                    )
                }

                is Result.Success -> {
                    saveAlbumsUseCase(result.data)
                    _uiState.update {
                        AlbumListUiState.Success
                    }
                }
            }

        }
    }
}