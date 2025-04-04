package work.therock24.albumapp.albums.presentation.album_list

sealed class AlbumListUiEvent {
    data class ShowSnackbar(val message: String, val actionLabel: String? = null) : AlbumListUiEvent()
}