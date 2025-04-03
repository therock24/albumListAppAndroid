package work.therock24.albumapp.albums.presentation.album_list

sealed class AlbumListEvent {
    data object SyncAlbums : AlbumListEvent()
    data object CloseDialog : AlbumListEvent()
}