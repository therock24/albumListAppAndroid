package work.therock24.albumapp.albums.presentation.album_list

import work.therock24.albumapp.albums.presentation.models.AlbumUiModel

sealed interface AlbumListAction {
    data class OnAlbumClick(val albumUiModel: AlbumUiModel): AlbumListAction
}