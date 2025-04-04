package work.therock24.albumapp.albums.presentation.models

import work.therock24.albumapp.albums.domain.model.Album

data class AlbumUiModel(
    val albumId: Int,
    val id: Int,
    val title: String,
    val url: String,
    val thumbnailUrl: String,
)

/*
 It is common that we need to do some data formatting from Domain to presentation layer
 So this is used to represent that conversion, even though right now we don't need to format any of the values.
 */
fun Album.toAlbumUi(): AlbumUiModel {
    return AlbumUiModel(
        albumId = albumId,
        id = id,
        title = title,
        url = url,
        thumbnailUrl = thumbnailUrl
    )
}