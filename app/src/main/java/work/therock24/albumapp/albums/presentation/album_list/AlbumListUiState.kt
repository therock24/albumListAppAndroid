package work.therock24.albumapp.albums.presentation.album_list

sealed class AlbumListUiState {
    data object Loading : AlbumListUiState()
    data object Offline : AlbumListUiState()
    data object Success : AlbumListUiState()
}

